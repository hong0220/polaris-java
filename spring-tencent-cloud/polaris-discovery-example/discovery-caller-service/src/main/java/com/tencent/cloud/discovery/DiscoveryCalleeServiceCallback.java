package com.tencent.cloud.discovery;

import org.springframework.stereotype.Component;

@Component
public class DiscoveryCalleeServiceCallback implements DiscoveryCalleeService {

    @Override
    public int sum(int value1, int value2) {
        return 0;
    }
}