package com.example.gateway;

import com.example.gateway.config.GrayLoadBalancerClientAutoConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;

/**
 * @description: 通过自定义默认的负载均衡器实现灰度发布
 * @author: kiwi
 * @date: 2023/3/28
 **/
@SpringBootApplication
@EnableDiscoveryClient
@LoadBalancerClients(defaultConfiguration = {GrayLoadBalancerClientAutoConfig.class})
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
