package com.joboffers.infrastracture.offer.http;

import com.joboffers.domain.offer.JobOfferResponse;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OfferFetcherRestTemplateTest {

    private OfferFetcherRestTemplate offerFetcherRestTemplate = new OfferFetcherRestTemplate(new RestTemplate());

    @Test
    public void test() {

        List<JobOfferResponse> result = offerFetcherRestTemplate.fetchOffers();

        System.out.println(result);

        for (JobOfferResponse jobOfferResponse : result) {
        }
    }

}