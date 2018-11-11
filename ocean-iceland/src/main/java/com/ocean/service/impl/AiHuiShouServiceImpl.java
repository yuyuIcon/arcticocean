package com.ocean.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ocean.dao.mapper.AiHuiShouLogMapper;
import com.ocean.dao.mapper.AiHuiShouOrderMapper;
import com.ocean.dao.mapper.AiHuiShouSmsMapper;
import com.ocean.dao.mapper.AiHuiShouVoucherMapper;
import com.ocean.model.pojo.AiHuiShouLog;
import com.ocean.model.pojo.AiHuiShouOrder;
import com.ocean.model.pojo.AiHuiShouSms;
import com.ocean.model.pojo.AiHuiShouVoucher;
import com.ocean.model.vo.Response;
import com.ocean.service.AiHuiShouService;
import com.ocean.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * Author: Qigaowei
 * Date: 2018/7/11 15:20
 * Description: 爱回收业务接口实现类
 */
@Service("aiHuiShouService")
public class AiHuiShouServiceImpl implements AiHuiShouService {

    private static Log log = LogFactory.getLog(AiHuiShouServiceImpl.class);


    @Value("${accountUrl}")
    private String ACCOUNT_VERIFY_URL;
    @Autowired
    private AiHuiShouSmsMapper aiHuiShouSmsMapper;
    @Autowired
    private AiHuiShouLogMapper aiHuiShouLogMapper;
    @Autowired
    private AiHuiShouOrderMapper aiHuiShouOrderMapper;
    @Autowired
    private AiHuiShouVoucherMapper aiHuiShouVoucherMapper;


    @Override
    public AiHuiShouOrder getAiHuiShouOrder(String orderNo) {
        return aiHuiShouOrderMapper.selectOrderByOrderNo(orderNo);
    }

    @Override
    @Transactional
    public Map createAHSVoucher(AiHuiShouVoucher aiHuiShouVoucher) throws Exception {

        Map map = new HashMap();
        int countOrder = aiHuiShouVoucherMapper.countByOrderNo(aiHuiShouVoucher.getOrderNo());
        if (countOrder <= 0) { // 判断数据库是否包含推送的订单号

            map = createVoucher(aiHuiShouVoucher);
        } else {
            aiHuiShouVoucherMapper.updateBySelectOrderNo(aiHuiShouVoucher);
            map.put("status", 200);
            map.put("message", "代金券更新成功!");
        }
        return map;
    }

    @Override
    public AiHuiShouVoucher getAiHuiShouVoucher(String voucherId) {
        return aiHuiShouVoucherMapper.getVoucherByPrimaryKey(voucherId);
    }

    @Override
    public Map clipCoupons(AiHuiShouVoucher voucher) throws Exception {

        AiHuiShouVoucher aiHuiShouVoucher =
                aiHuiShouVoucherMapper.getVoucherByPrimaryKey(voucher.getVoucherId());
        Map map = new HashMap();
        if (aiHuiShouVoucher != null) {
            switch (aiHuiShouVoucher.getStatus()) {
                case "0":// 初始状态,未使用
                    String now = DateUtil.parseDateToStr(new Date(), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
                    voucher.setStatus("1");
                    voucher.setUpdatedAt(now);
                    voucher.setUseTime(now);
                    log.info("使用代金券: " + voucher.getVoucherId());
                    int count = aiHuiShouVoucherMapper.updateBySelectPrimary(voucher);
                    if (count == 1) {
                        AiHuiShouVoucher aiVoucher =
                                aiHuiShouVoucherMapper.getVoucherByPrimaryKey(voucher.getVoucherId());
                        map.put("status", 200);
                        map.put("message", "代金券使用成功!");
                        map.put("data", aiVoucher);
                    } else {
                        map.put("status", 204);
                        map.put("message", "代金券使用失败!");
                        map.put("data", aiHuiShouVoucher);
                    }
                    break;
                case "1":// 已使用状态
                    map.put("status", 201);
                    map.put("message", "此代金券已使用!");
                    map.put("data", aiHuiShouVoucher);
                    break;
                case "2":// 已作废状态
                    map.put("status", 202);
                    map.put("message", "此代金券已作废!");
                    map.put("data", aiHuiShouVoucher);
                    break;
                default:// 没有该数据
                    map.put("status", 203);
                    map.put("message", "此代金券不存在!");
                    map.put("data", aiHuiShouVoucher);
                    break;
            }
        } else {
            map.put("status", 203);
            map.put("message", "此代金券不存在!");
            map.put("data", aiHuiShouVoucher);
        }
        return map;
    }

    @Override
    public Map insertAHSOrder(AiHuiShouOrder aiHuiShouOrder) {
        Map map = new HashMap();
        int countOrder = aiHuiShouOrderMapper.countOrder(aiHuiShouOrder.getOrderNo());
        String nowTime = DateUtil.parseDateToStr(new Date(), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);//CommonUtil.getStringDate(new Date());
        if (countOrder <= 0) { // 判断数据库是否包含推送的订单号
            aiHuiShouOrder.setCreateTime(nowTime);
            int count = aiHuiShouOrderMapper.insertAHSOrder(aiHuiShouOrder);

            if (count > 0) {
                map.put("status", 200);
                map.put("message", "订单保存成功!");
            } else {
                map.put("status", 503);
                map.put("message", "订单保存失败!");
            }
        } else {
            aiHuiShouOrder.setUpdateTime(nowTime);
            int count = aiHuiShouOrderMapper.updateAHSOrder(aiHuiShouOrder);

            if (count > 0) {
                map.put("status", 200);
                map.put("message", "订单更新成功!");
            } else {
                map.put("status", 504);
                map.put("message", "订单更新失败!");
            }
        }
        return map;
    }

    @Override
    public int updateVoucher(AiHuiShouVoucher voucher) {
        return aiHuiShouVoucherMapper.updateBySelectPrimary(voucher);
    }

    @Override
    public int insertAHSLog(String orderNo, String jsonData) {

        AiHuiShouLog aiHuiShouLog = new AiHuiShouLog();
        aiHuiShouLog.setId(CommonUtil.getUUID());
        aiHuiShouLog.setOrderNo(orderNo);
        aiHuiShouLog.setData(jsonData);
        aiHuiShouLog.setCreatedAt(DateUtil.parseDateToStr(new Date(), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS));
        return aiHuiShouLogMapper.insertAHSLog(aiHuiShouLog);
    }

    @Override
    public Map getVoucherAmount(String voucher) {
        AiHuiShouVoucher aiHuiShouVoucher =
                aiHuiShouVoucherMapper.getVoucherByPrimaryKey(voucher);
        Map map = new HashMap();
        if (aiHuiShouVoucher != null) {
            switch (aiHuiShouVoucher.getStatus()) {
                case "0":// 初始状态,未使用
                    map.put("status", 200);
                    map.put("message", "获取成功!");
                    map.put("data", aiHuiShouVoucher);
                    break;
                case "1":// 已使用状态
                    map.put("status", 201);
                    map.put("message", "此代金券已使用!");
                    map.put("data", null);
                    break;
                case "2":// 已作废状态
                    map.put("status", 202);
                    map.put("message", "此代金券已作废!");
                    map.put("data", null);
                    break;
                default:// 没有该数据
                    map.put("status", 203);
                    map.put("message", "此代金券不存在!");
                    map.put("data", null);
                    break;
            }
        } else {
            map.put("status", 203);
            map.put("message", "此代金券不存在!");
            map.put("data", null);
        }
        return map;
    }

    @Override
    public int applyCoupon(String order) {
        // 暂无逻辑
        return 0;
    }

    @Override
    public Map sendMessage(String orderNo) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        AiHuiShouVoucher voucher = aiHuiShouVoucherMapper.getVoucherByOrderNo(orderNo);
        if (voucher == null) {
            map.put("status", 201);
            map.put("message", "没有此订单信息!");
            return map;
        }
        //处理短信业务,发送爱回收代金券
        StringBuilder message = new StringBuilder();
        message.append("您通过“闲置物品换小牛”活动获得").append(voucher.getFinalAmount())
                .append("元优惠券，优惠码：").append(voucher.getVoucherId())
                .append("，请您在180天内到小牛电动线下授权店选购您喜欢的商品。详情咨询：")
                .append("https://t.cn/RTo8gpH");
        boolean flag = HttpUtils.sendMessage(voucher.getMobile(), message.toString());
        if (flag) {// 判断代金券短信是否发送成功

            AiHuiShouSms aiHuiShouSms = new AiHuiShouSms();
            aiHuiShouSms.setOrderNo(voucher.getOrderNo());
            aiHuiShouSms.setVoucher(voucher.getVoucherId());
            aiHuiShouSms.setMobile(voucher.getMobile());
            aiHuiShouSms.setMessage(message.toString());

            aiHuiShouSms.setSendTime(DateUtil.parseDateToStr(new Date(), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS));
            aiHuiShouSmsMapper.insertAHSSms(aiHuiShouSms);

            map.put("status", 200);
            map.put("message", "代金券短信发送成功!");
        } else {

            map.put("status", 202);
            map.put("message", "代金券短信发送失败!");
        }
        return map;
    }

    @Override
    public Response searchOrders(String orderNo, String mobile, String productName, String brandName, String categoryName,
                                 String skuName, BigDecimal finalAmount, String status, String beginCreatedAt, String endCreatedAt,
                                 String beginCompletedAt, String endCompletedAt, String beginCreateTime, String endCreateTime,
                                 Integer pageIndex, Integer pageRow) {

        List<AiHuiShouOrder> orders = aiHuiShouOrderMapper.searchOrders(orderNo, mobile, productName, brandName, categoryName,
                skuName, finalAmount, status, beginCreatedAt, endCreatedAt, beginCompletedAt, endCompletedAt, beginCreateTime, endCreateTime,
                pageIndex, pageRow);
        List<AiHuiShouOrder> allOrders = aiHuiShouOrderMapper.searchOrders(orderNo, mobile, productName, brandName, categoryName,
                skuName, finalAmount, status, beginCreatedAt, endCreatedAt, beginCompletedAt, endCompletedAt, beginCreateTime, endCreateTime,
                null, null);
        Response response = getResponse(orders, allOrders.size(), pageIndex, pageRow);

        return response;
    }

    @Override
    public Response searchVouchers(String voucherId, String orderNo, BigDecimal amount, String status, String userId, String modifier,
                                   String beginCreateTime, String endCreateTime, String beginUseTime, String endUseTime, String beginUpdatedAt,
                                   String endUpdatedAt, String customerId, String niuOrderNo, Integer pageIndex, Integer pageRow) {
        List<AiHuiShouVoucher> vouchers = aiHuiShouVoucherMapper.searchVouchers(voucherId, orderNo, amount, status, userId, modifier,
                beginCreateTime, endCreateTime, beginUseTime, endUseTime, beginUpdatedAt, endUpdatedAt, customerId, niuOrderNo, pageIndex, pageRow);
        List<AiHuiShouVoucher> allVouchers = aiHuiShouVoucherMapper.searchVouchers(voucherId, orderNo, amount, status, userId, modifier,
                beginCreateTime, endCreateTime, beginUseTime, endUseTime, beginUpdatedAt, endUpdatedAt, customerId, niuOrderNo, null, null);

        Response response = getResponse(vouchers, allVouchers.size(), pageIndex, pageRow);

        return response;
    }


    /**
     * 生成爱回收代金券,验证重复性
     *
     * @return voucher
     */
    private String getCoupons() {
        int i = 0;
        String voucher = RandomUtil.createCoupons(Constants.AHS_VOUCHER_LENGTH, Constants.AHS_VOUCHER_HEAD, "");
        System.out.println(i++ + ":" + voucher);
        int countVoucher = aiHuiShouVoucherMapper.countByVoucherId(voucher);
        if (countVoucher > 0) {
            voucher = getCoupons();
        }
        System.out.println("voucher=" + voucher);
        return voucher;
    }


    /**
     * 生成爱回收代金券,并发送短信
     *
     * @param aiHuiShouVoucher
     * @return
     */
    private Map createVoucher(AiHuiShouVoucher aiHuiShouVoucher) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        //AiHuiShouVoucher voucher = new AiHuiShouVoucher();
        String nowTime = DateUtil.parseDateToStr(new Date(), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
        String voucherId = getCoupons();
        aiHuiShouVoucher.setVoucherId(voucherId);

        aiHuiShouVoucher.setStatus(Constants.AHS_VOUCHER_INITIAL_STATUS);
        aiHuiShouVoucher.setCreateTime(nowTime);
        StringBuilder accountUrl = new StringBuilder();
        accountUrl.append(ACCOUNT_VERIFY_URL).append("?")
                .append("account").append("=").append(aiHuiShouVoucher.getMobile()).append("&")
                .append("key").append("=").append(Constants.ACCOUNT_VERIFY_KEY);

        System.out.println("Constants.ACCOUNT_VERIFY_URL=" + ACCOUNT_VERIFY_URL);
        // 获取手机号注册小牛官网信息
        String result = HttpUtils.get(accountUrl.toString());
        log.info("获取Account信息=" + result);
        if (!result.isEmpty()) {

            JSONObject object = JSON.parseObject(result);
            if ("200".equals(object.getString("status"))) {

                JSONArray array = object.getJSONArray("users");
                JSONObject userObject = array.getJSONObject(0);
                aiHuiShouVoucher.setCustomerId(userObject.getString("_id"));
            }
        }

        int countVoucher = aiHuiShouVoucherMapper.insertAHSVoucher(aiHuiShouVoucher);
        if (countVoucher > 0) {
            //处理短信业务,发送爱回收代金券
            StringBuilder message = new StringBuilder();
            message.append("您通过“闲置物品换小牛”活动获得").append(aiHuiShouVoucher.getFinalAmount())
                    .append("元优惠券，优惠码：").append(aiHuiShouVoucher.getVoucherId())
                    .append("，请您在180天内到小牛电动线下授权店选购您喜欢的商品。详情咨询：")
                    .append("https://t.cn/RTo8gpH");
            boolean flag = HttpUtils.sendMessage(aiHuiShouVoucher.getMobile(), message.toString());
            if (flag) {// 判断代金券短信是否发送成功

                AiHuiShouSms aiHuiShouSms = new AiHuiShouSms();
                aiHuiShouSms.setOrderNo(aiHuiShouVoucher.getOrderNo());
                aiHuiShouSms.setVoucher(voucherId);
                aiHuiShouSms.setMobile(aiHuiShouVoucher.getMobile());
                aiHuiShouSms.setMessage(message.toString());

                aiHuiShouSms.setSendTime(nowTime);
                aiHuiShouSmsMapper.insertAHSSms(aiHuiShouSms);
            }

            map.put("status", 200);
            map.put("message", "代金券发送成功!");
        } else {

            map.put("status", 503);
            map.put("message", "代金券发送失败!");
        }
        return map;
    }

    /**
     * Search接口返回数据
     *
     * @param data
     * @param dataCount
     * @param pageIndex
     * @param pageRow
     * @return
     */
    public Response getResponse(List<?> data, Integer dataCount, Integer pageIndex, Integer pageRow) {
        Response response = new Response();
        Integer pageCount = dataCount / pageRow + ((dataCount % pageRow == 0) ? 0 : 1);

        response.setDataCount(dataCount);
        response.setPageCount(pageCount);
        response.setPageIndex(pageIndex);
        response.setPageRow(pageRow);
        response.setData(data);

        if (dataCount <= 0) {
            response.setData(new ArrayList());
        }
        return response;
    }
}
