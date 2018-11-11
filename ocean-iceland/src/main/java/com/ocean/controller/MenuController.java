package com.ocean.controller;

import com.ocean.model.pojo.Menu;
import com.ocean.model.pojo.OfficialKv;
import com.ocean.model.vo.MenuVo;
import com.ocean.service.MenuService;
import com.ocean.service.OfficialKvService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by niu on 18/1/22.
 */

@RestController
@RequestMapping("/businessManage")
public class MenuController {

    private Log log = LogFactory.getLog(MenuController.class);

    @Autowired
    private MenuService menuService;
    @Autowired
    private OfficialKvService officialKvService;

    @RequestMapping(value = "/menus/{groupId}", method = RequestMethod.GET)
    public List getMenus(@PathVariable("groupId") Long groupId) {

        List<Menu> menus = menuService.getAllMenus(groupId);
        return menus;
    }

    @RequestMapping(value = "/getMenusOfficialKv/{menuId}", method = RequestMethod.GET)
    public MenuVo getMenusOfficialKv(@PathVariable Long menuId) {

        MenuVo menuVos = menuService.getMenuVos(menuId);
        return menuVos;
    }

    @RequestMapping(value = "/getOfficialKvs", method = RequestMethod.GET)
    public List getOfficialKvs() {

        List<OfficialKv> officialKv = officialKvService.getAllOfficialKvs();
        return officialKv;
    }

    @RequestMapping(value = "/getOfficialKv", method = RequestMethod.GET)
    public OfficialKv getOfficialKv(String id) {

        OfficialKv officialKv = officialKvService.getOfficialKvById(id);
        return officialKv;
    }

    @RequestMapping(value = "/searchOfficialKvs", method = RequestMethod.GET)
    public Map OfficialKvs(OfficialKv officialKv, Integer pageIndex, Integer pageLimit) {
        if (pageIndex == null ) {
            pageIndex = 0;
        }
        if (pageLimit == null ) {
            pageLimit = 100;
        }
        Map map = officialKvService.searchOfficialKvs(officialKv, pageIndex, pageLimit);
        return map;
    }

    @RequestMapping(value = "/addOfficialKv", method = RequestMethod.POST)
    public Map addOfficialK(@RequestBody OfficialKv officialKv) {

        Map<String, Object> map = new HashMap<String, Object>();
        try {
            int result = officialKvService.insertOfficialKv(officialKv);
            if (result > 0) {
                map.put("status", 200);
                map.put("message", "KV添加成功!");
            } else {
                map.put("status", 500);
                map.put("message", "KV添加失败!");
            }
        } catch (Exception e) {
            log.error("KV添加失败", e);
            e.printStackTrace();
            map.put("status", 500);
            map.put("message", "KV添加失败!");
        }
        return map;
    }

    @RequestMapping(value = "/deleteOfficialKv/{id}", method = RequestMethod.GET)
    public Map deleteOfficialKv(@PathVariable String id) {

        Map<String, Object> map = new HashMap<String, Object>();
        try {
            int result = officialKvService.deleteOfficialKv(id);
            if (result > 0) {
                map.put("status", 200);
                map.put("message", "KV删除成功!");
            } else {
                map.put("status", 500);
                map.put("message", "KV删除失败!");
            }
        } catch (Exception e) {
            log.error("KV删除失败", e);
            e.printStackTrace();
            map.put("status", 500);
            map.put("message", "KV删除失败!");
        }
        return map;
    }

    @RequestMapping(value = "/updateOfficialKv", method = RequestMethod.POST)
    public Map updateOfficialKv(@RequestBody OfficialKv officialKv) {

        Map<String, Object> map = new HashMap<String, Object>();
        try {
            int result = officialKvService.updateOfficialKv(officialKv);
            if (result > 0) {
                map.put("status", 200);
                map.put("message", "KV更新成功!");
            } else {
                map.put("status", 500);
                map.put("message", "KV更新失败!");
            }
        } catch (Exception e) {
            log.error("KV更新失败", e);
            e.printStackTrace();
            map.put("status", 500);
            map.put("message", "KV更新失败!");
        }
        return map;
    }

}
