package com.polaris.demo;

import com.tencent.polaris.ratelimit.api.core.LimitAPI;
import com.tencent.polaris.ratelimit.api.rpc.QuotaRequest;
import com.tencent.polaris.ratelimit.api.rpc.QuotaResponse;
import com.tencent.polaris.ratelimit.api.rpc.QuotaResultCode;
import com.tencent.polaris.ratelimit.factory.LimitAPIFactory;
import java.util.HashMap;
import java.util.Map;

public class RatelimitTest {

    /**
     * curl -XPOST -H'Content-Type:application/json' -d @ratelimit.json 'http://127.0.0.1:8090/naming/v1/ratelimits'
     */
    public static void main(String[] args) {
        test1();
        test2();
    }

    /**
     * 针对/path1获取配额
     */
    public static void test1() {
        LimitAPI limitAPI = LimitAPIFactory.createLimitAPI();

        QuotaRequest quotaRequest = new QuotaRequest();
        quotaRequest.setNamespace("default");
        quotaRequest.setService("test_service");

        Map<String, String> labels = new HashMap();
        labels.put("method", "/path1");
        quotaRequest.setLabels(labels);

        QuotaResponse quotaResponse = limitAPI.getQuota(quotaRequest);
        if (QuotaResultCode.QuotaResultOk == quotaResponse.getCode()) {
            // quota acquired, now can continue the procedure process
            System.out.println("quota result ok");
        } else {
            // quota limited, we should block the user request
            System.out.printf("quota result fail, info is %s%n", quotaResponse.getInfo());
        }

        limitAPI.destroy();
    }

    /**
     * 针对/path2获取配额
     */
    public static void test2() {
        LimitAPI limitAPI = LimitAPIFactory.createLimitAPI();

        QuotaRequest quotaRequest = new QuotaRequest();
        quotaRequest.setNamespace("default");
        quotaRequest.setService("test_service");

        Map<String, String> labels = new HashMap();
        labels.put("method", "/path2");
        quotaRequest.setLabels(labels);

        QuotaResponse quotaResponse = limitAPI.getQuota(quotaRequest);
        if (QuotaResultCode.QuotaResultOk == quotaResponse.getCode()) {
            // quota acquired, now can continue the procedure process
            System.out.printf("quota result ok, info is %s%n", quotaResponse.getInfo());
        } else {
            // quota limited, we should block the user request
            System.out.printf("quota result fail, info is %s%n", quotaResponse.getInfo());
        }

        limitAPI.destroy();
    }
}