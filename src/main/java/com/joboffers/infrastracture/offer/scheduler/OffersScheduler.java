package com.joboffers.infrastracture.offer.scheduler;

import com.joboffers.domain.offer.OfferResponseDto;
import com.joboffers.domain.offer.OffersFacade;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@Log4j2
public class OffersScheduler {

    private final OffersFacade offersFacade;

    @Scheduled(cron = "${offers.scheduler.request.delay}")
    public List<OfferResponseDto> fetchAllOffers() {
        log.info("Offers Scheduler started");
        List<OfferResponseDto> offers = offersFacade.fetchAllOffersAndSaveIfNotExists();
        log.info("Added {} offers", offers.size());
        return offers;
    }

}
