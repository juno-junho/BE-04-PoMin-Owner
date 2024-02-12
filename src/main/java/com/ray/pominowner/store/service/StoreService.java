package com.ray.pominowner.store.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ray.pominowner.global.vo.InfoSender;
import com.ray.pominowner.store.domain.Store;
import com.ray.pominowner.store.repository.StoreRepository;
import com.ray.pominowner.store.service.aop.TransmitData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class StoreService {

    private final StoreServiceValidator storeServiceValidator;

    private final StoreRepository storeRepository;

    private final StoreCategoryService storeCategoryService;

    private final StoreImageService storeImageService;

    @TransmitData
    public Store registerStore(Store store) throws JsonProcessingException {
        storeServiceValidator.validateBusinessNumber(store.getBusinessNumber());
        return storeRepository.save(store);
    }

    @TransmitData
    public Store registerCategory(List<String> categories, Long storeId) {
        storeServiceValidator.validateCategory(categories);
        Store store = findStore(storeId);
        storeCategoryService.saveCategories(store, categories);

        return store;
    }

    @TransmitData
    public Store registerPhoneNumber(String phoneNumber, Long storeId) {
        Store store = findStore(storeId).retrieveStoreAfterRegisteringPhoneNumber(phoneNumber);
        return storeRepository.save(store);
    }

    @TransmitData
    public Store deletePhoneNumber(Long storeId) {
        Store store = findStore(storeId).retrieveStoreAfterDeletingPhoneNumber();
        return storeRepository.save(store);
    }

    @TransmitData
    public Store registerInformation(String information, Long storeId) {
        Store store = findStore(storeId).retrieveStoreAfterRegisteringInfo(information);
        return storeRepository.save(store);
    }

    @TransmitData
    public Store deleteInformation(Long storeId) {
        Store store = findStore(storeId).retrieveStoreAfterDeletingInfo();
        return storeRepository.save(store);
    }

    @TransmitData
    public Store saveStoreImages(List<MultipartFile> images, Long storeId) {
        Store store = findStore(storeId);
        storeImageService.saveImages(images, store);
        return store;
    }

    private Store findStore(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 가게입니다."));
    }

}
