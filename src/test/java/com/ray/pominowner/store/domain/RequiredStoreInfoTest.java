package com.ray.pominowner.store.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RequiredStoreInfoTest {

    @ParameterizedTest
    @ValueSource(longs = {0, -1, -2, -100})
    @DisplayName("사업자등록번호가 0이하이면 예외가 발생한다")
    void failWhenBusinessNumberIsLessThanOne(Long businessNumber) {
        assertThatThrownBy(() -> new RequiredStoreInfo(businessNumber, "name", "address", "logoImage"))
                .isInstanceOf(IllegalStateException.class);
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 12, 123, 1234, 12345, 123456, 1234567, 12345678, 123456789})
    @DisplayName("사업자등록번호가 10자리가 아니면 예외가 발생한다")
    void failWhenBusinessNumberIsNotTenDigitNumber(Long businessNumber) {
        assertThatThrownBy(() -> new RequiredStoreInfo(businessNumber, "name", "address", "logoImage"))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("성공적으로 가게 엔티티가 생성된다")
    void successGeneratingStoreEntity() {
        assertThatNoException()
                .isThrownBy(() -> new RequiredStoreInfo(1234567890L, "name", "address", "logoImage"));
    }


    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    @NullSource
    @DisplayName("가게이름이 null이거나 공백이면 예외가 발생한다")
    void failWhenStoreNameHasNoText(String storeName) {
        assertThatThrownBy(() -> new RequiredStoreInfo(1234567890L, storeName, "address", "logoImage"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    @NullSource
    @DisplayName("주소가 null이거나 공백이면 예외가 발생한다")
    void failWhenAddressHasNoText(String storeAddress) {
        assertThatThrownBy(() -> new RequiredStoreInfo(1234567890L, "name", storeAddress, "logoImage"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    @NullSource
    @DisplayName("로고 이미지 url이 null이거나 공백이면 예외가 발생한다")
    void failWhenLogoImageUrlHasNoText(String logoImageUrl) {
        assertThatThrownBy(() -> new RequiredStoreInfo(1234567890L, "name", "storeAddress", logoImageUrl))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
