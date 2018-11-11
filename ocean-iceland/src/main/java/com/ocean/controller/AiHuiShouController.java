package com.ocean.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.ocean.model.pojo.AiHuiShouOrder;
import com.ocean.model.pojo.AiHuiShouVoucher;
import com.ocean.model.vo.Response;
import com.ocean.service.AiHuiShouService;
import com.ocean.util.CommonUtil;
import com.ocean.util.DateUtil;
import com.ocean.util.MD5Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * Author: Qigaowei
 * Date: 2018/7/11 16:43
 * Description: 爱回收业务Controller
 */

@RestController
@RequestMapping("/aihuishou")
public class AiHuiShouController {

    private Log log = LogFactory.getLog(AiHuiShouController.class);


    @Value("${aihuishouSecret}")
    private String aihuishouSecret;

    @Autowired
    private AiHuiShouService aiHuiShouService;

    /**
     * 根据订单号获取爱回收订单
     *
     * @param orderNo
     * @return
     */
    @RequestMapping(value = "/order/{orderNo}", method = RequestMethod.GET)
    public AiHuiShouOrder getAHSOrder(@PathVariable("orderNo") String orderNo) {
        AiHuiShouOrder order = new AiHuiShouOrder();
        try {
            order = aiHuiShouService.getAiHuiShouOrder(orderNo);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询爱回收订单异常,订单号:" + orderNo, e);
        }
        return order;
    }

    /**
     * 接收爱回收推送到牛电官网的订单
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/order/receiveAHSOrder", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> receiveAHSOrder(@RequestBody String aHSOrder) {
        Map map = new HashMap();
        try {
            if (null == aHSOrder || "".equals(aHSOrder)) {
                map.put("status", 501);
                map.put("message", "参数为空!");
                return new ResponseEntity<Map<String, Object>>(map, HttpStatus.valueOf((Integer) map.get("status")));
            }
            JSONObject orderJson = JSONObject.parseObject(aHSOrder);
            aiHuiShouService.insertAHSLog(orderJson.getString("order_no"), aHSOrder);

            //去掉sign签名,重组字符串加密验签
            HashMap<String, Object> orderMap = JSON.parseObject(aHSOrder,
                    LinkedHashMap.class, Feature.OrderedField);
            orderMap.remove("sign");
            String orderPrams = CommonUtil.getSortPrams(orderMap, aihuishouSecret);

            log.info("排序重组后的字符串:" + orderPrams);
            String paramMD5 = MD5Utils.getMD5String(orderPrams);
            log.info("MD5加密串: " + paramMD5);
            AiHuiShouOrder aiHuiShouOrder = JSON.parseObject(aHSOrder, AiHuiShouOrder.class);
            if (paramMD5.equals(orderJson.getString("sign"))) {

                map = aiHuiShouService.insertAHSOrder(aiHuiShouOrder);
            } else {
                map.put("status", 502);
                map.put("message", "签名验证失败!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("接收爱回收订单异常,订单号", e);
            map.put("status", 500);
            map.put("message", "系统异常");
            map.put("error", e.getMessage());
        }
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.valueOf((Integer) map.get("status")));
    }


    /**
     * 接收爱回收推送的优惠券信息
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/voucher/receiveAHSVoucher", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> receiveAHSVoucher(@RequestBody String aHSVoucher) {
        Map map = new HashMap();
        try {
            if (null == aHSVoucher || "".equals(aHSVoucher)) {
                map.put("status", 501);
                map.put("message", "参数为空!");
                return new ResponseEntity<Map<String, Object>>(map, HttpStatus.valueOf((Integer) map.get("status")));
            }
            JSONObject orderJson = JSONObject.parseObject(aHSVoucher);
            aiHuiShouService.insertAHSLog(orderJson.getString("order_no"), aHSVoucher);

            //去掉sign签名,重组字符串加密验签
            HashMap<String, Object> voucherMap = JSON.parseObject(aHSVoucher,
                    LinkedHashMap.class, Feature.OrderedField);
            voucherMap.remove("sign");
            String voucherPrams = CommonUtil.getSortPrams(voucherMap, aihuishouSecret);

            log.info("排序重组后的字符串:" + voucherPrams);
            String paramMD5 = MD5Utils.getMD5String(voucherPrams);
            log.info("MD5加密串: " + paramMD5);
            AiHuiShouVoucher aiHuiShouVoucher = JSON.parseObject(aHSVoucher, AiHuiShouVoucher.class);
            if (paramMD5.equals(orderJson.getString("sign"))) {

                map = aiHuiShouService.createAHSVoucher(aiHuiShouVoucher);
            } else {
                map.put("status", 502);
                map.put("message", "签名验证失败!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("接收爱回收优惠券推送信息失败", e);
            map.put("status", 500);
            map.put("message", "系统异常");
            map.put("error", e.getMessage());
        }
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.valueOf((Integer) map.get("status")));
    }

    /**
     * 根据代金券,查询代金券详情
     *
     * @param voucherId
     * @return
     */
    @RequestMapping(value = "/voucher/{voucherId}", method = RequestMethod.GET)
    public AiHuiShouVoucher getAHSVoucher(@PathVariable("voucherId") String voucherId) {
        AiHuiShouVoucher voucher = new AiHuiShouVoucher();
        try {
            voucher = aiHuiShouService.getAiHuiShouVoucher(voucherId);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询代金券详情异常,代金券:" + voucherId, e);
        }
        return voucher;
    }

    /**
     * 使用代金券
     *
     * @param voucher
     * @return
     */
    @RequestMapping(value = "/voucher/clipCoupons", method = RequestMethod.POST)
    public Map clipCoupons(@RequestBody AiHuiShouVoucher voucher) {
        Map map = new HashMap();
        try {
            map = aiHuiShouService.clipCoupons(voucher);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 500);
            map.put("message", "系统异常");
            map.put("error", e.toString());
            map.put("data", null);
            log.error("使用代金券异常,代金券:" + voucher.getVoucherId(), e);
        }
        return map;
    }


    /**
     * 验证优惠券,以及获取代金券金额
     *
     * @param voucher
     * @return
     */
    @RequestMapping(value = "/voucher/{voucher}/amount", method = RequestMethod.GET)
    public Map getVoucherAmount(@PathVariable String voucher) {
        Map map = new HashMap();
        try {
            map = aiHuiShouService.getVoucherAmount(voucher);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 500);
            map.put("message", "系统异常");
            map.put("data", null);
            map.put("error", e.toString());
            log.error("使用代金券异常,代金券:" + voucher, e);
        }
        return map;
    }

    /**
     * 修改代金券信息
     *
     * @param voucher
     * @return
     */
    @RequestMapping(value = "/voucher/updateVoucher", method = RequestMethod.POST)
    public Map updateVoucher(@RequestBody AiHuiShouVoucher voucher) {
        Map map = new HashMap();
        try {
            voucher.setUpdatedAt(DateUtil.parseDateToStr(new Date(), DateUtil.DATE_FORMAT_YYYY_MM_DD));
            int count = aiHuiShouService.updateVoucher(voucher);
            if (count == 1) {
                map.put("status", 200);
                map.put("message", "修改成功!");
            } else {
                map.put("status", 201);
                map.put("message", "修改失败!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 500);
            map.put("message", "系统异常");
            map.put("error", e.toString());
            log.error("更新代金券异常,代金券:" + voucher.getVoucherId(), e);
        }
        return map;
    }

    /**
     * 爱回收订单列表
     *
     * @param orderNo
     * @param mobile
     * @param productName
     * @param brandName
     * @param categoryName
     * @param skuName
     * @param finalAmount
     * @param status
     * @param beginCreatedAt
     * @param endCreatedAt
     * @param beginCompletedAt
     * @param endCompletedAt
     * @param beginCreateTime
     * @param endCreateTime
     * @param pageIndex
     * @param pageRow
     * @return
     */
    @RequestMapping(value = "/order/searchAHSOrder", method = RequestMethod.GET)
    public Response searchAHSOrder(@RequestParam(value = "orderNo", required = false) String orderNo,
                                   @RequestParam(value = "mobile", required = false) String mobile,
                                   @RequestParam(value = "productName", required = false) String productName,
                                   @RequestParam(value = "brandName", required = false) String brandName,
                                   @RequestParam(value = "categoryName", required = false) String categoryName,
                                   @RequestParam(value = "skuName", required = false) String skuName,
                                   @RequestParam(value = "finalAmount", required = false) BigDecimal finalAmount,
                                   @RequestParam(value = "status", required = false) String status,
                                   @RequestParam(value = "beginCreatedAt", required = false) String beginCreatedAt,
                                   @RequestParam(value = "endCreatedAt", required = false) String endCreatedAt,
                                   @RequestParam(value = "beginCompletedAt", required = false) String beginCompletedAt,
                                   @RequestParam(value = "endCompletedAt", required = false) String endCompletedAt,
                                   @RequestParam(value = "beginCreateTime", required = false) String beginCreateTime,
                                   @RequestParam(value = "endCreateTime", required = false) String endCreateTime,
                                   @RequestParam(value = "pageIndex", required = false) Integer pageIndex,
                                   @RequestParam(value = "pageRow", required = false) Integer pageRow) {

        if (pageIndex == null) {
            pageIndex = 0;
        }
        if (pageRow == null) {
            pageRow = 10;
        }

        Response response = aiHuiShouService.searchOrders(orderNo, mobile, productName, brandName, categoryName,
                skuName, finalAmount, status, beginCreatedAt, endCreatedAt, beginCompletedAt, endCompletedAt,
                beginCreateTime, endCreateTime, pageIndex, pageRow);

        return response;
    }

    /**
     * 爱回收代金券列表
     *
     * @param voucherId
     * @param orderNo
     * @param amount
     * @param status
     * @param userName
     * @param modifier
     * @param beginCreateTime
     * @param endCreateTime
     * @param beginUseTime
     * @param endUseTime
     * @param beginUpdatedAt
     * @param endUpdatedAt
     * @param customerId
     * @param pageIndex
     * @param pageRow
     * @return
     */
    @RequestMapping(value = "/voucher/searchAHSVoucher", method = RequestMethod.GET)
    public Response searchAHSVoucher(@RequestParam(value = "voucherId", required = false) String voucherId,
                                     @RequestParam(value = "orderNo", required = false) String orderNo,
                                     @RequestParam(value = "amount", required = false) BigDecimal amount,
                                     @RequestParam(value = "status", required = false) String status,
                                     @RequestParam(value = "userName", required = false) String userName,
                                     @RequestParam(value = "modifier", required = false) String modifier,
                                     @RequestParam(value = "beginCreateTime", required = false) String beginCreateTime,
                                     @RequestParam(value = "endCreateTime", required = false) String endCreateTime,
                                     @RequestParam(value = "beginUseTime", required = false) String beginUseTime,
                                     @RequestParam(value = "endUseTime", required = false) String endUseTime,
                                     @RequestParam(value = "beginUpdatedAt", required = false) String beginUpdatedAt,
                                     @RequestParam(value = "endUpdatedAt", required = false) String endUpdatedAt,
                                     @RequestParam(value = "customerId", required = false) String customerId,
                                     @RequestParam(value = "niuOrderNo", required = false) String niuOrderNo,
                                     @RequestParam(value = "pageIndex", required = false) Integer pageIndex,
                                     @RequestParam(value = "pageRow", required = false) Integer pageRow) {

        if (pageIndex == null) {
            pageIndex = 0;
        }
        if (pageRow == null) {
            pageRow = 10;
        }

        Response response = aiHuiShouService.searchVouchers(voucherId, orderNo, amount, status, userName, modifier,
                beginCreateTime, endCreateTime, beginUseTime, endUseTime, beginUpdatedAt, endUpdatedAt, customerId,
                niuOrderNo, pageIndex, pageRow);

        return response;
    }

    /**
     * 通过爱回收订单号,给客户重新发送短信
     *
     * @param orderNo
     * @return
     */
    @RequestMapping(value = "/message/send/{orderNo}", method = RequestMethod.GET)
    public Map sendMessage(@PathVariable String orderNo) {
        Map map = new HashMap();
        try {
            map = aiHuiShouService.sendMessage(orderNo);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("重新发送代金券短信异常", e);
            map.put("status", 500);
            map.put("message", "系统异常!");
            map.put("error", e);
        }
        return map;
    }
}

