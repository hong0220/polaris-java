package com.polaris.demo;

import com.tencent.polaris.api.core.ProviderAPI;
import com.tencent.polaris.api.rpc.InstanceDeregisterRequest;
import com.tencent.polaris.api.rpc.InstanceHeartbeatRequest;
import com.tencent.polaris.api.rpc.InstanceRegisterRequest;
import com.tencent.polaris.api.rpc.InstanceRegisterResponse;
import com.tencent.polaris.factory.api.DiscoveryAPIFactory;

public class InstanceRegisterTest {

    public static void main(String[] args) throws InterruptedException {
        ProviderAPI providerAPI = DiscoveryAPIFactory.createProviderAPI();

        // 服务注册
        InstanceRegisterRequest register = new InstanceRegisterRequest();
        register.setNamespace("default");
        register.setService("test_service");
        register.setHost("127.0.0.1");
        register.setPort(12380);
        register.setTtl(2);
        InstanceRegisterResponse instanceRegisterResponse = providerAPI.register(register);
        System.out.println("返回结果:" + instanceRegisterResponse.getInstanceId());

        // 心跳上报
        for (int i = 0; i < 100; ++i) {
            InstanceHeartbeatRequest heartbeat = new InstanceHeartbeatRequest();
            heartbeat.setNamespace("default");
            heartbeat.setService("test_service");
            heartbeat.setHost("127.0.0.1");
            heartbeat.setPort(12380);
            providerAPI.heartbeat(heartbeat);

            Thread.sleep(1000);
        }

//        // 服务反注册
//        InstanceDeregisterRequest deRegister = new InstanceDeregisterRequest();
//        deRegister.setNamespace("default");
//        deRegister.setService("test_service");
//        deRegister.setHost("127.0.0.1");
//        deRegister.setPort(12380);
//        providerAPI.deRegister(deRegister);

        providerAPI.destroy();
    }
}