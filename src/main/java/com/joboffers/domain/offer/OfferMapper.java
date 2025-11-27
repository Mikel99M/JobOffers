package com.joboffers.domain.offer;

import java.time.LocalDateTime;

class OfferMapper implements OfferMapperInterface {

    public Offer mapOfferRequestDtoToOffer(final OfferRequestDto offerDto) {
        return Offer.builder()
                .title(offerDto.title())
                .description(offerDto.description())
                .company(offerDto.company())
                .isActive(true)
                .publicationDate(LocalDateTime.now())
                .build();
    }

    public OfferResponseDto mapOfferToOfferResponseDto(final Offer offer) {
        return OfferResponseDto.builder()
                .id(offer.getId())
                .description(offer.getDescription())
                .title(offer.getTitle())
                .isActive(offer.isActive())
                .publicationDate(offer.getPublicationDate())
                .build();
    }
}
