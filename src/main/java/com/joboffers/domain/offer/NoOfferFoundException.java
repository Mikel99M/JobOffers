package com.joboffers.domain.offer;

class NoOfferFoundException extends RuntimeException {
    public NoOfferFoundException(final String message) {
        super(message);
    }
}
