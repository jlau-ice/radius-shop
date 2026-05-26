package com.ice.utils;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class WxMiniService {
    @Value("${wx.app-id}")
    private String appId;
    @Value("${wx.app-secret}")
    private String appSecret;
    @Value("${app.mock.wx-login:true}")
    private boolean mockWxLogin;

    private final RestTemplate rest = new RestTemplate();

    public JSONObject code2Session(String code) {
        if (mockWxLogin) {
            JSONObject mock = new JSONObject();
            mock.set("openid", "dev_" + code);
            return mock;
        }
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appId +
                "&secret=" + appSecret + "&js_code=" + code + "&grant_type=authorization_code";
        String resp = rest.getForObject(url, String.class);
        JSONObject json = JSONUtil.parseObj(resp);
        if (json.containsKey("errcode") && json.getInt("errcode") != 0) {
            log.error("微信登录失败: {}", resp);
            throw new com.ice.common.exception.BusinessException("微信登录失败");
        }
        return json;
    }
}
