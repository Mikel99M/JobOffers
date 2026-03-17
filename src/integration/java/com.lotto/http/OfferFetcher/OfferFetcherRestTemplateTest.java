package com.lotto.http.OfferFetcher;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.joboffers.domain.offer.JobOfferResponse;
import com.joboffers.domain.offer.OfferFetchable;
import com.joboffers.infrastracture.offer.http.HttpClientConfigProperties;
import com.joboffers.infrastracture.offer.http.RestTemplateResponseErrorHandler;
import com.lotto.SampleOfJobResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OfferFetcherRestTemplateTest implements SampleOfJobResponse {

    @RegisterExtension
    public static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    HttpClientConfigProperties properties = new HttpClientConfigProperties(
            "http://localhost",
            wireMockServer.getPort()
    );

    OfferFetchable offerFetcher = new OfferFetcherRestTemplateTestConfig()
            .offerFetcher(
                    1000L,
                    1000L,
                    new RestTemplateResponseErrorHandler(),
                    properties
            );

    @Test
    void should_return_offers_when_external_service_returns_200() {
        // given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(bodyWithFourOffersJson())
                ));

        // when
        List<JobOfferResponse> response = offerFetcher.fetchOffers();

        // then
        assertThat(response).hasSize(4);
        assertThat(response.get(0).title()).isEqualTo("Junior Java Developer NOWA");
    }

    @Test
    void should_throw_service_unavailable_when_external_service_returns_404() {
        //  then
        assertThatThrownBy(() -> offerFetcher.fetchOffers())
        .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.SERVICE_UNAVAILABLE)
                .hasMessageContaining("External offer service failed");
    }

}
