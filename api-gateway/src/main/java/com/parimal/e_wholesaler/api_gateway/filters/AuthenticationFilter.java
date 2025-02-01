package com.parimal.e_wholesaler.api_gateway.filters;

import com.parimal.e_wholesaler.api_gateway.services.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static reactor.core.publisher.Mono.fromRunnable;

@Component
@Slf4j
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.ConfigClass> {

    private final JwtService jwtService;

    private final Map<String, List<String>> roleToPathMap = Map.of(
            "OWNER", List.of("/shops/", "/sales/"),
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
            log.info("Inside the Authentication Filter 'PRE' with request path: {}", exchange.getRequest().getPath());
            String requestPath = exchange.getRequest().getPath().value();
            if(requestPath.startsWith("/users/")) return chain.filter(exchange);

            String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
            if(authHeader == null) return completeResponse(exchange);

            String accessToken = authHeader.split("Bearer ")[1];
            Claims claims = jwtService.getClaimsFromToken(accessToken);
            String role = (String) claims.get("roles");
            if(!isAuthorized(requestPath, role)) return completeResponse(exchange);
            log.info("Before creating transaction token");
            String transactionToken = jwtService.generateTransactionToken(Long.parseLong(claims.getSubject()));
            exchange.getRequest()
                    .mutate()
                    .header("Transaction-Token", transactionToken);
            return chain
                    .filter(exchange)
                    .then(fromRunnable(() -> {
                        log.info("Inside the Authentication Filter 'POST' with response: {}", exchange.getResponse());
                    }));
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


    public static class ConfigClass { }

}
