package com.lotto.feature;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.joboffers.domain.ErrorMessageResponse;
import com.joboffers.domain.offer.OfferResponseDto;
import com.joboffers.infrastracture.offer.http.OfferFetcherRestTemplate;
import com.joboffers.infrastracture.offer.scheduler.OffersScheduler;
import com.lotto.BaseIntegrationTest;
import com.lotto.SampleOfJobResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HappyPathIntegrationTest extends BaseIntegrationTest implements SampleOfJobResponse {

    @Autowired
    OffersScheduler scheduler;

    @Autowired
    OfferFetcherRestTemplate offerClient;

    @Test
    void f() throws Exception {
        // step 1: there are no offers in external HTTP server
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(bodyWithFourOffersJson())
                ));


        // step 2: scheduler ran 1st time and made GET to external server and system added 0 offers to database
        // given & when
        List<OfferResponseDto> newOffers = scheduler.fetchAllOffers();

        //then
        assertThat(newOffers.size()).isEqualTo(4);

        //step 3: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned UNAUTHORIZED(401)

        //step 4: user made GET /offers with no jwt token and system returned UNAUTHORIZED(401)

        //step 5: user made POST /register with username=someUser, password=somePassword and system registered user with status CREATED(201)

        //step 6: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned OK(200) and jwttoken=AAAA.BBBB.CCC

        //step 7: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 0 offers
        // when
        ResultActions perform = mockMvc.perform(get("/offers")
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        MvcResult mvcResult = perform.andExpect(status().isOk()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        List<OfferResponseDto> result = objectMapper.readValue(json, new TypeReference<List<OfferResponseDto>>() {
        });

        assertAll(
                () -> assertThat(result.size()).isEqualTo(4),
                () -> assertThat(result.get(0).title()).isEqualTo("Junior Java Developer NOWA"),
                () -> assertThat(result.get(0).company()).isEqualTo("Netcompany Poland Sp. z o.o."),
                () -> assertThat(result.get(0).offerUrl()).isEqualTo("https://nofluffjobs.com/pl/job/junior-java-developer-netcompany-poland-warsaw-3"),
                () -> assertThat(result.get(3).offerUrl()).isEqualTo("https://nofluffjobs.com/pl/job/software-developer-brainworkz-warsaw")

        );

        //step 8: there are 2 new offers in external HTTP server

        //step 9: scheduler ran 2nd time and made GET to external server and system added 2 new offers with ids: 1000 and 2000 to database


        //step 10: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 2 offers with ids: 1000 and 2000

        //step 11: user made GET /offers/9999 and system returned NOT_FOUND(404) with message “Offer with id 9999 not found”
        // when
        ResultActions perform1 = mockMvc.perform(get("/offers/9999")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());

        // then
        MvcResult mvcResultOfFetchingNonExistantOffer = perform1.andReturn();
        String json1 = mvcResultOfFetchingNonExistantOffer.getResponse().getContentAsString();
        ErrorMessageResponse errorMessageResponseResult = objectMapper.readValue(json1, ErrorMessageResponse.class);

        assertAll(
                () -> assertThat(errorMessageResponseResult.message()).isEqualTo("No offer found with id: 9999"),
                () -> assertThat(errorMessageResponseResult.httpStatus()).isEqualTo(HttpStatus.NOT_FOUND)
        );

        // when


        //step 12: user made GET /offers/1000 and system returned OK(200) with offer

        //step 13: there are 2 new offers in external HTTP server

        //step 14: scheduler ran 3rd time and made GET to external server and system added 2 new offers with ids: 3000 and 4000 to database

        //step 15: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 4 offers with ids: 1000,2000, 3000 and 4000


        //step 16: user made POST /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and offer as body and system returned CREATED(201) with saved offer
        // when
        ResultActions performPostOffer = mockMvc.perform(post("/offers")
                .content("""
                        {
                          "title": "title 1",
                          "description": "description 1",
                          "company": "company 1"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON
                ));

        MvcResult mvcResultOfPostOffer = performPostOffer.andExpect(status().isCreated()).andReturn();
        String postOfferJson = mvcResultOfPostOffer.getResponse().getContentAsString();
        OfferResponseDto offerResponseDto = objectMapper.readValue(postOfferJson, OfferResponseDto.class);
        assertAll(
                () -> assertThat(offerResponseDto.title()).isEqualTo("title 1"),
                () -> assertThat(offerResponseDto.description()).isEqualTo("description 1"),
                () -> assertThat(offerResponseDto.company()).isEqualTo("company 1"),
                () -> assertThat(offerResponseDto.offerUrl()).isEqualTo(null)
        );


        // then

        //step 17: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 5 offers


    }
}
