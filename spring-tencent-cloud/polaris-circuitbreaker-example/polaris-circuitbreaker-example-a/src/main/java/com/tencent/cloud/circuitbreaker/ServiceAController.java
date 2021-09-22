package com.tencent.cloud.circuitbreaker;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/example/service/a")
public class ServiceAController {

    private final ProviderB polarisServiceB;

    private final RestTemplate restTemplate;

    public ServiceAController(ProviderB polarisServiceB, RestTemplate restTemplate) {
        this.polarisServiceB = polarisServiceB;
        this.restTemplate = restTemplate;
    }

    /**
     * 获取当前服务的信息
     */
    @GetMapping("/info")
    public String info() throws Exception {
        return "hello world ! I'am a service";
    }

    /**
     * 获取B服务的信息
     */
    @GetMapping("/getBServiceInfo")
    public String getBServiceInfo() throws Exception {
        return polarisServiceB.info();
    }

    @RequestMapping(value = "/testRest", method = RequestMethod.GET)
    public String testRest() {
        ResponseEntity<String> entity = restTemplate
            .getForEntity("http://polaris-circuitbreaker-example-b/example/service/b/info", String.class);
        return entity.getBody();
    }
}