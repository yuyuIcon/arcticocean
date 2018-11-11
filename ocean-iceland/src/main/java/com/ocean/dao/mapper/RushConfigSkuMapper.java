package com.ocean.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Author: Qigaowei
 * Date: 2018/9/11 20:05
 * Description:
 */
@Repository
public interface RushConfigSkuMapper {

    Integer getConfigSkuBySkuIds(@Param("skuIds") String skuIds,@Param("status") String status);
}
