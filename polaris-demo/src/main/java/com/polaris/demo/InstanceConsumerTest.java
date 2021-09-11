package com.polaris.demo;

import com.tencent.polaris.api.core.ConsumerAPI;
import com.tencent.polaris.api.pojo.Instance;
import com.tencent.polaris.api.rpc.GetAllInstancesRequest;
import com.tencent.polaris.api.rpc.InstancesResponse;
import com.tencent.polaris.factory.api.DiscoveryAPIFactory;

public class InstanceConsumerTest {

    /**
     * 拉取所有的服务实例
     */
    public static void main(String[] args) {
        ConsumerAPI consumerAPI = DiscoveryAPIFactory.createConsumerAPI();

        GetAllInstancesRequest request = new GetAllInstancesRequest();
        request.setNamespace("default");
        request.setService("test_service");
        InstancesResponse instancesResponse = consumerAPI.getAllInstance(request);
        // 服务实例信息
        for (Instance instance : instancesResponse.getInstances()) {
            System.out.printf("instance is %s:%d%n", instance.getHost(), instance.getPort());
        }

        consumerAPI.destroy();
    }
}