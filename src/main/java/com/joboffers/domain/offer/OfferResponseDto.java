package com.joboffers.domain.offer;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
record OfferResponseDto(

        Long id,
        String title,
        String description,
        String company,
        LocalDateTime publicationDate,
        boolean isActive

) {
}
