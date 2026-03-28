package com.joboffers.domain.offer;

import java.time.Instant;

class OfferMapper implements OfferMapperInterface {

    public Offer mapOfferRequestDtoToOffer(final OfferRequestDto offerDto) {
        return Offer.builder()
                .title(offerDto.title())
                .offerUrl(offerDto.offerUrl())
                .description(offerDto.description())
                .company(offerDto.company())
                .isActive(true)
                .publicationDate(Instant.now())
                .build();
    }

    public OfferResponseDto mapOfferToOfferResponseDto(final Offer offer) {
        return OfferResponseDto.builder()
                .id(offer.getId())
                .description(offer.getDescription())
                .company(offer.getCompany())
                .offerUrl(offer.getOfferUrl())
                .title(offer.getTitle())
                .isActive(offer.isActive())
                .publicationDate(offer.getPublicationDate())
                .build();
    }

    public Offer mapJobOfferResponseToOfferEntity(final JobOfferResponse response) {
        return Offer.builder()
                .company(response.company())
                .offerUrl(response.offerUrl())
                .salary(response.salary())
                .title(response.title())
                .build();
    }
}
