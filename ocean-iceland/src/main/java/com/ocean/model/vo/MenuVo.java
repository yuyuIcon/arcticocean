package com.ocean.model.vo;

import com.ocean.model.pojo.OfficialKv;

import java.util.List;

/**
 * Author: Qigaowei
 * Date: 2018/1/23 PM5:22
 * Description:
 */
public class MenuVo {

    private Long menuId;
    private String title;
    private String subtitle;
    private Long pId;
    private String url;
    private List<OfficialKv> officialKvs;
    private List<MenuVo> children;

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public Long getpId() {
        return pId;
    }

    public void setpId(Long pId) {
        this.pId = pId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<OfficialKv> getOfficialKvs() {
        return officialKvs;
    }

    public void setOfficialKvs(List<OfficialKv> officialKvs) {
        this.officialKvs = officialKvs;
    }

    public List<MenuVo> getChildren() {
        return children;
    }

    public void setChildren(List<MenuVo> children) {
        this.children = children;
    }
}
