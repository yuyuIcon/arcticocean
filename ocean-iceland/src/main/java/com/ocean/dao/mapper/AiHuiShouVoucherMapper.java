package com.ocean.dao.mapper;

import com.ocean.model.pojo.AiHuiShouVoucher;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Author: Qigaowei
 * Date: 2018/7/11 10:01
 * Description: 爱回收代金券接口类
 */
@Repository
public interface AiHuiShouVoucherMapper {

    AiHuiShouVoucher getVoucherByPrimaryKey(String voucherId);

    AiHuiShouVoucher getVoucherByOrderNo(String orderNo);

    List<AiHuiShouVoucher> searchVouchers(@Param("voucherId") String voucherId, @Param("orderNo") String orderNo, @Param("amount") BigDecimal amount, @Param("status") String status,
                                                 @Param("userId") String userId, @Param("modifier") String modifier, @Param("beginCreateTime") String beginCreateTime,
                                                 @Param("endCreateTime") String endCreateTime, @Param("beginUseTime") String beginUseTime, @Param("endUseTime") String endUseTime,
                                                 @Param("beginUpdatedAt") String beginUpdatedAt, @Param("endUpdatedAt") String endUpdatedAt, @Param("customerId") String customerId,
                                                 @Param("niuOrderNo") String niuOrderNo, @Param("pageIndex") Integer pageIndex, @Param("pageRow") Integer pageRow);

    int updateBySelectPrimary(AiHuiShouVoucher aiHuiShouVoucher);

    int countByVoucherId(String voucherId);

    int countByOrderNo(String orderNo);

    int insertAHSVoucher(AiHuiShouVoucher aiHuiShouVoucher);

    int updateBySelectOrderNo(AiHuiShouVoucher aiHuiShouVoucher);

}
