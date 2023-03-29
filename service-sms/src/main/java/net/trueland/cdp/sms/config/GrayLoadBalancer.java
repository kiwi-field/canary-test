package net.trueland.cdp.sms.config;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.SelectedInstanceCallback;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
public class GrayLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    final AtomicInteger position;

    private final String serviceId;

    private ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;


    public GrayLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceId) {
        this(serviceInstanceListSupplierProvider, serviceId, new Random().nextInt(1000));
    }
    public GrayLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider,
                            String serviceId, int seedPosition) {
        this.serviceId = serviceId;
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
        this.position = new AtomicInteger(seedPosition);
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        System.out.println("负载方法线程id"+ Thread.currentThread().getId());
        System.out.println("负载方法hashcode" + request.hashCode());
        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider
                .getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get(request).next()
                .map(serviceInstances -> this.processInstanceResponse(supplier, serviceInstances, request));
    }

    private Response<ServiceInstance> processInstanceResponse(ServiceInstanceListSupplier supplier,
                                                              List<ServiceInstance> serviceInstances, Request request) {
        Response<ServiceInstance> serviceInstanceResponse = getInstanceResponse(serviceInstances, request);
        if (supplier instanceof SelectedInstanceCallback && serviceInstanceResponse.hasServer()) {
            ((SelectedInstanceCallback) supplier).selectedServiceInstance(serviceInstanceResponse.getServer());
        }
        return serviceInstanceResponse;
    }

    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances, Request request) {
        if (instances.isEmpty()) {
            if (log.isWarnEnabled()) {
                log.warn("No servers available for service: " + serviceId);
            }
            return new EmptyResponse();
        }

        String name = Thread.currentThread().getName();
        System.out.println("负载均衡的线程" + Thread.currentThread());
        DefaultRequestContext defaultContext = (DefaultRequestContext)request.getContext();
        Object req = defaultContext.getClientRequest();

        Map<String,String> map = RibbonParameters.get();
        String version = "";
        if (map != null && map.containsKey("version")){
            version = map.get("version");
        }
        // todo 这里可以将灰度发布的规则配置到数据库里面，在这里进行判断
        if (StringUtils.isNotEmpty(version)) {
            for (ServiceInstance instance : instances) {
                if (StringUtils.equals(version, instance.getMetadata().get("version"))) {
                    log.info("命中的实例{}", JSONObject.toJSONString(instance));
                    return new DefaultResponse(instance);
                }
            }
            log.info("配置的版本号不存在");
            return new EmptyResponse();
        } else {
            log.info("没有传递版本号");
            List<ServiceInstance> noVersionInstances = instances.stream().filter(i -> StringUtils.isEmpty(i.getMetadata().get("version"))).collect(Collectors.toList());
            int pos = Math.abs(this.position.incrementAndGet());
            ServiceInstance instance = noVersionInstances.get(pos % noVersionInstances.size());

            return new DefaultResponse(instance);
        }

    }
}