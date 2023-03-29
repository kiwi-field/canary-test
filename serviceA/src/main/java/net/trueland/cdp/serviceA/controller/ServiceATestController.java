package net.trueland.cdp.serviceA.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/test")
public class ServiceATestController {

    @Value("${server.port}")
    private String port;

    @GetMapping("/serviceA-test")
    public String test(HttpServletRequest request){

        return "serviceA-test:"+port;
    }

    @GetMapping("/serviceA-test2")
    public String test2(){

        return "serviceA-test2:"+port;
    }
    @GetMapping("/serviceA-test3")
    public String test3(){

        return "serviceA-test3:"+port;
    }


    @GetMapping("/serviceA-test4")
    public String test4(HttpServletRequest request){

        String token = request.getHeader("token");

        return "serviceA-test3:"+port+":"+token;
    }

    @GetMapping("/serviceA-test5")
    public String test5(HttpServletRequest request){

        String token = request.getHeader("Cookie");

        return "serviceA-test3:"+port+":"+token;
    }
}
