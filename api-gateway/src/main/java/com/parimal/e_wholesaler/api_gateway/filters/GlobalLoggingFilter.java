package com.parimal.e_wholesaler.api_gateway.filters;

import lombok.extern.slf4j.Slf4j;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import static reactor.core.publisher.Mono.fromRunnable;

@Component
@Slf4j
public class GlobalLoggingFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Inside the global logging filter 'PRE' with request path: {}", exchange.getRequest().getPath());
        return chain
                .filter(exchange)
                .then(fromRunnable(() -> {
                    log.info("Inside the global logging filter 'POST' with response: {}", exchange.getResponse());
                }));
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
