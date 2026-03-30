package com.lotto.scheduler;

import com.joboffers.JobOffersApplication;
import com.joboffers.infrastructure.offer.http.OfferFetcherRestTemplate;
import com.lotto.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.Duration;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@SpringBootTest(classes = JobOffersApplication.class, properties = {
        "spring.task.scheduling.enabled=true",
        "offers.scheduler.request.delay=*/1 * * * * *"})
public class HttpOfferSchedulerTest extends BaseIntegrationTest {

    @SpyBean
    OfferFetcherRestTemplate remoteOfferClient;

    @Test
    public void should_run_http_client_offers_fetching_exactly_given_times() {
        await().
                atMost(Duration.ofSeconds(2))
                .untilAsserted(() -> verify(remoteOfferClient, times(2)).fetchOffers());
    }

}
