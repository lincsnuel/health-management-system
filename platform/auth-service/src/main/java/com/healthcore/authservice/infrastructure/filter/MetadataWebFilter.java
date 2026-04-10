package com.healthcore.authservice.infrastructure.filter;

import com.healthcore.authservice.common.context.RequestMetadata;
import org.jspecify.annotations.NullMarked;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1) // Run right after TenantWebFilter
public class MetadataWebFilter implements WebFilter {

    @Override
    @NullMarked
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        var headers = exchange.getRequest().getHeaders();

        RequestMetadata metadata = RequestMetadata.builder()
                .deviceId(headers.getFirst("X-Device-ID"))
                .deviceName(headers.getFirst("X-Device-Name"))
                .userAgent(headers.getFirst("User-Agent"))
                .ipAddress(Objects.requireNonNull(exchange.getRequest()
                        .getRemoteAddress()).getAddress().getHostAddress())
                .build();

        return chain.filter(exchange)
                .contextWrite(ctx -> ctx.put(RequestMetadata.METADATA_KEY, metadata));
    }
}