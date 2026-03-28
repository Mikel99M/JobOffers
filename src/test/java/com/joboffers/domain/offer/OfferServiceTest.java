package com.joboffers.domain.offer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class OfferServiceTest {

    private OfferRepositoryStub repository;
    private OfferFetcherStub fetcher;
    private OfferService offerService;
    private final OfferMapperInterface mapper = new OfferMapper();

    @BeforeEach
    void setUp() {
        repository = new OfferRepositoryStub();
        fetcher = new OfferFetcherStub();
        offerService = new OfferService(fetcher, mapper, repository);
    }

    @Test
    void should_save_only_unique_offers_when_some_already_exist_in_database() {
        // given
        Offer existingOffer = Offer.builder().offerUrl("url1").build();
        repository.save(existingOffer);

        fetcher.setRemoteOffers(List.of(
                new JobOfferResponse("Title 1", "Comp 1", "1", "url2"),
                new JobOfferResponse("Title 2", "Comp 2", "2", "url3")
        ));

        // when
        List<Offer> result = offerService.fetchAllOffersAndSaveIfNotExists();

        // then
        assertThat(result).hasSize(3);
        assertThat(result.get(1).getOfferUrl()).isEqualTo("url2");
        assertThat(result.get(2).getOfferUrl()).isEqualTo("url3");
    }

    @Test
    void should_save_all_offers_when_database_is_empty() {
        // given
        fetcher.setRemoteOffers(List.of(
                new JobOfferResponse("Title 1", "Comp 1", "1", "url1"),
                new JobOfferResponse("Title 2", "Comp 2", "2", "url2")
        ));

        // when
        List<Offer> result = offerService.fetchAllOffersAndSaveIfNotExists();

        // then
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(Offer::isActive);
        assertThat(result.get(1).getOfferUrl()).isEqualTo("url2");
    }

    @Test
    void should_not_save_anything_if_all_fetched_offers_are_already_in_database() {
        // given
        repository.saveAll(List.of(
                Offer.builder().offerUrl("url1").build()
        ));
        fetcher.setRemoteOffers(List.of(
                new JobOfferResponse("Title 1", "Comp 1", "1", "url1")
        ));

        // when
        offerService.fetchAllOffersAndSaveIfNotExists();

        // then
        assertThat(repository.findAll()).hasSize(1);
    }
}
