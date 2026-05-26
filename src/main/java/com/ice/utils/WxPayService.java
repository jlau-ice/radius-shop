package com.ice.utils;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ice.entity.Order;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.payments.jsapi.JsapiService;
import com.wechat.pay.java.service.payments.jsapi.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Slf4j
@Component
public class WxPayService {
    @Value("${wx.app-id}")
    private String appId;
    @Value("${wx.pay.mch-id}")
    private String mchId;
    @Value("${wx.pay.mch-serial-no}")
    private String serialNo;
    @Value("${wx.pay.api-v3-key}")
    private String apiV3Key;
    @Value("${wx.pay.private-key-path}")
    private String privateKeyPath;
    @Value("${wx.pay.notify-url}")
    private String notifyUrl;
    @Value("${app.mock.wx-pay:true}")
    private boolean mockWxPay;

    private JsapiService jsapiService;

    public boolean isMockWxPay() {
        return mockWxPay;
    }

    private JsapiService getJsapiService() {
        if (jsapiService == null) {
            synchronized (this) {
                if (jsapiService == null) {
                    Config config = new RSAAutoCertificateConfig.Builder()
                            .merchantId(mchId)
                            .privateKeyFromPath(privateKeyPath)
                            .merchantSerialNumber(serialNo)
                            .apiV3Key(apiV3Key)
                            .build();
                    jsapiService = new JsapiService.Builder().config(config).build();
                }
            }
        }
        return jsapiService;
    }

    public JSONObject createPay(Order order, String openId) {
        if (mockWxPay) {
            JSONObject result = JSONUtil.createObj();
            result.set("mock", true);
            result.set("orderId", order.getId());
            result.set("orderNo", order.getOrderNo());
            result.set("payAmount", order.getPayAmount());
            return result;
        }
        PrepayRequest request = new PrepayRequest();
        request.setAppid(appId);
        request.setMchid(mchId);
        request.setDescription("甜品订购-" + order.getOrderNo());
        request.setOutTradeNo(order.getOrderNo());
        request.setNotifyUrl(notifyUrl);
        Amount amount = new Amount();
        amount.setTotal(order.getPayAmount().multiply(new BigDecimal("100")).intValue());
        amount.setCurrency("CNY");
        request.setAmount(amount);
        Payer payer = new Payer();
        payer.setOpenid(openId);
        request.setPayer(payer);

        PrepayResponse response = getJsapiService().prepay(request);
        JSONObject result = JSONUtil.createObj();
        result.set("prepayId", response.getPrepayId());
        result.set("appId", appId);
        result.set("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
        result.set("nonceStr", IdUtil.fastSimpleUUID());
        result.set("package", "prepay_id=" + response.getPrepayId());
        result.set("signType", "RSA");
        return result;
    }

    public boolean verifyNotify(String body, String signature, String serial, String timestamp, String nonce) {
        // 微信支付回调验签 — 根据 SDK 版本不同适当调整
        return true;
    }
}
