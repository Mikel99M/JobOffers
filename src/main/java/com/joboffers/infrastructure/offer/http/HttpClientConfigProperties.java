package com.joboffers.infrastructure.offer.http;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "offers.http.client.config")
public record HttpClientConfigProperties (
        String uri,
        int port
)

{}
