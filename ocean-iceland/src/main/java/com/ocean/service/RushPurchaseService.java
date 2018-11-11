package com.ocean.service;

import com.ocean.model.pojo.RushCode;
import com.ocean.model.pojo.RushConfig;
import com.ocean.model.vo.Response;

import java.util.Map;

/**
 * Author: Qigaowei
 * Date: 2018/8/14 11:28
 * Description: 抢购码接口类
 */
public interface RushPurchaseService {

    Map createCode(RushCode rushCode, int quantity, String address) throws Exception;

    int insertRushConfig(RushConfig rushConfig);

    //Map createRushCode(RushCode rushCode, int quantity) throws Exception;

    Map verifyRushCode(String jsonStr) throws Exception;

    Map applyRushCode(String jsonStr) throws Exception;

    Map applyCode(String jsonStr) throws Exception;

    Map offlineApplyCode(String rushCode, String mobile) throws Exception;

    Map sendRushCodeMessage(Integer configId, String message) throws Exception;

    Map sendConfigMessage(String rushCodeId, String mobile, Integer configId, String message) throws Exception;

    Map ladingApplyCode(String ladingCode) throws Exception;

    Map verifyCodeForCategory(String jsonStr) throws Exception;

    Response searchRushCodes(String rushCodeId, String mobile, String status, String configId, String beginExpireTime,
                             String endExpireTime, String beginCreateTime, String endCreateTime, String userId,
                             Integer pageIndex, Integer pageRow);

    Response searchRushConfig(String id, String activityName, String limitSku, Integer limitDayQuantity, Integer limitQuantity,
                              Integer expireDay, String status, String creator, String beginCreateTime, String endCreateTime,
                              Integer pageIndex, Integer pageRow);




}
