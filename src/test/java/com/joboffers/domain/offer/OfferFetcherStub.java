package com.joboffers.domain.offer;

import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
class OfferFetcherStub implements OfferFetchable {

    private List<JobOfferResponse> remoteOffers = new ArrayList<>();

    @Override
    public List<JobOfferResponse> fetchOffers() {
        return remoteOffers;
    }

}
