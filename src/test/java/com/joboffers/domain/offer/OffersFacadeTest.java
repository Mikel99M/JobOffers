package com.joboffers.domain.offer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OffersFacadeTest {

    private OfferRepositoryStub repo;
    private OffersFacade facade;
    private OfferService service;

    @BeforeEach
    void setup() {
        repo = new OfferRepositoryStub();
        OfferMapperStub mapper = new OfferMapperStub();
        service = mock(OfferService.class);
        facade = new OffersFacade(repo, mapper, service);
    }

    @Test
    void should_Add_Offer() {
        // given
        OfferRequestDto dto = new OfferRequestDto(
                "Java Developer", "Backend", "url", "Google"
        );

        // when
        OfferResponseDto response = facade.addOffer(dto);

        // then
        Offer saved = repo.findById(response.id()).orElseThrow();
        assertAll(
                () -> assertThat(saved.getTitle()).isEqualTo("Java Developer"),
                () -> assertThat(saved.getOfferUrl()).isEqualTo("url"),
                () -> assertThat(saved.getCompany()).isEqualTo("Google")
        );

    }

    @Test
    void should_Fetch_Offer_By_Id() {
        // given
        Offer offer = new Offer();
        offer.setTitle("DevOps");
        offer.setDescription("Cloud");
        offer.setCompany("AWS");
        offer.setActive(true);
        offer.setPublicationDate(Instant.now());
        Offer saved = repo.save(offer);

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
    void should_Fetch_All_Offers() {
        // given
        Offer o1 = new Offer();
        o1.setTitle("Java");
        o1.setActive(true);
        o1.setPublicationDate(Instant.now());
        repo.save(o1);

        Offer o2 = new Offer();
        o2.setTitle("Python");
        o2.setActive(true);
        o2.setPublicationDate(Instant.now());
        repo.save(o2);

        // when
        List<OfferResponseDto> list = facade.fetchAllOffers();

        // then
        assertThat(list).hasSize(2);
        assertThat(list).extracting("title").contains("Java", "Python");
    }

    @Test
    void should_Toggle_Offer_Activation() {
        // given
        Offer offer = new Offer();
        offer.setTitle("QA");
        offer.setActive(true);
        offer.setPublicationDate(Instant.now());
        Offer saved = repo.save(offer);

        // when
        facade.activateOrDeactivateOffer(saved.getId());

        // then
        Offer updated = repo.findById(saved.getId()).orElseThrow();
        assertThat(updated.isActive()).isFalse();

        // toggle again
        facade.activateOrDeactivateOffer(saved.getId());
        Offer toggledSecond = repo.findById(saved.getId()).orElseThrow();
        assertThat(toggledSecond.isActive()).isTrue();
    }

    @Test
    void should_Throw_When_Toggling_On_Missing_Offer() {
        assertThatThrownBy(() -> facade.activateOrDeactivateOffer("123"))
                .isInstanceOf(OfferNotFoundException.class)
                .hasMessageContaining("123");
    }

    @Test
    void should_Delete_Offer_By_Id() {
        // given
        Offer offer = new Offer();
        offer.setTitle("Go");
        offer.setActive(true);
        offer.setPublicationDate(Instant.now());
        Offer saved = repo.save(offer);

        // when
        facade.deleteOfferById(saved.getId());

        // then
        assertThat(repo.findById(saved.getId())).isEmpty();
    }

    @Test
    void should_fetch_all_OfferResponseDtos_from_service() {
        // given
        Offer offer1 = new Offer();
        offer1.setTitle("title 1");
        offer1.setDescription("Cloud 1");

        Offer offer2 = new Offer();
        offer2.setTitle("title 2");
        offer2.setDescription("Cloud 2");

        Offer offer3 = new Offer();
        offer3.setTitle("title 3");
        offer3.setDescription("Cloud 3");

        when(service.fetchAllOffersAndSaveIfNotExists()).thenReturn(List.of(offer1, offer2, offer3));

        // when
        List<OfferResponseDto> result = facade.fetchAllOffersAndSaveIfNotExists();

        // then
        assertAll(
                () -> assertThat(result).hasSize(3),
                () -> {
                    assert result != null;
                    assertThat(result.get(0).title()).isEqualTo("title 1");
                },
                () -> {
                    assert result != null;
                    assertThat(result.get(2).description()).isEqualTo("Cloud 3");
                }
        );
    }

}