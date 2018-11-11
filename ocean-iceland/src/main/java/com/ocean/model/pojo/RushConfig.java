package com.ocean.model.pojo;

/**
 * Author: Qigaowei
 * Date: 2018/8/13 16:25
 * Description:
 */
public class RushConfig {

    private Integer id;
    private String activityName;
    private String limitSku;
    private Integer limitDayQuantity;
    private Integer limitQuantity;
    private Integer expireDay;
    private String expireTime;
    private String activityStartTime;
    private String activityEndTime;
    private String configCode;
    private long categoryId;
    private String status;
    private String creator;
    private String createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getLimitSku() {
        return limitSku;
    }

    public void setLimitSku(String limitSku) {
        this.limitSku = limitSku;
    }

    public Integer getLimitDayQuantity() {
        return limitDayQuantity;
    }

    public void setLimitDayQuantity(Integer limitDayQuantity) {
        this.limitDayQuantity = limitDayQuantity;
    }

    public Integer getLimitQuantity() {
        return limitQuantity;
    }

    public void setLimitQuantity(Integer limitQuantity) {
        this.limitQuantity = limitQuantity;
    }

    public Integer getExpireDay() {
        return expireDay;
    }

    public void setExpireDay(Integer expireDay) {
        this.expireDay = expireDay;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getActivityStartTime() {
        return activityStartTime;
    }

    public void setActivityStartTime(String activityStartTime) {
        this.activityStartTime = activityStartTime;
    }

    public String getActivityEndTime() {
        return activityEndTime;
    }

    public void setActivityEndTime(String activityEndTime) {
        this.activityEndTime = activityEndTime;
    }

    public String getConfigCode() {
        return configCode;
    }

    public void setConfigCode(String configCode) {
        this.configCode = configCode;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
