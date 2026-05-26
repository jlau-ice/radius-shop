package com.ice.controller.app;

import com.ice.common.result.Result;
import com.ice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.BufferedReader;

@RestController
@RequestMapping("/api/app/pay")
@RequiredArgsConstructor
public class PayController {
    private final OrderService orderService;

    @PostMapping("/notify")
    public String notify(HttpServletRequest request) {
        try {
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
            cn.hutool.json.JSONObject json = cn.hutool.json.JSONUtil.parseObj(sb.toString());
            String orderNo = json.getStr("out_trade_no");
            String transactionId = json.getStr("transaction_id");
            String tradeState = json.getStr("trade_state");
            if ("SUCCESS".equals(tradeState)) {
                orderService.handlePayNotify(orderNo, transactionId);
            }
        } catch (Exception e) {
            return "{\"code\":\"FAIL\",\"message\":\"fail\"}";
        }
        return "{\"code\":\"SUCCESS\",\"message\":\"ok\"}";
    }
}
