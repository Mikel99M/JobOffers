package com.joboffers.infrastructure.offer.http;

import com.joboffers.domain.offer.JobOfferResponse;
import com.joboffers.domain.offer.OfferFetchable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Component
public class OfferFetcherRestTemplate implements OfferFetchable {

    private final RestTemplate restTemplate;
    private final HttpClientConfigProperties properties;

    @Override
    public List<JobOfferResponse> fetchOffers() {
        log.info("Fetching job offers from external service...");

        String url = getUrlForService();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set(HttpHeaders.CONNECTION, "close");

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<List<JobOfferResponse>> response = restTemplate.exchange(
                    url, HttpMethod.GET, requestEntity,
                    new ParameterizedTypeReference<>() {
                    });

            List<JobOfferResponse> offers = response.getBody();
            log.info("Successfully fetched {} offers", offers != null ? offers.size() : 0);
            return offers != null ? offers : Collections.emptyList();

        } catch (RestClientException e) {
            log.error("Error while fetching offers from external service at {}: {}", url, e.getMessage());
            throw e;
        }
    }

    private String getUrlForService() {
        return UriComponentsBuilder
                .fromHttpUrl(properties.uri()) // http://ec2-3-127-218-34.eu-central-1.compute.amazonaws.com
                .port(properties.port())       // 5057
                .path("/offers")               // /offers
                .toUriString();
    }
}
