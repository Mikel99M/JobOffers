package com.lotto.feature;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.joboffers.domain.ErrorMessageResponse;
import com.joboffers.domain.loginandregister.RegistrationResultDto;
import com.joboffers.domain.offer.OfferResponseDto;
import com.joboffers.infrastracture.loginandregister.dto.JwtResponseDto;
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
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HappyPathIntegrationTest extends BaseIntegrationTest implements SampleOfJobResponse {

    @Autowired
    OffersScheduler scheduler;

    @Autowired
    OfferFetcherRestTemplate offerClient;

    @Test
    void test_typical_scenario() throws Exception {
        // step 1: there are no offers in external HTTP server
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(bodyWithZeroOffersJson())
                ));

        // step 2: scheduler ran 1st time and made GET to external server and system added 0 offers to database
        // given & when
        List<OfferResponseDto> zeroOffers = scheduler.fetchAllOffers();

        //then
        assertThat(zeroOffers).isEmpty();

        //step 3: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned UNAUTHORIZED(401)
        // given & when
        ResultActions failedLoginRequest = mockMvc.perform(post("/token")
                .content("""
                        {
                        "username": "someUser",
                        "password": "somePassword"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        failedLoginRequest.andExpect(status().is(HttpStatus.UNAUTHORIZED.value()))
                .andExpect(content().json("""
                        {
                        "message": "Bad credentials",
                        "status": "UNAUTHORIZED"
                        }
                        """.trim()));

        //step 4: user made GET /offers with no jwt token and system returned UNAUTHORIZED(401)
        // given & when
        ResultActions failedGetOffersRequest = mockMvc.perform(get("/offers")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        failedGetOffersRequest.andExpect(status().is(HttpStatus.FORBIDDEN.value()));

        //step 5: user made POST /register with username=someUser, password=somePassword and system registered user with status CREATED(201)
        // given & when
        ResultActions registerAction = mockMvc.perform(post("/register")
                .content("""
                        {
                        "username": "someUser",
                        "password": "somePassword"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        registerAction.andExpect(status().is(HttpStatus.CREATED.value()));
        MvcResult mvcResultOfRegisterAction = registerAction.andReturn();
        String registerActionResultJson = mvcResultOfRegisterAction.getResponse().getContentAsString();
        RegistrationResultDto registrationResultDto = objectMapper.readValue(registerActionResultJson, RegistrationResultDto.class);
        assertAll(
                () -> assertThat(registrationResultDto.username()).isEqualTo("someUser"),
                () -> assertThat(registrationResultDto.created()).isTrue(),
                () -> assertThat(registrationResultDto.id()).isNotNull()
        );

        //step 6: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned OK(200) and jwttoken=AAAA.BBBB.CCC
        // given & when
        ResultActions successLoginRequest = mockMvc.perform(post("/token")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                        {
                        "username": "someUser",
                        "password": "somePassword"
                        }
                        """.trim())
        );

        // then
        MvcResult mvcResultOfLoginRequest = successLoginRequest.andReturn();
        String loginRequestResultJson = mvcResultOfLoginRequest.getResponse().getContentAsString();
        JwtResponseDto jwtResponse = objectMapper.readValue(loginRequestResultJson, JwtResponseDto.class);
        String token = jwtResponse.token();

        assertAll(
                () -> assertThat(jwtResponse.username()).isEqualTo("someUser"),
                () -> assertThat(token).matches(Pattern.compile("^([A-Za-z0-9-_=]+\\.)+([A-Za-z0-9-_=])+\\.?$"))
        );

        //step 7: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 0 offers
        // when
        ResultActions perform = mockMvc.perform(get("/offers")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        MvcResult mvcResult = perform.andExpect(status().isOk()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        List<OfferResponseDto> result = objectMapper.readValue(json, new TypeReference<List<OfferResponseDto>>() {
        });
        assertThat(result).isEmpty();

        //step 8: there are 4 new offers in external HTTP server
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(bodyWithFourOffersJson())
                ));

        //step 9: scheduler ran 2nd time and made GET to external server and system added 4 new offers
        // given & when
        List<OfferResponseDto> newOffers = scheduler.fetchAllOffers();

        // then
        assertThat(newOffers).hasSize(4);

        //step 10: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 4 offers
        // given
        ResultActions getOffersAction = mockMvc.perform(get("/offers")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
        );

        MvcResult getOffersActionResult = getOffersAction.andExpect(status().is(HttpStatus.OK.value())).andReturn();
        String getOffersActionResultJson = getOffersActionResult.getResponse().getContentAsString();
        List<OfferResponseDto> getOffersResult = objectMapper.readValue(getOffersActionResultJson, new TypeReference<List<OfferResponseDto>>() {
        });

        // then
        assertAll(
                () -> assertThat(getOffersResult.size()).isEqualTo(4),
                () -> assertThat(getOffersResult.get(0).title()).isEqualTo("Junior Java Developer NOWA"),
                () -> assertThat(getOffersResult.get(0).company()).isEqualTo("Netcompany Poland Sp. z o.o."),
                () -> assertThat(getOffersResult.get(0).offerUrl()).isEqualTo("https://nofluffjobs.com/pl/job/junior-java-developer-netcompany-poland-warsaw-3"),
                () -> assertThat(getOffersResult.get(3).offerUrl()).isEqualTo("https://nofluffjobs.com/pl/job/software-developer-brainworkz-warsaw"),
                () -> assertThat(getOffersResult.get(0).id()).isNotEqualTo(getOffersResult.get(1).id()),
                () -> assertThat(getOffersResult.get(2).id()).isNotEqualTo(getOffersResult.get(3).id()),
                () -> assertThat(getOffersResult.get(0).isActive()).isTrue(),
                () -> assertThat(getOffersResult.get(1).isActive()).isTrue(),
                () -> assertThat(getOffersResult.get(2).isActive()).isTrue(),
                () -> assertThat(getOffersResult.get(3).isActive()).isTrue()
        );

        String firstOfferId = getOffersResult.get(0).id();

        //step 11: user made GET /offers/9999 and system returned NOT_FOUND(404) with message “Offer with id 9999 not found”
        // given & when
        ResultActions perform1 = mockMvc.perform(get("/offers/9999")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());

        // then
        MvcResult mvcResultOfFetchingNonExistantOffer = perform1.andReturn();
        String json1 = mvcResultOfFetchingNonExistantOffer.getResponse().getContentAsString();
        ErrorMessageResponse errorMessageResponseResult = objectMapper.readValue(json1, ErrorMessageResponse.class);

        assertAll(
                () -> assertThat(errorMessageResponseResult.message()).isEqualTo("No offer found with id: 9999"),
                () -> assertThat(errorMessageResponseResult.status()).isEqualTo(HttpStatus.NOT_FOUND)
        );

        //step 12: user made GET /offers/{first offer id} and system returned OK(200) with offer
        // give & when
        ResultActions performGetOffersForFirstOffer = mockMvc.perform(get("/offers/%s".formatted(firstOfferId))
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        // then
        MvcResult mvcResultOfPerformGetOffersForFirstOffer = performGetOffersForFirstOffer.andReturn();
        String json2 = mvcResultOfPerformGetOffersForFirstOffer.getResponse().getContentAsString();
        OfferResponseDto offerResponseDto1 = objectMapper.readValue(json2, OfferResponseDto.class);

        assertAll(
                () -> assertThat(offerResponseDto1.id()).isEqualTo(firstOfferId),
                () -> assertThat(offerResponseDto1.title()).isEqualTo("Junior Java Developer NOWA")
        );

        //step 13: there are 2 new offers in external HTTP server
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(bodyWithTwoOffersJson())
                ));

        //step 14: scheduler ran 3rd time and made GET to external server and system added 2 new offers
        // given & when
        List<OfferResponseDto> twoNewOffers = scheduler.fetchAllOffers();

        //then
        assertThat(twoNewOffers).hasSize(6);

        //step 15: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 6 offers
        // given & when
        ResultActions performGetOfferAfterAdditionOfTwoNewOffers = mockMvc.perform(get("/offers")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        // then
        MvcResult mvcResultOfPerformGetOfferAfterAdditionOfTwoNewOffers = performGetOfferAfterAdditionOfTwoNewOffers.andReturn();
        String json3 = mvcResultOfPerformGetOfferAfterAdditionOfTwoNewOffers.getResponse().getContentAsString();
        List<OfferResponseDto> getOffersResult2 = objectMapper.readValue(json3, new TypeReference<List<OfferResponseDto>>() {
        });

        assertAll(
                () -> assertThat(getOffersResult2.size()).isEqualTo(6),
                () -> assertThat(getOffersResult2.get(0).title()).isEqualTo("Junior Java Developer NOWA"),
                () -> assertThat(getOffersResult2.get(4).title()).isEqualTo("AI Engineer"),
                () -> assertThat(getOffersResult2.get(5).title()).isEqualTo("Salesforce Developer - Force Academy"),
                () -> assertThat(getOffersResult2.get(4).offerUrl()).isEqualTo("https://nofluffjobs.com/pl/job/ai-engineer-strategy-warsaw"),
                () -> assertThat(getOffersResult2.get(3).id()).isNotEqualTo(getOffersResult2.get(1).id()),
                () -> assertThat(getOffersResult2.get(4).id()).isNotEqualTo(getOffersResult2.get(5).id()),
                () -> assertThat(getOffersResult2.get(4).isActive()).isTrue(),
                () -> assertThat(getOffersResult2.get(5).isActive()).isTrue()
        );

        //step 16: user made POST /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and offer as body and system returned CREATED(201) with saved offer
        // when
        ResultActions performPostOffer = mockMvc.perform(post("/offers")
                .header("Authorization", "Bearer " + token)
                .content("""
                        {
                          "title": "title 1",
                          "description": "description 1",
                          "offerUrl": "offerUrl 1",
                          "company": "company 1"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON
                ));

        // then
        MvcResult mvcResultOfPostOffer = performPostOffer.andExpect(status().isCreated()).andReturn();
        String postOfferJson = mvcResultOfPostOffer.getResponse().getContentAsString();
        OfferResponseDto offerResponseDto2 = objectMapper.readValue(postOfferJson, OfferResponseDto.class);
        assertAll(
                () -> assertThat(offerResponseDto2.title()).isEqualTo("title 1"),
                () -> assertThat(offerResponseDto2.description()).isEqualTo("description 1"),
                () -> assertThat(offerResponseDto2.company()).isEqualTo("company 1"),
                () -> assertThat(offerResponseDto2.offerUrl()).isEqualTo("offerUrl 1")
        );

        //step 17: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 7 offers
        // given & when
        ResultActions performGetOfferAfterAddingOneOffer = mockMvc.perform(get("/offers")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        // then
        MvcResult mvcResultOfPerformGetOfferAfterAddingOneOffer = performGetOfferAfterAddingOneOffer.andReturn();
        String json4 = mvcResultOfPerformGetOfferAfterAddingOneOffer.getResponse().getContentAsString();
        List<OfferResponseDto> getOffersResult3 = objectMapper.readValue(json4, new TypeReference<List<OfferResponseDto>>() {
        });

        assertAll(
                () -> assertThat(getOffersResult3.size()).isEqualTo(7),
                () -> assertThat(getOffersResult3.get(6).title()).isEqualTo("title 1"),
                () -> assertThat(getOffersResult3.get(6).company()).isEqualTo("company 1"),
                () -> assertThat(getOffersResult3.get(6).description()).isEqualTo("description 1"),
                () -> assertThat(getOffersResult3.get(4).offerUrl()).isEqualTo("https://nofluffjobs.com/pl/job/ai-engineer-strategy-warsaw"),
                () -> assertThat(getOffersResult3.get(6).id()).isNotEqualTo(getOffersResult3.get(4).id()),
                () -> assertThat(getOffersResult3.get(6).isActive()).isTrue()
        );

    }
}
