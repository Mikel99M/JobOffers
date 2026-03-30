package com.joboffers.infrastructure.offer.http;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@EnableConfigurationProperties(HttpClientConfigProperties.class)
@Configuration
public class JobOffersClientConfig {

    @Bean
    public RestTemplateResponseErrorHandler restTemplateResponseErrorHandler() {
        return new RestTemplateResponseErrorHandler();
    }

    @Bean
    public RestTemplate restTemplate(
            @Value("${offers.offers_finder.http.client.config.connectionTimeout:1000}") long connectionTimeout,
            @Value("${offers.offers_finder.http.client.config.readTimeout:1000}") long readTimeout,
            RestTemplateResponseErrorHandler restTemplateResponseErrorHandler) {

        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofMillis(connectionTimeout))
                .setReadTimeout(Duration.ofMillis(readTimeout))
                .errorHandler(restTemplateResponseErrorHandler)
                .build();
    }
}
