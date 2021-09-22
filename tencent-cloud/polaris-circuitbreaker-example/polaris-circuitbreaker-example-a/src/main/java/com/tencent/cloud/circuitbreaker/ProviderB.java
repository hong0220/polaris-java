package com.tencent.cloud.circuitbreaker;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "polaris-circuitbreaker-example-b", fallback = ProviderBFallback.class)
public interface ProviderB {

    @GetMapping("/example/service/b/info")
    String info();
}