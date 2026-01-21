package com.joboffers.domain.offer;

import java.time.LocalDateTime;

class OfferMapperStub implements OfferMapperInterface {

    @Override
    public Offer mapOfferRequestDtoToOffer(OfferRequestDto dto) {
        Offer offer = new Offer();
        offer.setTitle(dto.title());
        offer.setDescription(dto.description());
        offer.setOfferUrl(dto.offerUrl());
        offer.setCompany(dto.company());
        offer.setActive(true);
        offer.setPublicationDate(LocalDateTime.now());
        return offer;
    }

    @Override
    public OfferResponseDto mapOfferToOfferResponseDto(Offer offer) {
        return OfferResponseDto.builder()
                .id(offer.getId())
                .title(offer.getTitle())
                .description(offer.getDescription())
                .company(offer.getCompany())
                .publicationDate(offer.getPublicationDate())
                .isActive(offer.isActive())
                .build();
    }

    @Override
    public Offer mapJobOfferResponseToOfferEntity(final JobOfferResponse response) {
        return null;
    }
}
