package com.ocean.service;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * Author: Qigaowei
 * Date: 2018/11/2 11:39
 * Description:
 */
public interface MsService {

    /** 秒杀活动 */
    boolean seckill(RedisTemplate<String, Object> redisTemplate, String key);

}