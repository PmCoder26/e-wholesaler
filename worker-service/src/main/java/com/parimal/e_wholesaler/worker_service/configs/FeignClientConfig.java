package com.parimal.e_wholesaler.worker_service.configs;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Enumeration;
import java.util.Objects;

@Configuration
@Slf4j
public class FeignClientConfig {

    @Bean
    public RequestInterceptor headerPropagationInterceptor() {
        return requestTemplate -> {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes instanceof ServletRequestAttributes) {
                HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
                String transactionToken = request.getHeader("Transaction-Token");
                // Here, use the small case in adding the headers.
                // Feign generally follows the convention of sending headers in lowercase
                requestTemplate.header("transaction-token", transactionToken);
            }
        };
    }

}
