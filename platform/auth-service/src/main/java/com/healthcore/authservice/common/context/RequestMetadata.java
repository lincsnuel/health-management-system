package com.healthcore.authservice.common.context;

import lombok.Builder;
import lombok.Getter;
import reactor.core.publisher.Mono;

@Getter
@Builder
public class RequestMetadata {
    private final String deviceId;
    private final String deviceName;
    private final String ipAddress;
    private final String userAgent;

    public static final String METADATA_KEY = "requestMetadata";

    public static Mono<RequestMetadata> get() {
        return Mono.deferContextual(ctx ->
                ctx.hasKey(METADATA_KEY) ? Mono.just(ctx.get(METADATA_KEY)) : Mono.empty()
        );
    }
}