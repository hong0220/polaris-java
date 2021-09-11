package com.polaris.demo;

import com.tencent.polaris.api.core.ProviderAPI;
import com.tencent.polaris.api.rpc.InstanceDeregisterRequest;
import com.tencent.polaris.api.rpc.InstanceHeartbeatRequest;
import com.tencent.polaris.api.rpc.InstanceRegisterRequest;
import com.tencent.polaris.api.rpc.InstanceRegisterResponse;
import com.tencent.polaris.factory.api.DiscoveryAPIFactory;

public class InstanceRegisterTest {

    public static void main(String[] args) throws InterruptedException {
        // 服务注册
        InstanceRegisterRequest instanceRegisterRequest = new InstanceRegisterRequest();
        instanceRegisterRequest.setNamespace("Test");
        instanceRegisterRequest.setService("dummy");
        instanceRegisterRequest.setHost("127.0.0.1");
        instanceRegisterRequest.setPort(12380);
        instanceRegisterRequest.setTtl(2);

        ProviderAPI providerAPI = DiscoveryAPIFactory.createProviderAPI();
        InstanceRegisterResponse instanceRegisterResponse = providerAPI.register(instanceRegisterRequest);

        // 心跳上报
        InstanceHeartbeatRequest instanceHeartbeatRequest = new InstanceHeartbeatRequest();
        instanceHeartbeatRequest.setNamespace("Test");
        instanceHeartbeatRequest.setService("dummy");
        instanceHeartbeatRequest.setHost("127.0.0.1");
        instanceHeartbeatRequest.setPort(12380);
        providerAPI.heartbeat(instanceHeartbeatRequest);

        // 服务反注册
//        InstanceDeregisterRequest request = new InstanceDeregisterRequest();
//        request.setNamespace("Test");
//        request.setService("dummy");
//        request.setHost("127.0.0.1");
//        request.setPort(12380);
//        providerAPI.deRegister(request);

        InstanceRegisterTest.class.wait();

        providerAPI.destroy();
    }
}