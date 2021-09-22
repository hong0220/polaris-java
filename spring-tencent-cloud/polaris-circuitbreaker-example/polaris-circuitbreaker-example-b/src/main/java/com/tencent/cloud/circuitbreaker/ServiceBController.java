package com.tencent.cloud.circuitbreaker;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/example/service/b")
public class ServiceBController {

    private final ProviderA polarisServiceA;

    private final RestTemplate restTemplate;

    public ServiceBController(ProviderA polarisServiceA, RestTemplate restTemplate) {
        this.polarisServiceA = polarisServiceA;
        this.restTemplate = restTemplate;
    }

    /**
     * 获取当前服务的信息
     */
    @GetMapping("/info")
    public String info() throws Exception {
//        return "hello world ! I'am a service";
        throw new RuntimeException("failed for call my service");
    }

    /**
     * 获取B服务的信息
     */
    @GetMapping("/getAServiceInfo")
    public String getAServiceInfo() throws Exception {
        return polarisServiceA.info();
    }

    @RequestMapping(value = "/testRest", method = RequestMethod.GET)
    public String testRest() {
        ResponseEntity<String> entity = restTemplate
            .getForEntity("http://polaris-circuitbreaker-example-a/example/service/b/info", String.class);
        return entity.getBody();
    }
}