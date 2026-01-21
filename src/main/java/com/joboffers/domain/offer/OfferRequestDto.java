package com.joboffers.domain.offer;

public record OfferRequestDto(

        String title,
        String description,
        String offerUrl,
        String company

) {
}
