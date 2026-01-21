package com.joboffers.domain.offer;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record OfferResponseDto(

        String id,
        String title,
        String description,
        String company,
        String offerUrl,
        LocalDateTime publicationDate,
        boolean isActive

) {
}
