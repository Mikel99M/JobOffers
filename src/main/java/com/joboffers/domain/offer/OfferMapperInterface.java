package com.joboffers.domain.offer;

public interface OfferMapperInterface {

    Offer mapOfferRequestDtoToOffer(final OfferRequestDto offerDto);

    OfferResponseDto mapOfferToOfferResponseDto(final Offer offer);

    Offer mapJobOfferResponseToOfferEntity(final JobOfferResponse response);
}
