package com.ocean.dao.mapper;

import com.ocean.model.pojo.RushConfig;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: Qigaowei
 * Date: 2018/8/13 16:40
 * Description:
 */
@Repository
public interface RushConfigMapper {

    RushConfig getRushConfigById(int rushConfigId);

    int updateRushConfig(RushConfig rushConfig);

    int insertRushConfig(RushConfig rushConfig);

    RushConfig getRushConfigByConfigCode(String configCode);

    List<RushConfig> searchRushConfigs(@Param("id") String id, @Param("activityName") String activityName, @Param("limitSku") String limitSku,
                                     @Param("limitDayQuantity") Integer limitDayQuantity, @Param("limitQuantity") Integer limitQuantity,
                                     @Param("expireDay") Integer expireDay, @Param("status") String status, @Param("creator") String creator,
                                     @Param("beginCreateTime") String beginCreateTime, @Param("endCreateTime") String endCreateTime,
                                     @Param("userId") String userId, @Param("pageIndex") Integer pageIndex, @Param("pageRow") Integer pageRow);

}
