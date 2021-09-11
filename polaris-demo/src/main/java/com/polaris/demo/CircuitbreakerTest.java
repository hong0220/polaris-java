package com.polaris.demo;

import com.tencent.polaris.api.core.ConsumerAPI;
import com.tencent.polaris.api.core.ProviderAPI;
import com.tencent.polaris.api.pojo.Instance;
import com.tencent.polaris.api.pojo.RetStatus;
import com.tencent.polaris.api.rpc.GetOneInstanceRequest;
import com.tencent.polaris.api.rpc.InstanceRegisterRequest;
import com.tencent.polaris.api.rpc.InstanceRegisterResponse;
import com.tencent.polaris.api.rpc.InstancesResponse;
import com.tencent.polaris.api.rpc.ServiceCallResult;
import com.tencent.polaris.factory.api.DiscoveryAPIFactory;

public class CircuitbreakerTest {

    public static void main(String[] args) throws InterruptedException {
        ProviderAPI providerAPI = DiscoveryAPIFactory.createProviderAPI();
        // 添加2个服务实例
        //add 2 instances, one is 127.0.0.1:10010, second is 127.0.0.1:10011
        for (int i = 0; i < 2; i++) {
            InstanceRegisterRequest request = new InstanceRegisterRequest();
            request.setNamespace("default");
            request.setService("test_service");
            request.setHost("127.0.0.1");
            request.setPort(10010 + i);
            InstanceRegisterResponse instanceRegisterResponse = providerAPI.register(request);
        }

        ConsumerAPI consumerAPI = DiscoveryAPIFactory.createConsumerAPI();
        // 针对其中一个实例连续上报10次失败（模拟业务调用10次失败）
        // report 10 continuous failure
        for (int i = 0; i < 10; i++) {
            ServiceCallResult serviceCallResult = new ServiceCallResult();
            serviceCallResult.setNamespace("default");
            serviceCallResult.setService("test_service");
            serviceCallResult.setHost("127.0.0.1");
            serviceCallResult.setPort(10011);
            serviceCallResult.setRetStatus(RetStatus.RetFail);
            serviceCallResult.setRetCode(500);
            serviceCallResult.setDelay(1000);
            consumerAPI.updateServiceCallResult(serviceCallResult);
        }

        Thread.sleep(10000);

        System.out.println("服务熔断");

        // 实例被熔断，通过GetOneInstance无法再获取该实例（已经被剔除）
        for (int i = 0; i < 10; i++) {
            GetOneInstanceRequest request = new GetOneInstanceRequest();
            request.setNamespace("default");
            request.setService("test_service");
            InstancesResponse instancesResponse = consumerAPI.getOneInstance(request);
            // 服务实例信息
            for (Instance instance : instancesResponse.getInstances()) {
                // instance port won't be 10010, it's been kick off
                System.out.printf("selected instance is %s:%d%n", instance.getHost(), instance.getPort());
            }
        }
    }
}