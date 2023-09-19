package com.ray.pominowner.menu.controller;

import com.ray.pominowner.menu.controller.dto.MenuRequest;
import com.ray.pominowner.menu.domain.MenuImage;
import com.ray.pominowner.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/menus")
public class MenuController {

    private final MenuService menuService;

    @PostMapping(value = "/stores/{storeId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> registerMenu(@RequestPart MenuRequest request, @RequestPart MultipartFile image, @PathVariable Long storeId) {
        MenuImage menuImage = menuService.createImage(image);
        Long menuId = menuService.registerMenu(request.generateMenuEntity(menuImage));

        final String url = "/api/v1/menus/%d/stores/%d".formatted(menuId, storeId);

        return ResponseEntity.created(URI.create(url)).build();
    }

    @PostMapping(value = "/{menuId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void updateMenu(@RequestPart MenuRequest request, @RequestPart MultipartFile image, @PathVariable Long menuId) {
        MenuImage menuImage = menuService.createImage(image);
        menuService.updateMenu(request.generateMenuEntity(menuImage), menuId);
    }

}
