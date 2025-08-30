package com.parimal.e_wholesaler.user_service.interceptors;

import com.parimal.e_wholesaler.user_service.entities.UserEntity;
import com.parimal.e_wholesaler.user_service.services.JwtService;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@AllArgsConstructor
public class FeignInterceptor implements RequestInterceptor {

    private final JwtService jwtService;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(principal.getClass());
        System.out.println("Inside the interceptor");
        if(principal instanceof UserDetails) {
            UserEntity user = (UserEntity) principal;
            String transactionToken = jwtService.generateTransactionToken(user.getId());
            requestTemplate.header("Transaction-Token", transactionToken);
        }
    }

}
