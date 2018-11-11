package com.ocean.service;

import com.ocean.model.pojo.AiHuiShouOrder;
import com.ocean.model.pojo.AiHuiShouVoucher;
import com.ocean.model.vo.Response;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Author: Qigaowei
 * Date: 2018/7/11 15:19
 * Description: 爱回收业务接口类
 */
public interface AiHuiShouService {

    AiHuiShouOrder getAiHuiShouOrder(String orderNo);

    Map createAHSVoucher(AiHuiShouVoucher aiHuiShouVoucher) throws Exception;

    AiHuiShouVoucher getAiHuiShouVoucher(String voucherId);

    Map clipCoupons(AiHuiShouVoucher voucher) throws Exception;

    Map insertAHSOrder(AiHuiShouOrder aiHuiShouOrder);

    int updateVoucher(AiHuiShouVoucher voucher);

    int insertAHSLog(String orderNo, String jsonData);

    Map getVoucherAmount(String voucher);

    int applyCoupon(String order);

    Map sendMessage(String orderNo) throws Exception;

    Response searchOrders(String orderNo, String mobile, String productName, String brandName, String categoryName,
                          String skuName, BigDecimal finalAmount, String status, String beginCreatedAt, String endCreatedAt,
                          String beginCompletedAt, String endCompletedAt, String beginCreateTime, String endCreateTime,
                          Integer pageIndex, Integer pageRow);

    Response searchVouchers(String voucherId, String orderNo, BigDecimal amount, String status, String userId, String modifier,
                            String beginCreateTime, String endCreateTime, String beginUseTime, String endUseTime, String beginUpdatedAt,
                            String endUpdatedAt, String customerId, String niuOrderNo, Integer pageIndex, Integer pageRow);
}
