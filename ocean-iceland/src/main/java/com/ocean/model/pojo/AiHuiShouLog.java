package com.ocean.model.pojo;


/**
 * Author: Qigaowei
 * Date: 2018/7/10 14:53
 * Description: 爱回收接口日志类
 */
public class AiHuiShouLog  {

    private String id;
    private String orderNo;
    private String data;
    private String createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
