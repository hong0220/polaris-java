package com.tencent.cloud.circuitbreaker;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "polaris-circuitbreaker-example-a", fallback = ProviderAFallback.class)
public interface ProviderA {

    @GetMapping("/example/service/a/info")
    String info();
}