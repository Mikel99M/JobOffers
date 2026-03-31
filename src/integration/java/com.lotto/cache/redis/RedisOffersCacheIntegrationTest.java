package com.lotto.cache.redis;

import com.joboffers.JobOffersApplication;
import com.joboffers.domain.offer.OffersFacade;
import com.lotto.BaseIntegrationTest;
import com.lotto.SampleOfJobResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest(classes = JobOffersApplication.class, properties = {"spring.cache.type=redis", "spring.cache.type=none"})
public class RedisOffersCacheIntegrationTest extends BaseIntegrationTest implements SampleOfJobResponse {

    @Container
    private static final GenericContainer<?> REDIS;

    static {
        REDIS = new GenericContainer<>("redis").withExposedPorts(6379);
        REDIS.start();
    }

    @SpyBean
    OffersFacade offerFacade;
    @Autowired
    CacheManager cacheManager;

    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("offers.http.client.config.port", () -> wireMockServer.getPort());
        registry.add("offers.http.client.config.uri", () -> WIRE_MOCK_HOST);
        registry.add("spring.redis.port", REDIS::getFirstMappedPort);
        registry.add("spring.cache.type", () -> "redis");
        registry.add("spring.cache.redis.time-to-live", () -> "PT1S");
    }

    @Test
    public void should_save_offers_to_cache_and_then_invalidate_by_time_to_live() throws Exception {
        // step 1: create a stub for mock server
        createStubMockServer(bodyWithZeroOffersJson());

        // step 2: register, log in and get test user token
        String jwtToken = registerTestUserAndLogHimInAndGetHisToken();

        // step 3: should save to cache offers request
        // given && when
        mockMvc.perform(get("/offers")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // then
        verify(offerFacade, times(1)).fetchAllOffersAndSaveIfNotExists();
        assertThat(cacheManager.getCacheNames().contains("jobOffers")).isTrue();

        // step 4: cache should be invalidated
        // given && when && then
        await()
                .atMost(Duration.ofSeconds(15))
                .pollInterval(Duration.ofSeconds(1))
                .untilAsserted(() -> {
                            mockMvc.perform(get("/offers")
                                    .header("Authorization", "Bearer " + jwtToken)
                                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                            );
                            verify(offerFacade, atLeast(2)).fetchAllOffersAndSaveIfNotExists();
                        }
                );
    }

}