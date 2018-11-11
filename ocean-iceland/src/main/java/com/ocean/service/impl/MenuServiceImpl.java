package com.ocean.service.impl;

import com.ocean.dao.mapper.MenuMapper;
import com.ocean.model.pojo.Menu;
import com.ocean.model.vo.MenuVo;
import com.ocean.service.MenuService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by niu on 18/1/22.
 */
@Service("menuService")
public class MenuServiceImpl implements MenuService {

    private static Log log = LogFactory.getLog(MenuServiceImpl.class);

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<Menu> getAllMenus(Long groupId) {

        List<Menu> rootMenu = menuMapper.getAllMenus(groupId);
        // 最后的结果
        List<Menu> menuList = new ArrayList<Menu>();
        // 先找到所有的一级菜单
        for (int i = 0; i < rootMenu.size(); i++) {
            // 一级菜单没有parentId
            if (rootMenu.get(i).getpId() == 0) {
                menuList.add(rootMenu.get(i));
            }
        }
        // 为一级菜单设置子菜单，getChild是递归调用的
        for (Menu menu : menuList) {
            menu.setChildren(getChild(menu.getMenuId(), rootMenu));
        }
        return menuList;
    }

    @Override
    public MenuVo getMenuVos(Long menuId) {
        MenuVo menuVo = menuMapper.getMenuVoByMenuId(menuId);
        menuVo.setChildren(getChildren(menuId));
        return menuVo;
    }

    /**
     * 递归查找子菜单
     *
     * @param menuId   当前菜单id
     * @param rootMenu 要查找的列表
     * @return
     */
    private List<Menu> getChild(Long menuId, List<Menu> rootMenu) {

        // 子菜单
        List<Menu> children = new ArrayList<>();
        for (Menu menu : rootMenu) {
            // 遍历所有节点，将父菜单id与传过来的id比较
            if (menu.getpId() != null) {
                if (menu.getpId().equals(menuId)) {
                    children.add(menu);
                }
            }
        }
        // 把子菜单的子菜单再循环一遍
        for (Menu menu : children) {// 没有url子菜单还有子菜单
            if (null == menu.getUrl() || "".equals(menu.getUrl())) {
                // 递归
                menu.setChildren(getChild(menu.getMenuId(), rootMenu));
            }
        }
        // 递归退出条件
        if (children.size() == 0) {
            return new ArrayList<>();
        }
        return children;
    }

    /**
     * 递归子菜单中的KV
     *
     * @param menuId
     * @return
     */
    private List<MenuVo> getChildren(Long menuId){

        List<MenuVo> children = new ArrayList<>();
        List<Menu> menus = menuMapper.getMenusBypId(menuId);
        for (Menu menu : menus) {
            if (null == menu.getUrl() || "".equals(menu.getUrl())) {
                // 递归
                MenuVo menuVo = menuMapper.getMenuVoByMenuId(menu.getMenuId());
                menuVo.setChildren(getChildren(menu.getMenuId()));
                children.add(menuVo);
            }
        }
        // 递归退出条件
        if (menus.size() == 0) {
            return new ArrayList();
        }
        return children;
    }


}
