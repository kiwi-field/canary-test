package net.trueland.cdp.sms.config;

import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * 配置自定义filter给spring管理
 *
 * @author hy
 * @date 2021/6/1
 */
public class GrayLoadBalancerClientAutoConfig {

    @Bean
    public ReactorServiceInstanceLoadBalancer grayReactiveLoadBalancerClientFilter(LoadBalancerClientFactory clientFactory
            , Environment environment) {
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        return new GrayLoadBalancer(
                clientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class), name);
    }

}

