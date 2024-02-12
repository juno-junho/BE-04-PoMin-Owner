package com.ray.pominowner.menu.service;

import com.ray.pominowner.menu.domain.Menu;
import com.ray.pominowner.menu.domain.MenuOptionGroup;
import com.ray.pominowner.menu.domain.OptionGroup;
import com.ray.pominowner.menu.repository.MenuRepository;
import com.ray.pominowner.menu.repository.OptionGroupRepository;
import com.ray.pominowner.menu.service.vo.OptionGroupInfoToSend;
import com.ray.pominowner.store.service.aop.TransmitData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final OptionGroupRepository optionGroupRepository;

    @TransmitData
    public Menu registerMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    @TransmitData
    public Menu updateMenu(Menu menu, Long menuId) {
        Menu findMenu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("해당 메뉴가 존재하지 않습니다."));
        Menu updatedMenu = findMenu.updateMenu(menu);
        return menuRepository.save(updatedMenu);
    }

    @TransmitData
    public OptionGroupInfoToSend addOptionGroupToMenu(Long menuId, Long optionGroupId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("해당 메뉴가 존재하지 않습니다."));

        OptionGroup optionGroup = optionGroupRepository.findById(optionGroupId)
                .orElseThrow(() -> new IllegalArgumentException("해당 옵션 그룹이 존재하지 않습니다."));

        menu.addMenuOptionGroup(new MenuOptionGroup(menu, optionGroup));
        menuRepository.save(menu);

        return OptionGroupInfoToSend.from(optionGroup, menu);
    }
}
