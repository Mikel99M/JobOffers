package com.joboffers.domain.offer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class OffersFacadeTest {

    private OfferRepositoryStub offerRepo;
    private OfferMapperStub mapper;
    private OfferFetcherStub fetcher;
    private OffersFacade facade;
    private OfferService offerService;

    @BeforeEach
    void setup() {
        offerRepo = new OfferRepositoryStub();
        mapper = new OfferMapperStub();
        fetcher = new OfferFetcherStub();
        facade = new OffersFacade(offerRepo, mapper, fetcher, offerService);
    }

    @Test
    void shouldAddOffer() {
        // given
        OfferRequestDto dto = new OfferRequestDto(
                "Java Developer", "Backend", "url", "Google"
        );

        // when
        OfferResponseDto response = facade.addOffer(dto);

        // then
        Offer saved = offerRepo.findById(response.id()).orElseThrow();
        assertAll(
                () -> assertThat(saved.getTitle()).isEqualTo("Java Developer"),
                () -> assertThat(saved.getOfferUrl()).isEqualTo("url"),
                () -> assertThat(saved.getCompany()).isEqualTo("Google")
        );

    }

    @Test
    void shouldFetchOfferById() {
        // given
        Offer offer = new Offer();
        offer.setTitle("DevOps");
        offer.setDescription("Cloud");
        offer.setCompany("AWS");
        offer.setActive(true);
        offer.setPublicationDate(LocalDateTime.now());
        Offer saved = offerRepo.save(offer);

        // when
        OfferResponseDto result = facade.fetchOfferById(saved.getId());

        // then
        assertThat(result.company()).isEqualTo("AWS");
    }

    @Test
    void shouldThrowWhenOfferNotFoundById() {
        assertThatThrownBy(() -> facade.fetchOfferById("999"))
                .isInstanceOf(OfferNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    void shouldFetchAllOffers() {
        // given
        Offer o1 = new Offer();
        o1.setTitle("Java");
        o1.setActive(true);
        o1.setPublicationDate(LocalDateTime.now());
        offerRepo.save(o1);

        Offer o2 = new Offer();
        o2.setTitle("Python");
        o2.setActive(true);
        o2.setPublicationDate(LocalDateTime.now());
        offerRepo.save(o2);

        // when
        List<OfferResponseDto> list = facade.fetchAllOffers();

        // then
        assertThat(list).hasSize(2);
        assertThat(list).extracting("title").contains("Java", "Python");
    }

    @Test
    void shouldToggleOfferActivation() {
        // given
        Offer offer = new Offer();
        offer.setTitle("QA");
        offer.setActive(true);
        offer.setPublicationDate(LocalDateTime.now());
        Offer saved = offerRepo.save(offer);

        // when
        facade.activateOrDeactivateOffer(saved.getId());

        // then
        Offer updated = offerRepo.findById(saved.getId()).orElseThrow();
        assertThat(updated.isActive()).isFalse();

        // toggle again
        facade.activateOrDeactivateOffer(saved.getId());
        Offer toggledSecond = offerRepo.findById(saved.getId()).orElseThrow();
        assertThat(toggledSecond.isActive()).isTrue();
    }

    @Test
    void shouldThrowWhenTogglingOnMissingOffer() {
        assertThatThrownBy(() -> facade.activateOrDeactivateOffer("123"))
                .isInstanceOf(OfferNotFoundException.class)
                .hasMessageContaining("123");
    }

    @Test
    void shouldDeleteOfferById() {
        // given
        Offer offer = new Offer();
        offer.setTitle("Go");
        offer.setActive(true);
        offer.setPublicationDate(LocalDateTime.now());
        Offer saved = offerRepo.save(offer);

        // when
        facade.deleteOfferById(saved.getId());

        // then
        assertThat(offerRepo.findById(saved.getId())).isEmpty();
    }

}