package com.ocean.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ocean.dao.mapper.*;
import com.ocean.model.pojo.AiHuiShouSms;
import com.ocean.model.pojo.RushCategory;
import com.ocean.model.pojo.RushCode;
import com.ocean.model.pojo.RushConfig;
import com.ocean.model.vo.Response;
import com.ocean.service.RushPurchaseService;
import com.ocean.util.DateUtil;
import com.ocean.util.HttpUtils;
import com.ocean.util.RandomUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Author: Qigaowei
 * Date: 2018/8/14 11:29
 * Description: 抢购码业务类
 */
@Service("rushPurchaseService")
public class RushPurchaseServiceImpl implements RushPurchaseService {

    private static final Log log = LogFactory.getLog(RushPurchaseServiceImpl.class);

    @Autowired
    private RushCategoryMapper rushCategoryMapper;
    @Autowired
    private RushCodeMapper rushCodeMapper;
    @Autowired
    private RushConfigMapper rushConfigMapper;
    @Autowired
    private RushConfigSkuMapper rushConfigSkuMapper;
    @Autowired
    private AiHuiShouSmsMapper aiHuiShouSmsMapper;


    @Override
    public Map createCode(RushCode rushCode, int quantity, String address) throws Exception {
        Map map = new HashMap();
        Date nowDate = new Date();
        String nowTime = DateUtil.parseDateToStr(new Date(), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
        RushConfig rushConfig = rushConfigMapper.getRushConfigById(rushCode.getConfigId());
        map = createCodeVerify(rushCode, nowTime);
        if (map.isEmpty()) {
            String expireTime = rushConfig.getExpireTime();
            if (rushConfig.getExpireDay() > 0) { //0:以过期时间为准,其他:以过期天数为准

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(nowDate);
                calendar.add(Calendar.DATE, +rushConfig.getExpireDay());
                expireTime = DateUtil.parseDateToStr(calendar.getTime(), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
            }
            RushCategory rushCategory = rushCategoryMapper.getCategoryById(rushConfig.getCategoryId());
            List<RushCode> codeList = new ArrayList<>();
            List<String> codeIds = new ArrayList<>();
            for (int i = 0; i < quantity; i++) {

                String rushCodeId = getRushPurchase(16, "NR", rushConfig.getConfigCode());
                codeIds.add(rushCodeId);

                RushCode code = new RushCode();
                code.setRushCodeId(rushCodeId);
                code.setCreateTime(nowTime);
                code.setExpireTime(expireTime);
                code.setStatus(rushCode.getStatus());

                code.setCategoryCode(rushCategory.getCategoryCode());
                code.setConfigId(rushCode.getConfigId());
                code.setMobile(rushCode.getMobile());
                rushCode.setRushCodeId(rushCodeId);
                codeList.add(code);
            }
            int result = rushCodeMapper.insertRushCodesByBatch(codeList);

            if (result > 0) {

                if (rushCode.getMobile() != null && !"".equals(rushCode.getMobile())) {
                    StringBuilder message = new StringBuilder();
                    System.out.println("预约码: " + rushCode.getRushCodeId());
                    message.append("恭喜您成功预约回馈礼品1份！您的礼品领取码为：").append(rushCode.getRushCodeId())
                            .append("，您可在11月3日-11月18日期间凭借此码，到 ").append(address)
                            .append(" 领取礼品。请务必妥善保管礼品领取码，切勿对外泄露。");
                    boolean flag = HttpUtils.sendMessage(rushCode.getMobile(), message.toString());
                    if (flag) {// 判断短信是否发送成功

                        insertSms(rushCode, message.toString());
                    }
                }
                map.put("status", 200);
                map.put("message", "领取成功!");
                map.put("data", codeIds);
            } else {
                map.put("status", 206);
                map.put("message", "领取失败!");
                map.put("data", new ArrayList<>());
            }

        }
        return map;
    }

    @Override
    public int insertRushConfig(RushConfig rushConfig) {
        return rushConfigMapper.insertRushConfig(rushConfig);
    }

    /*
    @Override
    public Map createRushCode(RushCode rushCode, int quantity) throws Exception {
        Map map = new HashMap();
        Date nowDate = new Date();
        String nowTime = CommonUtil.getStringDate(nowDate);
        RushConfig rushConfig = rushConfigMapper.getRushConfigById(rushCode.getConfigId());

        map = createCodeVerify(rushCode, nowTime);
        if (map.isEmpty()) {
            String expireTime = rushConfig.getExpireTime();
            if (rushConfig.getExpireDay() > 0) { //0:以过期时间为准,其他:以过期天数为准

                Calendar cal = Calendar.getInstance();
                cal.setTime(nowDate);
                cal.add(Calendar.DATE, +rushConfig.getExpireDay());
                expireTime = CommonUtil.getStringDate(cal.getTime());
            }
            List<RushCode> codeList = new ArrayList<>();
            List<String> codeIds = new ArrayList<>();
            for (int i = 0; i < quantity; i++) {

                String rushCodeId = getRushPurchase(16, "NF", rushConfig.getConfigCode());
                codeIds.add(rushCodeId);

                RushCode code = new RushCode();
                code.setRushCodeId(rushCodeId);
                code.setCreateTime(nowTime);
                code.setExpireTime(expireTime);
                code.setStatus(rushCode.getStatus());

                code.setConfigId(rushCode.getConfigId());
                code.setMobile(rushCode.getMobile());
                rushCode.setRushCodeId(rushCodeId);
                codeList.add(code);
            }
            int result = rushCodeMapper.insertRushCodesByBatch(codeList);
            if (result > 0) {

                if (rushCode.getMobile() != null && !"".equals(rushCode.getMobile())) {
                    StringBuilder message = new StringBuilder();
                    System.out.println("预约码: " + rushCode.getRushCodeId());
                    message.append("亲爱的牛油，您已成功预约小牛迈凯伦车队合作款，预约码：").append(rushCode.getRushCodeId())
                            .append("，请您于2018年9月28日 10:00:00凭码抢购，")
                            .append("前50名付款成功的牛油将获得China GT（中国超级跑车锦标赛）门票。")
                            .append("更多详情请戳https://www.niu.com/mclaren");
                    System.out.println("预约码: " + rushCode.getMobile());
                    boolean flag = HttpUtils.sendMessage(rushCode.getMobile(), message.toString());
                    if (flag) {// 判断短信是否发送成功

                        AiHuiShouSms sms = new AiHuiShouSms();
                        sms.setVoucher(codeIds.get(0));
                        sms.setMobile(rushCode.getMobile());
                        sms.setMessage(message.toString());
                        sms.setSendTime(nowTime);

                        aiHuiShouSmsMapper.insertAHSSms(sms);
                    }
                }
                map.put("status", 200);
                map.put("message", "预约码领取成功!");
                map.put("data", codeIds);
            } else {
                map.put("status", 206);
                map.put("message", "预约码领取失败!");
                map.put("data", new ArrayList<>());
            }
        }
        return map;
    }
    */

    @Override
    public Map verifyRushCode(String jsonStr) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        String ticket = jsonObject.getString("ticket");
        map = verifyCode(jsonObject, ticket);

        if (map.isEmpty()) {

            map.put("status", 200);
            map.put("message", "预约码验证成功!");
        }
        return map;
    }

    @Override
    public Map applyRushCode(String jsonStr) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        String ticket = jsonObject.getString("ticket");
        String userId = jsonObject.getString("customerId");
        String orderNo = jsonObject.getString("orderNo");

        //map = verifyCode(jsonObject, ticket);
        if (map.isEmpty()) {

            RushCode rushCode = new RushCode();
            rushCode.setRushCodeId(ticket);
            rushCode.setUserId(userId);
            rushCode.setOrderNo(orderNo);
            rushCode.setStatus("2");

            String nowTime = DateUtil.parseDateToStr(new Date(), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
            ;
            rushCode.setUseTime(nowTime);
            rushCode.setUpdateTime(nowTime);
            int result = rushCodeMapper.updateRushStatus(rushCode);
            if (result > 0) {

                map.put("status", 200);
                map.put("message", "预约码使用成功!");
            } else {

                map.put("status", 206);
                map.put("message", "预约码使用失败!");
            }
        }
        return map;
    }

    @Override
    public Map applyCode(String jsonStr) throws Exception {

        Map map = verifyCodeForCategory(jsonStr);
        if ("200".equals(map.get("status").toString())) {

            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            String code = jsonObject.getString("rushCode");
            String orderNo = jsonObject.getString("orderNo");
            String userId = jsonObject.getString("customerId");

            RushCode rushCode = new RushCode();
            rushCode.setRushCodeId(code);
            rushCode.setUserId(userId);
            rushCode.setOrderNo(orderNo);
            rushCode.setStatus("2");

            String nowTime = DateUtil.parseDateToStr(new Date(), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
            rushCode.setUseTime(nowTime);
            rushCode.setUpdateTime(nowTime);
            rushCodeMapper.updateRushStatus(rushCode);

            map.put("message", "使用成功!");
        }
        return map;
    }

    @Override
    public Map offlineApplyCode(String rushCode, String mobile) throws Exception {
        Map map = new HashMap();
        String nowTime = DateUtil.parseDateToStr(new Date(), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
        ;

        if (rushCode == null || "".equals(rushCode)) {
            map.put("status", 201);
            map.put("message", "预约码为空!");
            return map;
        }
        RushCode code = rushCodeMapper.getRushCodeById(rushCode);
        if (code == null) {
            map.put("status", 202);
            map.put("message", "预约码不存在!");
            return map;
        }
        RushConfig config = rushConfigMapper.getRushConfigById(code.getConfigId());
        if (config == null) {
            map.put("status", 203);
            map.put("message", "预约码无效!");
            return map;
        }
        if ("2".equals(code.getStatus())) {
            map.put("status", 207);
            map.put("message", "预约码已使用!");
            return map;
        }
        if ("3".equals(code.getStatus())) {
            map.put("status", 208);
            map.put("message", "预约码已作废!");
            return map;
        }
        if (nowTime.compareTo(code.getExpireTime()) > 0) {

            map.put("status", 204);
            map.put("message", "预约码已过期!");
            return map;
        }
        if (StringUtils.isEmpty(mobile)) {

            map.put("status", 205);
            map.put("message", "请填写手机号!");
            return map;
        }

        String status = decrStocks(config.getLimitSku(), 1);
        if (status.equals("200")) {

            code.setStatus("2");
            code.setUseTime(nowTime);
            code.setUpdateTime(nowTime);
            rushCodeMapper.updateRushStatus(code);

            // 发送提货码
            RushCode ladingCode = new RushCode();
            ladingCode.setRushCodeId(RandomUtil.createCoupons(8, "NL", ""));
            ladingCode.setStatus("1");
            ladingCode.setCreateTime(nowTime);
            ladingCode.setMobile(mobile);

            rushCodeMapper.insertRushCode(ladingCode);
            StringBuilder message = new StringBuilder();
            System.out.println("提货码: " + ladingCode.getRushCodeId());
            message.append("亲爱的牛油，您已成功抢购小牛迈凯伦车队合作款，提货码：").append(ladingCode.getRushCodeId())
                    .append("，请您妥善保管避免冒领，提车时需要向门店出示此码。更多详情请戳情请戳https://www.niu.com/mclaren");

            boolean flag = HttpUtils.sendMessage(mobile, message.toString());
            if (flag) {// 判断短信是否发送成功

                insertSms(ladingCode, message.toString());
            }

            map.put("status", 200);
            map.put("message", "预约码核销成功!");
            return map;
        } else {

            map.put("status", 206);
            map.put("message", "商品库存不足!");
            return map;
        }
    }

    @Override
    public Map sendRushCodeMessage(Integer configId, String message) throws Exception {

        List<RushCode> rushCodes = rushCodeMapper.getRushCodeByConfigId(configId);
        int count = 0;
        for (RushCode rushCode : rushCodes) {
            String nowDate = DateUtil.parseDateToStr(new Date(), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
            if (!StringUtils.isEmpty(rushCode.getMobile()) && "1".equals(rushCode.getStatus())
                    && rushCode.getExpireTime().compareTo(nowDate) > 0) {

                boolean flag = HttpUtils.sendMessage(rushCode.getMobile(), message);

                if (flag) {
                    insertSms(rushCode, message);
                }
                count++;
            }
        }

        Map map = new HashMap();
        map.put("status", 200);
        map.put("count", count);
        return map;
    }

    @Override
    public Map sendConfigMessage(String rushCodeId, String mobile, Integer configId, String message) throws Exception {

        Map map = new HashMap();

        RushCode rushCode = new RushCode();
        if (StringUtils.isEmpty(rushCodeId)) {
            rushCode = rushCodeMapper.getRushCodeByMobileAndConfigId(mobile, configId);
        } else {
            rushCode = rushCodeMapper.getRushCodeById(rushCodeId);
        }

        if (rushCode != null && !StringUtils.isEmpty(mobile)) {

            StringBuilder param = new StringBuilder();
            log.info("预约码: " + rushCode.getRushCodeId());

            param.append("恭喜您成功预约回馈礼品1份！您的礼品领取码为：").append(rushCode.getRushCodeId())
                    .append("，您可在11月3日-11月18日期间凭借此码，到 ").append(message)
                    .append(" 领取礼品。请务必妥善保管礼品领取码，切勿对外泄露。");
            boolean flag = HttpUtils.sendMessage(mobile, param.toString());
            if (flag) {

                insertSms(rushCode, param.toString());
            }
            map.put("status", 200);
            map.put("message", "发送成功!");
        } else {
            map.put("status", 201);
            map.put("message", "发送失败!");
        }
        return map;
    }

    @Override
    public Map ladingApplyCode(String ladingCode) throws Exception {
        Map map = new HashMap();
        if (ladingCode == null || "".equals(ladingCode)) {
            map.put("status", 201);
            map.put("message", "提货码为空!");
            return map;
        }
        RushCode code = rushCodeMapper.getRushCodeById(ladingCode);
        if (code == null) {
            map.put("status", 202);
            map.put("message", "提货码无效!");
            return map;
        }
        if (code.getStatus().equals("2")) {
            map.put("status", 203);
            map.put("message", "提货码已使用!");
            return map;
        } else if (code.getStatus().equals("3")) {
            map.put("status", 204);
            map.put("message", "提货码已作废!");
            return map;
        } else {
            String nowTime = DateUtil.parseDateToStr(new Date(), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
            ;
            code.setStatus("2");
            code.setUseTime(nowTime);
            code.setUpdateTime(nowTime);
            rushCodeMapper.updateRushStatus(code);

            map.put("status", 200);
            map.put("message", "提货成功!");
            return map;
        }
    }

    @Override
    public Map verifyCodeForCategory(String jsonStr) throws Exception {
        JSONObject requestObject = JSONObject.parseObject(jsonStr);
        String rushCodeId = requestObject.getString("rushCode");
        String category = requestObject.getString("category");

        Map map = new HashMap();
        RushCode rushCode = rushCodeMapper.getRushCodeById(rushCodeId);

        if (rushCode == null) {
            map.put("status", 201);
            map.put("message", "不存在!");
            return map;
        }
        if (!category.equals(rushCode.getCategoryCode())) {
            map.put("status", 202);
            map.put("message", "无效!");
            return map;
        }
        if ("2".equals(rushCode.getStatus())) {
            map.put("status", 203);
            map.put("message", "已使用!");
            return map;
        }
        if ("3".equals(rushCode.getStatus())) {
            map.put("status", 204);
            map.put("message", "已作废!");
            return map;
        }
        if ( !StringUtils.isEmpty(rushCode.getExpireTime()) &&
                rushCode.getExpireTime().compareTo(DateUtil.parseDateToStr(new Date(),
                        DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS)) < 0){

            map.put("status", 205);
            map.put("message", "预约码已过期!");
            return map;
        }
        /** 抢购码单独验证 */
        switch (category) {

            case "RUSH_PURCHASE_CODE"://抢购码
                map = verifyCategoryRushCode(requestObject, rushCodeId);
                break;
            default:

                map.put("status", 200);
                map.put("message", "验证成功!");
                break;
        }
        return map;
    }

    @Override
    public Response searchRushCodes(String rushCodeId, String mobile, String status, String configId, String
            beginExpireTime, String endExpireTime, String beginCreateTime, String endCreateTime, String userId, Integer
                                            pageIndex, Integer pageRow) {
        return null;
    }

    @Override
    public Response searchRushConfig(String id, String activityName, String limitSku, Integer
            limitDayQuantity, Integer limitQuantity, Integer expireDay, String status, String creator, String
                                             beginCreateTime, String endCreateTime, Integer pageIndex, Integer pageRow) {
        return null;
    }

    /**
     * 生成预约码
     *
     * @param length
     * @param head
     * @param tail
     * @return
     */

    private String getRushPurchase(int length, String head, String tail) {
        String rushCodeId = RandomUtil.createCoupons(length, head, tail);

        RushCode rushCode = rushCodeMapper.getRushCodeById(rushCodeId);
        if (rushCode != null) {
            rushCodeId = getRushPurchase(length, head, tail);
        }
        return rushCodeId;
    }

    /**
     * 生成预约码进行验证
     *
     * @param rushCode
     * @param nowTime
     * @return
     */
    private Map createCodeVerify(RushCode rushCode, String nowTime) {

        Map map = new HashMap();
        RushConfig rushConfig = rushConfigMapper.getRushConfigById(rushCode.getConfigId());
        if (!StringUtils.isEmpty(rushConfig.getActivityStartTime()) &&
                nowTime.compareTo(rushConfig.getActivityStartTime()) < 0) {

            map.put("status", 207);
            map.put("message", "活动未到开始时间!");
            map.put("data", new ArrayList<>());
            return map;
        }

        if (!StringUtils.isEmpty(rushConfig.getActivityEndTime()) &&
                nowTime.compareTo(rushConfig.getActivityEndTime()) >= 0) {

            map.put("status", 208);
            map.put("message", "活动已过期!");
            map.put("data", new ArrayList<>());
            return map;
        }
        if (rushConfig == null) {

            map.put("status", 201);
            map.put("message", "配置不存在!");
            map.put("data", new ArrayList<>());
            return map;
        }
        if ("2".equals(rushConfig.getStatus())) {

            map.put("status", 202);
            map.put("message", "配置已禁用!");
            map.put("data", new ArrayList<>());
            return map;
        }
        if (rushConfig.getLimitDayQuantity() > 0) {

            String cutOffHour = rushConfig.getActivityStartTime().substring(10, 19);
            String today = nowTime.substring(0, 10);
            String standard = today + cutOffHour;
            String beginDate, endDate;
            Calendar cal = Calendar.getInstance();

            if (nowTime.compareTo(standard) < 0) {

                cal.add(Calendar.DATE, -1);
                beginDate = DateUtil.parseDateToStr(cal.getTime(), DateUtil.DATE_FORMAT_YYYY_MM_DD) + cutOffHour;
                endDate = standard;
            } else {

                cal.add(Calendar.DATE, +1);
                beginDate = standard;
                endDate = DateUtil.parseDateToStr(cal.getTime(), DateUtil.DATE_FORMAT_YYYY_MM_DD) + cutOffHour;
            }

            int codesCount = rushCodeMapper.getRushCounts(rushCode.getConfigId(), null, beginDate, endDate);
            if (codesCount >= rushConfig.getLimitDayQuantity()) {
                map.put("status", 203);
                map.put("message", "今天已领完!");
                map.put("data", new ArrayList<>());
                return map;
            }
        }
        if (rushConfig.getLimitQuantity() > 0) {

            int codesCount = rushCodeMapper.getRushCounts(rushCode.getConfigId(), null, rushConfig.getActivityStartTime(),
                    rushConfig.getActivityEndTime());
            if (codesCount >= rushConfig.getLimitQuantity()) {
                map.put("status", 204);
                map.put("message", "已全部领完!");
                map.put("data", new ArrayList<>());
                return map;
            }
        }

        if (!rushCode.getMobile().isEmpty()) {
            int codesCount = rushCodeMapper.getRushCounts(rushCode.getConfigId(), rushCode.getMobile(),
                    rushConfig.getActivityStartTime(), rushConfig.getActivityEndTime());
            if (codesCount > 0) {
                map.put("status", 205);
                map.put("message", "手机号已经领取过!");
                map.put("data", new ArrayList<>());
                return map;
            }
        }
        return map;
    }

    /**
     * 验证抢购码
     *
     * @param jsonObject
     * @param rushCode
     * @return
     */
    private Map verifyCategoryRushCode(JSONObject jsonObject, String rushCode) {
        Map map = new HashMap();
        JSONArray jsonArray = jsonObject.getJSONArray("lineItems");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < jsonArray.size(); i++) {

            JSONObject skuObject = jsonArray.getJSONObject(i);
            String skuId = skuObject.getString("skuId");
            sb.append(skuId).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        Integer count = rushConfigSkuMapper.getConfigSkuBySkuIds(sb.toString(), "0");
        if (count < 1) {
            map.put("status", 200);
            map.put("message", "您购买的商品无需使用预约码!");
            return map;
        }

        String configCode = rushCode.substring(rushCode.length() - 3, rushCode.length());
        RushConfig config = rushConfigMapper.getRushConfigByConfigCode(configCode);

        boolean flag = false;//判断购买商品是否包含预约码使用商品
        String[] skus = config.getLimitSku().split(",");
        for (String sku : skus) {
            System.out.println("sb.toString() = " + sb.toString());
            if (sb.toString().contains(sku)) {
                flag = true;
            }
        }
        if (flag) {

            map.put("status", 200);
            map.put("message", "验证成功!");
        } else {
            map.put("status", 206);
            map.put("message", "预约码不适用于您购买的商品!");
        }
        return map;
    }

    /**
     * 验证抢购码
     *
     * @param jsonObject
     * @param ticket
     * @return
     */
    private Map verifyCode(JSONObject jsonObject, String ticket) {
        Map<String, Object> map = new HashMap<String, Object>();
        JSONArray jsonArray = jsonObject.getJSONArray("lineItems");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < jsonArray.size(); i++) {

            JSONObject skuObject = jsonArray.getJSONObject(i);
            String skuId = skuObject.getString("skuId");
            sb.append(skuId).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        Integer count = rushConfigSkuMapper.getConfigSkuBySkuIds(sb.toString(), "0");
        if (count < 1) {
            map.put("status", 200);
            map.put("message", "您购买的商品无需使用预约码!");
            return map;
        }
        if (ticket.isEmpty()) {

            map.put("status", 206);
            map.put("message", "预约码为空!");
            return map;
        }
        if (ticket.length() < 3) {

            map.put("status", 207);
            map.put("message", "预约码无效!");
            return map;
        }

        String configCode = ticket.substring(ticket.length() - 3, ticket.length());
        RushConfig config = rushConfigMapper.getRushConfigByConfigCode(configCode);
        if (config == null) {
            map.put("status", 205);
            map.put("message", "预约码无效!");
            return map;
        }

        RushCode rushCode = rushCodeMapper.getRushCodeById(ticket);
        if (rushCode == null) {

            map.put("status", 201);
            map.put("message", "预约码无效!");
            return map;
        }
        if (config.getId() != rushCode.getConfigId()) {

            map.put("status", 202);
            map.put("message", "预约码输入错误!");
            return map;
        }
        if (rushCode.getStatus().equals("2")) {

            map.put("status", 208);
            map.put("message", "预约码已使用!");
            return map;
        }
        String nowTime = DateUtil.parseDateToStr(new Date(), DateUtil.DATE_FORMAT_YYYY_MM_DD);
        if (rushCode.getExpireTime().compareTo(nowTime) < 0) {

            map.put("status", 203);
            map.put("message", "预约码已过期!");
            return map;
        }

        boolean flag = false;//判断购买商品是否包含预约码使用商品
        String[] skus = config.getLimitSku().split(",");
        for (String sku : skus) {
            System.out.println("sb.toString() = " + sb.toString());
            if (sb.toString().contains(sku)) {
                flag = true;
            }
        }
        if (!flag) {

            map.put("status", 204);
            map.put("message", "预约码不适用于您购买的商品!");
        }
        return map;
    }

    /**
     * 插入发送短信日志
     *
     * @param rushCode
     * @param message
     */
    private void insertSms(RushCode rushCode, String message) {

        AiHuiShouSms aiHuiShouSms = new AiHuiShouSms();
        aiHuiShouSms.setVoucher(rushCode.getRushCodeId());
        aiHuiShouSms.setMobile(rushCode.getMobile());
        aiHuiShouSms.setMessage(message.toString());
        aiHuiShouSms.setSendTime(DateUtil.parseDateToStr(new Date(), DateUtil.DATE_FORMAT_YYYY_MM_DD));

        aiHuiShouSmsMapper.insertAHSSms(aiHuiShouSms);
    }


    /**
     * 减少库存
     *
     * @param sku
     * @param quantity
     * @return
     */
    private String decrStocks(String sku, int quantity) {

        JSONObject parameter = new JSONObject();
        parameter.put("sku", sku);
        parameter.put("count", quantity);
        parameter.put("decrtype", 1);
        parameter.put("decrmessage", "");
        parameter.put("depotId", "XN0001");

        String result = HttpUtils.post("http://barcelona.api.niu.com/stock/skudecrStock",
                parameter.toString());
        JSONObject object = JSON.parseObject(result);

        return object.getString("status");
    }

}
