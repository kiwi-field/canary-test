package net.trueland.cdp.sms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

/**
 * @创建人:kiwi
 * @创建时间:2023/3/29
 * @描述:
 */
@RestController
@RequestMapping("/serviceA")
public class TestCallServiceAController {
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/call")
    public String testCall(HttpServletRequest request){
        System.out.println("cal方法线程id"+ Thread.currentThread().getId());
        System.out.println("call方法hashcode" + request.hashCode());

        return restTemplate.getForObject("http://service-A/test/serviceA-test",String.class);
    }
}
