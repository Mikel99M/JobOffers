package com.lotto.http.OfferFetcher;

import com.joboffers.domain.offer.OfferFetchable;
import com.joboffers.infrastructure.offer.http.HttpClientConfigProperties;
import com.joboffers.infrastructure.offer.http.JobOffersClientConfig;
import com.joboffers.infrastructure.offer.http.OfferFetcherRestTemplate;
import com.joboffers.infrastructure.offer.http.RestTemplateResponseErrorHandler;
import org.springframework.web.client.RestTemplate;


public class OfferFetcherRestTemplateTestConfig extends JobOffersClientConfig {

    public OfferFetchable offerFetcher(Long connectionTimeout, Long readTimeout, RestTemplateResponseErrorHandler errorHandler, HttpClientConfigProperties properties) {
        RestTemplate restTemplate = restTemplate(connectionTimeout, readTimeout, errorHandler);
        return new OfferFetcherRestTemplate(restTemplate, properties);
    }

}
