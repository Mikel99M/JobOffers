package com.joboffers.domain.offer;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class OffersFacade {

    private final OfferRepository offerRepository;
    private final OfferMapperInterface offerMapper;
    private final OfferService offerService;

    public OfferResponseDto addOffer(OfferRequestDto offerDto) {
        Offer offer = offerMapper.mapOfferRequestDtoToOffer(offerDto);
        Offer savedOffer = offerRepository.save(offer);
        return offerMapper.mapOfferToOfferResponseDto(savedOffer);
    }

    public OfferResponseDto fetchOfferById(String id) {
        Offer offer = offerRepository.findById(id).orElseThrow(() -> new OfferNotFoundException("No offer found with id: " + id));
        return offerMapper.mapOfferToOfferResponseDto(offer);
    }

    public List<OfferResponseDto> fetchAllOffers() {
        List<Offer> offers = offerRepository.findAll();
        return offers.stream()
                .map(offerMapper::mapOfferToOfferResponseDto)
                .toList();
    }

    public void activateOrDeactivateOffer(String id) {
        Offer offer = offerRepository.findById(id).orElseThrow(() -> new OfferNotFoundException("No offer found with id: " + id));

        offer.setActive(!offer.isActive());
        offerRepository.save(offer);
    }

    public void deleteOfferById(String id) {
        offerRepository.deleteById(id);
    }

    public List<OfferResponseDto> fetchAllOffersAndSaveIfNotExists() {
        return offerService.fetchAllOffersAndSaveIfNotExists().stream()
                .map(offerMapper::mapOfferToOfferResponseDto)
                .collect(Collectors.toList());
    }

}
