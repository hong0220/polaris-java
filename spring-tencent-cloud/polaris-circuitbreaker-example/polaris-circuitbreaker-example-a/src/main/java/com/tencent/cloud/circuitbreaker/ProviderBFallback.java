package com.tencent.cloud.circuitbreaker;

import org.springframework.stereotype.Component;

@Component
public class ProviderBFallback implements ProviderB {

    /**
     * 获取B的服务的信息
     */
    @Override
    public String info() {
        return "trigger the refuse for service b";
    }
}