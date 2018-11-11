package com.ocean.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ocean.config.RedisService;
import com.ocean.model.pojo.RushCode;
import com.ocean.service.MsService;
import com.ocean.service.RushPurchaseService;
import com.ocean.util.DateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Qigaowei
 * Date: 2018/8/14 11:07
 * Description: 预约码Controller类
 */
@RestController
@RequestMapping("/rush")
public class RushPurchaseController {

    private static final Log log = LogFactory.getLog(RushPurchaseController.class);

    @Autowired
    MsService msService;
    @Autowired
    RedisService redisService;
    @Autowired
    RushPurchaseService rushPurchaseService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 生成预约码
     *
     * @param jsonData
     * @return
     */
    @RequestMapping(value = "/createRushPurchase", method = RequestMethod.POST)
    public Map createRushPurchase(@RequestBody String jsonData) {

        Map map = new HashMap();
        System.out.println("jsonData=" + jsonData);
        try {

            JSONObject object = JSON.parseObject(jsonData);
            int quantity = object.getInteger("quantity");

            RushCode rushCode = new RushCode();
            rushCode.setConfigId(object.getInteger("configId"));
            rushCode.setMobile(object.getString("mobile"));
            rushCode.setStatus(object.getString("status"));

//            map.put("status", 208);
//            map.put("message", "活动已过期!");
//            map.put("data", new ArrayList<>());
            //map = rushPurchaseService.createRushCode(rushCode, quantity);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("系统异常", e);
            map.put("message", "系统异常!");
            map.put("status", 500);
            map.put("data", new ArrayList<>());
        }
        return map;
    }

    /**
     * 使用预约码
     *
     * @param jsonData
     * @return
     */
    @RequestMapping(value = "/applyRushCode", method = RequestMethod.POST)
    public Map applyRushCode(@RequestBody String jsonData) {
        Map map = new HashMap();
        System.out.println("jsonData=" + jsonData);
        try {
            map = rushPurchaseService.applyRushCode(jsonData);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("系统异常", e);
            map.put("status", 500);
            map.put("message", "系统异常!");
        }
        return map;
    }

    /**
     * 验证预约码
     *
     * @param jsonData
     * @return
     */
    @RequestMapping(value = "/verifyRushCode", method = RequestMethod.POST)
    public Map verifyRushCode(@RequestBody String jsonData) {
        Map map = new HashMap();
        System.out.println("jsonData=" + jsonData);
        try {
            map = rushPurchaseService.verifyRushCode(jsonData);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("系统异常", e);
            map.put("status", 500);
            map.put("message", "系统异常!");
        }
        return map;
    }

    /**
     * 线下预约码审核,发送提货码
     *
     * @param rushCode
     * @return
     */
    @RequestMapping(value = "/offlineApplyCode", method = RequestMethod.POST)
    public Map offlineApplyCode(@RequestBody String rushCode) {
        Map map = new HashMap();
        try {
            JSONObject object = JSON.parseObject(rushCode);
            System.out.println("rushCode===" + object.getString("rushCode"));
            map = rushPurchaseService.offlineApplyCode(object.getString("rushCode"),
                    object.getString("mobile"));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("系统异常", e);
            map.put("status", 500);
            map.put("message", "系统异常!");
        }
        return map;
    }

    /**
     * 核销提货码
     *
     * @param ladingCode
     * @return
     */
    @RequestMapping(value = "/ladingApplyCode", method = RequestMethod.POST)
    public Map ladingApplyCode(@RequestBody String ladingCode) {
        Map map = new HashMap();
        try {
            JSONObject object = JSON.parseObject(ladingCode);
            System.out.println("ladingCode===" + object.getString("ladingCode"));
            map = rushPurchaseService.ladingApplyCode(object.getString("ladingCode"));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("系统异常", e);
            map.put("status", 500);
            map.put("message", "系统异常!");
        }
        return map;
    }

    @RequestMapping(value = "/send/{configId}/message", method = RequestMethod.GET)
    public Map sendRushCodeMessage(@PathVariable Integer configId,
                                   @RequestParam(value = "message", required = false) String message) {
        Map map = new HashMap();
        try {
            map = rushPurchaseService.sendRushCodeMessage(configId, message);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 500);
            map.put("count", 0);
        }
        return map;
    }


    @RequestMapping(value = "/send/config/message", method = RequestMethod.GET)
    public Map sendConfigMessage(
            @RequestParam(value = "rushCode", required = false) String rushCode,
            @RequestParam(value = "mobile", required = false) String mobile,
            @RequestParam(value = "configId", required = false) Integer configId,
            @RequestParam(value = "message", required = false) String message) {

        Map map = new HashMap();
        try {
            map = rushPurchaseService.sendConfigMessage(rushCode, mobile, configId, message);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 500);
            map.put("count", 0);
        }
        return map;
    }

    /**
     * 生成券码
     *
     * @param jsonData
     * @return
     */
    @RequestMapping(value = "/create/code", method = RequestMethod.POST)
    public Map createCode(@RequestBody String jsonData) {
        Map map = new HashMap();
        try {
            JSONObject object = JSON.parseObject(jsonData);
            int quantity = object.getInteger("quantity");
            String address = object.getString("address");
            Integer configId = object.getInteger("configId");

            String seckillDate = DateUtil.parseDateToStr(new Date(), DateUtil.DATE_FORMAT_YYYY_MM_DD);
            StringBuilder key = new StringBuilder();
            key.append("seckill-date").append(":").append(seckillDate).append("_")
                    .append("config-id").append(":").append(configId);
            boolean flag = msService.seckill(redisTemplate, key.toString());
            if (flag) {
                RushCode rushCode = new RushCode();
                rushCode.setConfigId(configId);
                rushCode.setMobile(object.getString("mobile"));
                rushCode.setStatus(object.getString("status"));
                map = rushPurchaseService.createCode(rushCode, quantity, address);
            } else {
                map.put("status", 204);
                map.put("message", "秒杀结束!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("系统异常", e);
            map.put("status", 500);
            map.put("message", "系统异常!");
            map.put("data", new ArrayList<>());
        }

        return map;
    }

    /**
     * 验证券码
     *
     * @param jsonStr
     * @return
     */
    @RequestMapping(value = "/verify/category/code", method = RequestMethod.POST)
    public Map verifyCode(@RequestBody String jsonStr) {
        log.info("验证券码参数: " + jsonStr);
        Map map  = new HashMap();
        try {
            map = rushPurchaseService.verifyCodeForCategory(jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("系统异常", e);
            map.put("status", 500);
            map.put("message", "系统异常!");
        }
        return map;
    }

    /**
     * 使用券码
     *
     * @param jsonStr
     * @return
     */
    @RequestMapping(value = "/apply/category/code", method = RequestMethod.POST)
    public Map applyCode(@RequestBody String jsonStr) {
        log.info("使用券码参数: " + jsonStr);
        Map map  = new HashMap();
        try {
            map = rushPurchaseService.applyCode(jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("系统异常", e);
            map.put("message", "系统异常!");
            map.put("status", 500);
        }
        return map;
    }

    @RequestMapping(value = "/set/seckill/count", method = RequestMethod.POST)
    public Map setSpikeCount(@RequestBody String jsonStr) {
        log.info("设置秒杀物品数量: " + jsonStr);
        Map spikeMap  = new HashMap();
        try {
            JSONObject object = JSON.parseObject(jsonStr);
            int configId = object.getInteger("configId");
            int quantity = object.getInteger("quantity");
            String seckillDate = object.getString("seckillDate");
            long expireTime = object.getLong("expireTime");

            StringBuilder key = new StringBuilder();
            key.append("seckill-date").append(":").append(seckillDate).append("_")
                    .append("config-id").append(":").append(configId);
            System.out.println("key="+key.toString()+", quantity=" + quantity+", expireTime=" + expireTime);
            boolean flag = redisService.set(key.toString(), quantity, expireTime);
            if (flag) {
                spikeMap.put("status", 200);
                spikeMap.put("message", "设置秒杀数量成功!");
            } else {
                spikeMap.put("status", 201);
                spikeMap.put("message", "设置秒杀数量失败!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("系统异常: {}", e);
            spikeMap.put("message", "系统异常!");
            spikeMap.put("status", 500);

        }
        return spikeMap;
    }

}
