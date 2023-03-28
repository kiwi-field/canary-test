//package com.example.gateway.config;
//
//import com.alibaba.nacos.common.utils.StringUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//
//import reactor.core.publisher.Mono;
//
//@Component
//@Slf4j
//public class MyFilter implements Ordered,GlobalFilter {
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//
//        ServerHttpRequest request = exchange.getRequest();
//
//
//        return chain.filter(exchange);
//    }
//
//    @Override
//    public int getOrder() {
//        // TODO Auto-generated method stub
//        return -1;
//    }
//
//}