package net.trueland.cdp.sms.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @创建人:kiwi
 * @创建时间:2023/3/29
 * @描述:
 */
@FeignClient(value = "service-A")
public interface ServiceAFeign {

    @GetMapping("/test/serviceA-test")
    String testCall();
}
