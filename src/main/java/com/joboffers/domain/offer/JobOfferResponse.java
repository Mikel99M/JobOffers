package com.joboffers.domain.offer;

import lombok.Builder;

@Builder
public record JobOfferResponse(
        String title,
        String company,
        String salary,
        String offerUrl
) {
}
