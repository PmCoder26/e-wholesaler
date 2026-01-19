package com.parimal.e_wholesaler.order_service.configs;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor headerPropagationInterceptor() {
        return requestTemplate -> {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes instanceof ServletRequestAttributes) {
                String transactionToken = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
                // Here, use the small case in adding the headers.
                // Feign generally follows the convention of sending headers in lowercase
                requestTemplate.header("X-Transaction-Token", transactionToken);
            }
        };
    }

}
