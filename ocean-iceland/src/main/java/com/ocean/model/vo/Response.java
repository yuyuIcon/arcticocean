package com.ocean.model.vo;

import java.util.List;

/**
 * Author: Qigaowei
 * Date: 2018/7/11 15:09
 * Description: 查询返回列表类
 */
public class Response {

    private Integer pageIndex;
    private Integer pageRow;
    private Integer pageCount;
    private Integer dataCount;
    private List data;

    public Response() {
    }

    public Response(Integer pageIndex, Integer pageRow, Integer pageCount, Integer dataCount, List data) {
        this.pageIndex = pageIndex;
        this.pageRow = pageRow;
        this.pageCount = pageCount;
        this.dataCount = dataCount;
        this.data = data;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageRow() {
        return pageRow;
    }

    public void setPageRow(Integer pageRow) {
        this.pageRow = pageRow;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getDataCount() {
        return dataCount;
    }

    public void setDataCount(Integer dataCount) {
        this.dataCount = dataCount;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }
}
