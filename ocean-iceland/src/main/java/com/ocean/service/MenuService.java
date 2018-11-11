package com.ocean.service;

import com.ocean.model.pojo.Menu;
import com.ocean.model.vo.MenuVo;

import java.util.List;

/**
 * Created by niu on 18/1/22.
 */
public interface MenuService {

    List<Menu> getAllMenus(Long groupId);

    MenuVo getMenuVos(Long menuId);
}
