package com.ocean.model.pojo;

import java.math.BigDecimal;

/**
 * Author: Qigaowei
 * Date: 2018/7/10 14:53
 * Description: 爱回收订单类
 */
public class AiHuiShouOrder {

    private String orderNo;
    private String mobile;
    private String requestId;
    private String productName;
    private String brandName;
    private String categoryName;
    private String skuName;
    private BigDecimal inspectionAmount;
    private BigDecimal finalAmount;
    private BigDecimal submitedAmount;
    private String status;
    private String createdAt;
    private String completedAt;
    private String updatedAt;
    private int timestamp;
    private String sign;
    private String createTime;
    private String updateTime;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public BigDecimal getInspectionAmount() {
        return inspectionAmount;
    }

    public void setInspectionAmount(BigDecimal inspectionAmount) {
        this.inspectionAmount = inspectionAmount;
    }

    public BigDecimal getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(BigDecimal finalAmount) {
        this.finalAmount = finalAmount;
    }

    public BigDecimal getSubmitedAmount() {
        return submitedAmount;
    }

    public void setSubmitedAmount(BigDecimal submitedAmount) {
        this.submitedAmount = submitedAmount;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
