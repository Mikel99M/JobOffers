package com.joboffers.domain.offer;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@AllArgsConstructor
class OfferService {

    private final OfferFetchable offerFetcher;
    private final OfferMapperInterface offerMapper;
    private final OfferRepository offerRepository;

    List<Offer> fetchAllOffersAndSaveIfNotExists() {

        try {
            List<Offer> uniqueOffers = fetchOffersFromClient().stream()
                    .filter(offer -> !offerRepository.existsByOfferUrl(offer.getOfferUrl()))
                    .peek(offer -> offer.setActive(true))
                    .toList();

            offerRepository.saveAll(uniqueOffers);
        } catch (ResourceAccessException e) {
            log.error("Problem with connection or timeout: {}", e.getMessage());
        } catch (RestClientException e) {
            log.warn("External service unreachable. Proceeding with database-only offers.");
        }

        return offerRepository.findAll();
    }

    List<Offer> fetchOffersFromClient() {
        return offerFetcher.fetchOffers().stream()
                .map(offerMapper::mapJobOfferResponseToOfferEntity)
                .collect(Collectors.toList());
    }
}
