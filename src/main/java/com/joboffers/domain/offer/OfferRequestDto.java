package com.joboffers.domain.offer;

import java.time.LocalDateTime;

record OfferRequestDto(

        String title,
        String description,
        String company

) {
}
