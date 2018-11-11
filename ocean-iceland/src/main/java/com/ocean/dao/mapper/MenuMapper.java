package com.ocean.dao.mapper;

import com.ocean.model.pojo.Menu;
import com.ocean.model.vo.MenuVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by niu on 18/1/22.
 */
@Repository
public interface MenuMapper {

    List<Menu> getAllMenus(Long groupId);

    List<Menu> getMenusBypId(Long pId);

    Menu getMenuByMenuId(Long menuId);

    MenuVo getMenuVoByMenuId(Long menuId);
}
