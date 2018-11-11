package com.ocean.service.impl;

import com.ocean.config.RedisLock;
import com.ocean.service.MsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Author: Qigaowei
 * Date: 2018/11/2 14:07
 * Description:
 */
@Service("msService")
public class MsServiceImpl implements MsService {

    private static Logger logger = LoggerFactory.getLogger(RedisLock.class);

    /***
     * 抢购代码
     * @param redisTemplate
     * @param key pronum 首先用客户端设置数量
     * @return
     */
    @Override
    public boolean seckill(RedisTemplate<String, Object> redisTemplate, String key) {
        RedisLock lock = new RedisLock(redisTemplate, key, 10000, 20000);
        boolean flag = false;
        try {
            if (lock.lock()) {

                // 需要加锁的代码
                String pronum = lock.get(key);
                System.out.println("pronum=======" + pronum);
                if (null == pronum || "".equals(pronum)) {
                    return flag;
                }
                //修改库存
                if (Integer.parseInt(pronum) - 1 >= 0) {

                    lock.set(key, String.valueOf(Integer.parseInt(pronum) - 1));
                    System.out.println("库存数量:" + pronum + "     成功!!!" + Thread.currentThread().getName());
                    logger.info("stock total : " + pronum + "     success !!!" + Thread.currentThread().getName());
                    flag = true;
                } else {

                    System.out.println("库存数量:" + pronum + "     失败!!!" + Thread.currentThread().getName());
                    logger.info("stock total : " + pronum + "     failed !!!" + Thread.currentThread().getName());
                    flag = false;
                }
                return flag;
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.error("seckill error : {}", e);
        } finally {
            // 为了让分布式锁的算法更稳键些，持有锁的客户端在解锁之前应该再检查一次自己的锁是否已经超时，再去做DEL操作，因为可能客户端因为某个耗时的操作而挂起，
            // 操作完的时候锁因为超时已经被别人获得，这时就不必解锁了。 ————这里没有做
            lock.unlock();
        }
        return flag;
    }

}
