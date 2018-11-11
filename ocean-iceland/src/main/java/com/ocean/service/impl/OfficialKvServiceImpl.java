package com.ocean.service.impl;

import com.ocean.dao.mapper.MenuMapper;
import com.ocean.dao.mapper.OfficialKvMapper;
import com.ocean.model.pojo.Menu;
import com.ocean.model.pojo.OfficialKv;
import com.ocean.service.OfficialKvService;
import com.ocean.util.CommonUtil;
import com.ocean.util.DateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by niu on 18/1/22.
 */
@Service("officialKvService")
public class OfficialKvServiceImpl implements OfficialKvService {

    private static Log log = LogFactory.getLog(OfficialKvServiceImpl.class);

    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private OfficialKvMapper officialKvMapper;

    @Override
    public List<OfficialKv> getAllOfficialKvs() {
        return officialKvMapper.getAllOfficialKvs();
    }

    @Override
    public Map searchOfficialKvs(OfficialKv officialKv, Integer pageIndex, Integer pageLimit) {

        List<OfficialKv> officialKvs = officialKvMapper.searchOfficialKvs(officialKv.getId(), officialKv.getTitle(),
                officialKv.getSubtitle(), officialKv.getOpenType(), officialKv.getStatus(), officialKv.getMenuId(),
                officialKv.getCreator(), officialKv.getLastModifier(), officialKv.getBeginDate(), officialKv.getEndDate(),
                pageIndex, pageLimit);
        List<OfficialKv> officialKvList = officialKvMapper.searchOfficialKvs(officialKv.getId(), officialKv.getTitle(),
                officialKv.getSubtitle(), officialKv.getOpenType(), officialKv.getStatus(), officialKv.getMenuId(),
                officialKv.getCreator(), officialKv.getLastModifier(), officialKv.getBeginDate(), officialKv.getEndDate(),
                null, null);
        Integer dataCount = officialKvList.size();
        Integer pageCount = dataCount / pageLimit + ((dataCount % pageLimit == 0) ? 0 : 1);
        String pageTitle = getParentTitle(officialKv.getMenuId());

        Map map = new HashMap();
        map.put("pageIndex", pageIndex);
        map.put("pageLimit", pageLimit);
        map.put("pageCount", pageCount);
        map.put("pageTitle", pageTitle);
        map.put("dataCount", dataCount);

        if (dataCount <= 0) {
            map.put("data", new ArrayList());
            return map;
        }
        map.put("data", officialKvs);
        return map;
    }

    @Override
    public int insertOfficialKv(OfficialKv officialKv) throws Exception {
        int result = 0;
        if (null != officialKv.getMenuId()) {
            Menu menu = menuMapper.getMenuByMenuId(officialKv.getMenuId());
            if (null != menu) {
                officialKv.setId(CommonUtil.getUUID());
                officialKv.setCreateTime(DateUtil.parseDateToStr(new Date(), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS));
                result = officialKvMapper.insertOfficialKv(officialKv);
            }
        }
        return result;
    }

    @Override
    public int deleteOfficialKv(String id) {
        return officialKvMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateOfficialKv(OfficialKv officialKv) {
        officialKv.setLastModifyTime(DateUtil.parseDateToStr(new Date(), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS));
        return officialKvMapper.updateByPrimaryKey(officialKv);
    }

    @Override
    public OfficialKv getOfficialKvById(String id) {
        return officialKvMapper.getOfficialKvById(id);
    }

    private String getParentTitle(Long menuId) {
        //和数据库交互,得到当前节点记录
        Menu menu = menuMapper.getMenuByMenuId(menuId);
        if (menu != null) {
            String title = menu.getTitle() + " -> ";
            //参数patrolConfigEntity.getConfigParentId()表示当前节点的父节点ID
            String resultTitle = getParentTitle(menu.getpId());
            return resultTitle + title;
        } else {
            return "";
        }
    }

}
