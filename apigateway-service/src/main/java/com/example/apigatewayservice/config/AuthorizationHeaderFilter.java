package com.example.apigatewayservice.config;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    private final Environment env;

    public AuthorizationHeaderFilter(Environment env) {
        super(Config.class);
        this.env = env;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, HttpStatus.UNAUTHORIZED, "No Authorization header found");
            }

            String jwt = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            jwt = jwt.replace("Bearer", "");

            if(isJwtInvalid(jwt)) {
                return onError(exchange, HttpStatus.UNAUTHORIZED, "Invalid JWT token");
            }

            return chain.filter(exchange);
        };
    }

    private boolean isJwtInvalid(String jwt) {
        boolean resultFlag = false;
        try {
            String subject = Jwts.parser().setSigningKey(env.getProperty("token.secret"))
                    .parseClaimsJws(jwt).getBody()
                    .getSubject();
            if(subject == null || subject.isEmpty()) {
                resultFlag = true;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            resultFlag = true;
        }

        return resultFlag;
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus, String msg) {
        exchange.getResponse().setStatusCode(httpStatus);
        log.error(msg);
        return exchange.getResponse().setComplete();
    }

    public static class Config {

    }
}
