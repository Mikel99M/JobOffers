package com.joboffers.infrastracture.offer.http;


import com.joboffers.domain.offer.JobOfferResponse;
import com.joboffers.domain.offer.OfferFetchable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Component
public class OfferFetcherRestTemplate implements OfferFetchable {

    private final RestTemplate restTemplate;

    @Override
    public List<JobOfferResponse> fetchOffers() {
        log.info("Fetching job offers from external service...");

        String url = UriComponentsBuilder
                .fromHttpUrl("http://ec2-3-127-218-34.eu-central-1.compute.amazonaws.com:5057/offers")
                .toUriString();

        try {
            ResponseEntity<List<JobOfferResponse>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<List<JobOfferResponse>>() {
                    }
            );

            List<JobOfferResponse> offers = response.getBody();

            if (!offers.isEmpty()) {
                log.info("Found {} job offers", offers.size());
                return offers;
            } else {
                log.info("No job offers found");
                return Collections.emptyList();
            }

        } catch (ResourceAccessException e) {
            log.error("Error while connecting to the offers service: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Offer service is down");
        }
    }
}
