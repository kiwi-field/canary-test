package net.trueland.cdp.sms;

//import net.trueland.cdp.sms.config.GrayLoadBalancerClientAutoConfig;
import net.trueland.cdp.sms.config.GrayLoadBalancerClientAutoConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @description: 微服务-》微服务灰度发布
 * @author: kiwi
 * @date: 2023/3/29
 **/
@SpringBootApplication
@RestController
@EnableDiscoveryClient
@EnableFeignClients
@LoadBalancerClients(defaultConfiguration = {GrayLoadBalancerClientAutoConfig.class})
public class ServiceSmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceSmsApplication.class, args);
    }


    @LoadBalanced
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }


}
