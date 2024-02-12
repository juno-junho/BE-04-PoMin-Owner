package com.ray.pominowner.store.service.aop;

import com.ray.pominowner.global.vo.InfoSender;
import com.ray.pominowner.menu.domain.Menu;
import com.ray.pominowner.menu.service.vo.OptionGroupInfoToSend;
import com.ray.pominowner.store.domain.Store;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DataTransferAspect {

    private final InfoSender infoSender;

    @Pointcut("@annotation(com.ray.pominowner.store.service.aop.TransmitData)")
    private void toTransmitDataAnnotation() {}

    @AfterReturning(pointcut = "toTransmitDataAnnotation()", returning = "store")
    public void transmitData(Store store) {
        infoSender.send(store);
        log.info("store info sent");
    }

    @AfterReturning(pointcut = "toTransmitDataAnnotation()", returning = "menu")
    public void transmitData(Menu menu) {
        infoSender.send(menu);
        log.info("menu info sent");
    }

    @AfterReturning(pointcut = "toTransmitDataAnnotation()", returning = "optionInfo")
    public void transmitData(OptionGroupInfoToSend optionInfo) {
        infoSender.send(optionInfo);
        log.info("option info sent");
    }

}
