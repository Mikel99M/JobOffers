package com.joboffers.domain.offer;

public class OfferNotFoundException extends RuntimeException {
    public OfferNotFoundException(final String message) {
        super(message);
    }
}
