package com.parimal.e_wholesaler.api_gateway.filters;

import com.parimal.e_wholesaler.api_gateway.services.JwtService;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.ConfigClass> implements Ordered {

    private final JwtService jwtService;

    private final Map<String, List<String>> roleToPathMap = Map.of(
            "OWNER", List.of("/shops/", "/sales/", "/products/", "/orders/", "/workers/"),
            "WORKER", List.of("/workers/"),
            "CUSTOMER", List.of("/customers/")
    );

    public AuthenticationFilter(JwtService jwtService) {
        super(ConfigClass.class);
        this.jwtService = jwtService;
    }

    @Override
    public GatewayFilter apply(ConfigClass config) {
        return (exchange, chain) -> {
            String requestPath = exchange.getRequest().getPath().value();

            if(requestPath.startsWith("/users/")) {
                String userServiceTransactionToken = jwtService.generateUserServiceTransactionToken();
                exchange.getRequest()
                        .mutate()
                        .header("X-Transaction-Token", userServiceTransactionToken);
                return chain.filter(exchange);
            }

            String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
            if(authHeader == null) return completeResponse(exchange);

            String accessToken = authHeader.split("Bearer ")[1];
            Claims claims = jwtService.getClaimsFromToken(accessToken);
            String role = (String) claims.get("user_type");
            if(!isAuthorized(requestPath, role)) return completeResponse(exchange);

            String transactionToken = jwtService.generateTransactionToken(Long.parseLong(claims.getSubject()), claims);
            exchange.getRequest()
                    .mutate()
                    .header("X-Transaction-Token", transactionToken);
            return chain.filter(exchange);
        };
    }

    private boolean isAuthorized(String requestPath, String role) {
        return !roleToPathMap.get(role)
                .stream()
                .filter(requestPath::startsWith)
                .toList()
                .isEmpty();
    }

    private Mono<Void> completeResponse(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return 2;
    }


    public static class ConfigClass { }

}
