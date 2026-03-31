package com.lotto.feature;

import com.jayway.jsonpath.JsonPath;
import com.joboffers.JobOffersApplication;
import com.joboffers.domain.offer.OfferResponseDto;
import com.joboffers.infrastructure.offer.scheduler.OffersScheduler;
import com.lotto.BaseIntegrationTest;
import com.lotto.SampleOfJobResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = JobOffersApplication.class, properties = {"spring.cache.type=none"})
public class HappyPathIntegrationTest extends BaseIntegrationTest implements SampleOfJobResponse {

    @Autowired
    OffersScheduler scheduler;

    @Test
    void test_typical_scenario() throws Exception {
        // step 1: there are no offers in external HTTP server
        createStubMockServer(bodyWithZeroOffersJson());

        // step 2: scheduler ran 1st time and made GET to external server and system added 0 offers to database
        // given & when
        List<OfferResponseDto> allOffers = scheduler.fetchAllOffers();

        //then
        assertThat(allOffers).isEmpty();

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
        //step 6: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned OK(200) and jwttoken=AAAA.BBBB.CCC
        // given & when
        String token = registerTestUserAndLogHimInAndGetHisToken();

        // then
        assertThat(token).matches(Pattern.compile("^([A-Za-z0-9-_=]+\\.)+([A-Za-z0-9-_=])+\\.?$"));

        //step 7: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 0 offers
        // when & then
        mockMvc.perform(get("/offers")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        //step 8: there are 4 new offers in external HTTP server
        createStubMockServer(bodyWithFourOffersJson());

        //step 9: scheduler ran 2nd time and made GET to external server and system added 4 new offers
        // given & when
        List<OfferResponseDto> allOffersAfterAddingFour = scheduler.fetchAllOffers();

        // then
        assertThat(allOffersAfterAddingFour).hasSize(4);

        //step 10: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 4 offers
        // when & then
        ResultActions getOffersAction = mockMvc.perform(get("/offers")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(4))
                .andExpect(jsonPath("$[0].title").value("Junior Java Developer NOWA"))
                .andExpect(jsonPath("$[0].company").value("Netcompany Poland Sp. z o.o."))
                .andExpect(jsonPath("$[0].offerUrl").value("https://nofluffjobs.com/pl/job/junior-java-developer-netcompany-poland-warsaw-3"))
                .andExpect(jsonPath("$[3].offerUrl").value("https://nofluffjobs.com/pl/job/software-developer-brainworkz-warsaw"))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[1].id").exists())
                .andExpect(jsonPath("$[*].isActive").value(everyItem(is(true))));

        MvcResult mvcResult = getOffersAction.andExpect(status().isOk()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();

        String firstOfferId = JsonPath.read(json, "$[0].id");

        //step 11: user made GET /offers/9999 and system returned NOT_FOUND(404) with message “Offer with id 9999 not found”
        // when & then
        mockMvc.perform(get("/offers/9999")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No offer found with id: 9999"))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"));

        //step 12: user made GET /offers/{first offer id} and system returned OK(200) with offer
        // when & then
        mockMvc.perform(get("/offers/%s".formatted(firstOfferId))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(firstOfferId))
                .andExpect(jsonPath("$.title").value("Junior Java Developer NOWA"));

        //step 13: there are 2 new offers in external HTTP server
        createStubMockServer(bodyWithTwoOffersJson());

        //step 14: scheduler ran 3rd time and made GET to external server and system added 2 new offers
        // given & when
        List<OfferResponseDto> allOffersAfterAddingTwo = scheduler.fetchAllOffers();

        //then
        assertThat(allOffersAfterAddingTwo).hasSize(6);

        //step 15: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 6 offers
        // when & then
        mockMvc.perform(get("/offers")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(6))
                .andExpect(jsonPath("$[0].title").value("Junior Java Developer NOWA"))
                .andExpect(jsonPath("$[4].title").value("AI Engineer"))
                .andExpect(jsonPath("$[5].title").value("Salesforce Developer - Force Academy"))
                .andExpect(jsonPath("$[4].offerUrl").value("https://nofluffjobs.com/pl/job/ai-engineer-strategy-warsaw"))
                .andExpect(jsonPath("$[4:6].isActive").value(everyItem(is(true))));

        //step 16: user made POST /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and offer as body and system returned CREATED(201) with saved offer
        // when
        mockMvc.perform(post("/offers")
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
                        ))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("title 1"))
                .andExpect(jsonPath("$.description").value("description 1"))
                .andExpect(jsonPath("$.company").value("company 1"))
                .andExpect(jsonPath("$.offerUrl").value("offerUrl 1"))
                .andExpect(jsonPath("$.id").exists());

        //step 17: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 7 offers
        // when & then
        mockMvc.perform(get("/offers")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(7))
                .andExpect(jsonPath("$[6].title").value("title 1"))
                .andExpect(jsonPath("$[6].company").value("company 1"))
                .andExpect(jsonPath("$[6].isActive").value(true));
    }
}
