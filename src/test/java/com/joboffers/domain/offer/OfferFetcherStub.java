package com.joboffers.domain.offer;

import lombok.Setter;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;

@Setter
class OfferFetcherStub implements OfferFetchable {

    private List<JobOfferResponse> remoteOffers = new ArrayList<>();
    private RestClientException exceptionToThrow;

    @Override
    public List<JobOfferResponse> fetchOffers() {
        if (exceptionToThrow != null) {
            throw exceptionToThrow;
        }
        return remoteOffers;
    }

    void setToThrow(RestClientException e) {
        this.exceptionToThrow = e;
    }

}
