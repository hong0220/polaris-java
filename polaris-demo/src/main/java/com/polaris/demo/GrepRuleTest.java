package com.polaris.demo;

import com.tencent.polaris.api.core.ConsumerAPI;
import com.tencent.polaris.api.core.ProviderAPI;
import com.tencent.polaris.api.pojo.Instance;
import com.tencent.polaris.api.pojo.ServiceInfo;
import com.tencent.polaris.api.rpc.GetInstancesRequest;
import com.tencent.polaris.api.rpc.GetOneInstanceRequest;
import com.tencent.polaris.api.rpc.InstanceRegisterRequest;
import com.tencent.polaris.api.rpc.InstanceRegisterResponse;
import com.tencent.polaris.api.rpc.InstancesResponse;
import com.tencent.polaris.factory.api.DiscoveryAPIFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * 1.使用场景 test_service服务下，有5个实例，3个实例部署了version 1.0的应用，2个实例部署了version 2.0的应用， 需要保证只有灰度用户才能请求到version 2.0的应用。
 * <p>
 * 2.添加路由规则 路由规则中声明，带有灰度标签(grey=true)的请求，路由到version 2.0的实例分组，否则路由到version 1.0的实例分组。
 * <p>
 * curl -XPOST -H'Content-Type:application/json' -d @grep.json 'http://127.0.0.1:8090/naming/v1/routings'
 */
public class GrepRuleTest {

    public static void main(String[] args) {
        ProviderAPI providerAPI = DiscoveryAPIFactory.createProviderAPI();

        // 3.添加不同分组的多个实例
        // 注册version 1.0的服务实例
        for (int i = 0; i < 3; i++) {
            InstanceRegisterRequest request = new InstanceRegisterRequest();
            request.setNamespace("default");
            request.setService("test_service");
            request.setHost("127.0.0.1");
            request.setPort(12390 + i);
            request.setVersion("1.0");
            InstanceRegisterResponse instanceRegisterResponse = providerAPI.register(request);
        }

        // 注册version 2.0的服务实例
        for (int i = 0; i < 2; i++) {
            InstanceRegisterRequest request = new InstanceRegisterRequest();
            request.setNamespace("default");
            request.setService("test_service");
            request.setHost("127.0.0.1");
            request.setPort(12370 + i);
            request.setVersion("2.0");
            InstanceRegisterResponse instanceRegisterResponse = providerAPI.register(request);
        }

        // 拉取经过路由和负载均衡后的单个实例
        ConsumerAPI consumerAPI = DiscoveryAPIFactory.createConsumerAPI();
        GetOneInstanceRequest request = new GetOneInstanceRequest();

        ServiceInfo serviceInfo = new ServiceInfo();
        Map<String, String> metadata = new HashMap<String, String>();
        metadata.put("grep", "true");
        serviceInfo.setMetadata(metadata);
        request.setServiceInfo(serviceInfo);

        request.setNamespace("default");
        request.setService("test_service");

        InstancesResponse instancesResponse = consumerAPI.getOneInstance(request);
        // 服务实例信息
        for (Instance instance : instancesResponse.getInstances()) {
            System.out.printf("selected instance is %s:%d%n", instance.getHost(), instance.getPort());
        }

        consumerAPI.destroy();
    }

    public static void test() {
        // 拉取经过路由和负载均衡后的多个实例
        ConsumerAPI consumerAPI = DiscoveryAPIFactory.createConsumerAPI();
        GetInstancesRequest request = new GetInstancesRequest();

        // todo bug
//        ServiceInfo serviceInfo = new ServiceInfo();
//        Map<String, String> metadata = new HashMap<String, String>();
//        metadata.put("grep", "true");
//        serviceInfo.setMetadata(metadata);
//        request.setServiceInfo(serviceInfo);

        request.setNamespace("default");
        request.setService("test_service");

        InstancesResponse instancesResponse = consumerAPI.getInstances(request);
        // 服务实例信息
        for (Instance instance : instancesResponse.getInstances()) {
            System.out.printf("selected instance is %s:%d%n", instance.getHost(), instance.getPort());
        }

        consumerAPI.destroy();
    }
}