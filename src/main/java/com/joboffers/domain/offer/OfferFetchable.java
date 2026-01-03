package com.joboffers.domain.offer;

import java.util.List;

public interface OfferFetchable {

    public List<JobOfferResponse> fetchOffers();
}
