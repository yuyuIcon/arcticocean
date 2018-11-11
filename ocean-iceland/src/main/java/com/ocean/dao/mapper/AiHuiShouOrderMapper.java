package com.ocean.dao.mapper;

import com.ocean.model.pojo.AiHuiShouOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Author: Qigaowei
 * Date: 2018/7/11 10:00
 * Description: 订单接口类
 */
@Repository
public interface AiHuiShouOrderMapper {

    public List<AiHuiShouOrder> getAllOrders();

    public int updateAHSOrder(AiHuiShouOrder order);

    public AiHuiShouOrder selectOrderByOrderNo(String orderNo);

    public int countOrder(String orderNo);

    public List<AiHuiShouOrder> searchOrders(@Param("orderNo") String orderNo, @Param("mobile") String mobile, @Param("productName") String productName, @Param("brandName") String brandName,
                                             @Param("categoryName") String categoryName, @Param("skuName") String skuName, @Param("finalAmount") BigDecimal finalAmount, @Param("status") String status,
                                             @Param("beginCreatedAt") String beginCreatedAt, @Param("endCreatedAt") String endCreatedAt, @Param("beginCompletedAt") String beginCompletedAt,
                                             @Param("endCompletedAt") String endCompletedAt, @Param("beginCreateTime") String beginCreateTime, @Param("endCreateTime") String endCreateTime,
                                             @Param("pageIndex") Integer pageIndex, @Param("pageRow") Integer pageRow);

    public AiHuiShouOrder selectOrderByVoucherId(String voucherId);

    public int insertAHSOrder(AiHuiShouOrder order);

}
