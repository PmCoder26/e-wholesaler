package com.parimal.e_wholesaler.worker_service.filters;

import com.parimal.e_wholesaler.worker_service.services.JwtService;
import com.parimal.e_wholesaler.common.utils.PermissionMapping;
import com.parimal.e_wholesaler.common.enums.UserType;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Component
public class TransactionTokenFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    public TransactionTokenFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String transactionToken = request.getHeader("X-Transaction-Token");

        if (transactionToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Claims claims = jwtService.getClaimsFromToken(transactionToken);
            String subject = claims.getSubject();

            if (subject.equals("user_service_transaction_token")) {
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(claims, transactionToken, Set.of(new SimpleGrantedAuthority("ROLE_USER_SERVICE")));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
                return;
            }

            // Store data in SecurityContext
            String userTypeStr = claims.get("user_type", String.class);
            UserType userType = UserType.valueOf(userTypeStr);
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(claims, transactionToken, PermissionMapping.getAuthorities(userType));
            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            response.setStatus(HttpStatus.FORBIDDEN.value());
        }
    }
}
