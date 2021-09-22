package com.tencent.cloud.ratelimit;

import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.TooManyRequests;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * http://localhost:48081/business/invoke
 * <p>
 * curl -XPOST -H'Content-Type:application/json' -d @ratelimit.json 'http://127.0.0.1:8090/naming/v1/ratelimits'
 * <p>
 * 请求被限流：request blocked by polaris, reason is
 */
@RestController
@RequestMapping("/business")
public class BusinessController {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${spring.application.name}")
    private String appName;
    private final AtomicInteger index = new AtomicInteger(0);

    /**
     * 获取当前服务的信息
     */
    @GetMapping("/info")
    public String info() {
        return "hello world for ratelimit service " + index.incrementAndGet();
    }

    @GetMapping("/invoke")
    public String invokeInfo() throws Exception {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 30; i++) {
            try {
                ResponseEntity<String> entity = restTemplate
                    .getForEntity("http://" + appName + "/business/info", String.class);
                builder.append(entity.getBody()).append("<br/>");

                System.out.println("返回结果:" + entity.getBody());
            } catch (RestClientException e) {
                System.out.println("异常了");

                if (e instanceof TooManyRequests) {
                    builder.append(((TooManyRequests) e).getResponseBodyAsString()).append(index.incrementAndGet())
                        .append("<br/>");
                } else {
                    throw e;
                }
            }
        }
        return builder.toString();
    }
}