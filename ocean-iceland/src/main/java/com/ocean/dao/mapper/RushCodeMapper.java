package com.ocean.dao.mapper;

import com.ocean.model.pojo.RushCode;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: Qigaowei
 * Date: 2018/8/13 16:38
 * Description:
 */
@Repository
public interface RushCodeMapper {

    RushCode getRushCodeById(String rushCodeId);

    List<RushCode> getRushCodeByConfigId(Integer configId);

    int updateRushCode(RushCode rushCode);

    int updateRushStatus(RushCode rushCode);

    int insertRushCode(RushCode rushCode);

    int insertRushCodesByBatch(@Param("rushCodes") List<RushCode> rushCodes);

    RushCode getRushCodeByMobileAndConfigId(@Param("mobile") String mobile, @Param("configId") Integer configId);

    int getRushCounts(@Param("configId") Integer configId, @Param("mobile") String mobile,
                      @Param("beginDate") String beginDate, @Param("endDate") String endDate);

    List<RushCode> searchRushCodes(@Param("rushCodeId") String rushCodeId, @Param("mobile") String mobile,
                                   @Param("status") String status, @Param("configId") String configId,
                                   @Param("beginExpireTime") String beginExpireTime, @Param("endExpireTime") String endExpireTime,
                                   @Param("beginCreateTime") String beginCreateTime, @Param("endCreateTime") String endCreateTime,
                                   @Param("pageIndex") Integer pageIndex, @Param("pageRow") Integer pageRow);


}
