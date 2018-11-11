package com.ocean.model.pojo;

import java.math.BigDecimal;

/**
 * Author: Qigaowei
 * Date: 2018/7/10 14:53
 * Description: 爱回收代金券类
 */
public class AiHuiShouVoucher {

    private String voucherId;
    private String mobile;
    private BigDecimal finalAmount;
    private String orderNo;
    private String userId;
    private String requestId;
    private String status;
    private String createTime;
    private String useTime;
    private String modifier;
    private String updatedAt;
    private String customerId;
    private String niuOrderNo;

    public String getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public BigDecimal getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(BigDecimal finalAmount) {
        this.finalAmount = finalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getNiuOrderNo() {
        return niuOrderNo;
    }

    public void setNiuOrderNo(String niuOrderNo) {
        this.niuOrderNo = niuOrderNo;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
