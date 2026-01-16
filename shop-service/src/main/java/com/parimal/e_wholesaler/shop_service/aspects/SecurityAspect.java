package com.parimal.e_wholesaler.shop_service.aspects;

import com.parimal.e_wholesaler.shop_service.exceptions.ForbiddenException;
import com.parimal.e_wholesaler.shop_service.services.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@AllArgsConstructor
public class SecurityAspect {

    private final JwtService jwtService;

    @Pointcut(value = "execution(public * com.parimal.e_wholesaler.shop_service.services..*(..)) && " +
            "!execution(* com.parimal.e_wholesaler.shop_service.services.JwtService.*(..))")
    public void servicesPointCut() {}

    @Before(value = "servicesPointCut()")
    public void jwtTokenCheck(JoinPoint joinPoint) {
        HttpServletRequest request = (HttpServletRequest) joinPoint.getArgs()[0];
        String transactionToken = request.getHeader("Transaction-Token");
        if(transactionToken == null) throw new ForbiddenException("Access Denied");
        Claims claims = jwtService.getClaimsFromToken(transactionToken);
    }


}
