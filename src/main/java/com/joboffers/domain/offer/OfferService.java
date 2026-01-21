package com.joboffers.domain.offer;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
class OfferService {

    private final OfferFetchable offerFetcher;
    private final OfferMapperInterface offerMapper;
    private final OfferRepository offerRepository;

    List<Offer> fetchAllOffersAndSaveIfNotExists() {

        List<Offer> uniqueOffers = fetchOffersFromClient().stream()
                .filter(offer -> !offerRepository.existsByOfferUrl(offer.getOfferUrl()))
                .toList();

        offerRepository.saveAll(uniqueOffers);

        return offerRepository.findAll();
    }

    List<Offer> fetchOffersFromClient() {
        return offerFetcher.fetchOffers().stream()
                .map(offerMapper::mapJobOfferResponseToOfferEntity)
                .collect(Collectors.toList());
    }
}
