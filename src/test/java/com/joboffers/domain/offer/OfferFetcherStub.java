package com.joboffers.domain.offer;

import java.util.List;

class OfferFetcherStub implements OfferFetchable {
    @Override
    public List<JobOfferResponse> fetchOffers() {
        return List.of();
    }
}
