package com.ray.pominowner.orders.service;

import com.ray.pominowner.global.domain.PhoneNumber;
import com.ray.pominowner.orders.domain.Order;
import com.ray.pominowner.orders.domain.OrderStatus;
import com.ray.pominowner.orders.repository.OrderRepository;
import com.ray.pominowner.payment.domain.PGType;
import com.ray.pominowner.payment.domain.Payment;
import com.ray.pominowner.payment.domain.PaymentStatus;
import com.ray.pominowner.payment.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private PaymentService paymentService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ReceiptNumberGenerator generator;

    private Order order;

    @BeforeEach
    void setup() {
        order = Order.builder()
                .id(1L)
                .orderNumber("ORDERNUMBER")
                .status(OrderStatus.CONFIRMING)
                .requestedDetails("덜맵게 해주세요")
                .totalPrice(30000)
                .customerPhoneNumber(new PhoneNumber("01012345678"))
                .storeId(1L)
                .paymentId(1L)
                .build();
    }

    @Test
    @DisplayName("주문 접수를 성공한다")
    void successOrderSave() {
        // given
        given(orderRepository.save(order)).willReturn(order);

        // when
        Order receivedOrder = orderService.receiveOrder(order);

        // then
        assertThat(receivedOrder).isEqualTo(order);
    }

    @Test
    @DisplayName("주문 수락을 성공한다")
    void successAcceptOrder() {
        // given
        given(orderRepository.save(order)).willReturn(order);
        given(orderRepository.findById(1L)).willReturn(Optional.ofNullable(order));
        given(generator.incrementAndGet()).willReturn(1);

        // when
        Order approvedOrder = orderService.approve(order.getId());

        // then
        assertThat(approvedOrder).hasFieldOrPropertyWithValue("status", OrderStatus.COOKING);
        assertThat(approvedOrder).hasFieldOrPropertyWithValue("receiptNumber", 1);
        assertThat(approvedOrder).hasFieldOrPropertyWithValue("estimatedCookingTime", approvedOrder.getEstimatedCookingTime());
    }

    @Test
    @DisplayName("주문 거절을 성공한다")
    void successRejectOrder() {
        // given
        Payment canceled = new Payment(1L, 1000, PaymentStatus.CANCELED, PGType.TOSS);

        given(orderRepository.save(order)).willReturn(order);
        given(orderRepository.findById(1L)).willReturn(Optional.ofNullable(order));
        given(paymentService.cancel(order.getPaymentId())).willReturn(canceled);

        // when
        Order rejectedOrder = orderService.reject(order.getId());

        // then
        assertThat(rejectedOrder).hasFieldOrPropertyWithValue("status", OrderStatus.REJECTED);
        assertThat(rejectedOrder).hasFieldOrPropertyWithValue("rejectReason", "재고 소진");
    }

}
