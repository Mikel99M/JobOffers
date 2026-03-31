package com.joboffers.domain.offer;

import lombok.Builder;

import java.io.Serializable;
import java.time.Instant;

@Builder
public record OfferResponseDto(

        String id,
        String title,
        String description,
        String company,
        String offerUrl,
        Instant publicationDate,
        boolean isActive

) implements Serializable {
}
