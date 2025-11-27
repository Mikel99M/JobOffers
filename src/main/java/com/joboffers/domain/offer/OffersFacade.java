package com.joboffers.domain.offer;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class OffersFacade {

    private final OfferRepository offerRepository;
    private final OfferMapperInterface offerMapper;
    private final OfferFetcherInterface offerFetcher;

    public OfferResponseDto addOffer(OfferRequestDto offerDto) {

        Offer offer = offerMapper.mapOfferRequestDtoToOffer(offerDto);
        Offer savedOffer = offerRepository.save(offer);
        return offerMapper.mapOfferToOfferResponseDto(savedOffer);
    }

    public OfferResponseDto fetchOfferById(Long id) {
        Offer offer = offerRepository.findById(id).orElseThrow(() -> new NoOfferFoundException("No offer found with id: " + id));
        return offerMapper.mapOfferToOfferResponseDto(offer);
    }

    public List<OfferResponseDto> fetchAllOffers() {
        List<Offer> offers = offerRepository.findAll();
        return offers.stream()
                .map(offerMapper::mapOfferToOfferResponseDto)
                .toList();
    }

    public void activateOrDeactivateOffer(Long id) {
        Offer offer = offerRepository.findById(id).orElseThrow(() -> new NoOfferFoundException("No offer found with id: " + id));

        offer.setActive(!offer.isActive());
        offerRepository.save(offer);
    }

    public void deleteOfferById(Long id) {
        offerRepository.deleteById(id);
    }

    public void fetchAllOffersAndSaveIfNotExists() {

    }


}
