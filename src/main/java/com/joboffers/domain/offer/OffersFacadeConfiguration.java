package com.joboffers.domain.offer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OffersFacadeConfiguration {

    @Bean
    OfferMapperInterface offerMapper() {
        return new OfferMapper();
    }

    @Bean
    OfferService offerService(OfferFetchable offerFetcher, OfferRepository offerRepository, OfferMapperInterface offerMapper) {

        return new OfferService(offerFetcher, offerMapper, offerRepository);
    }

    @Bean
    OffersFacade offersFacade(
            OfferRepository offerRepository,
            OfferMapperInterface offerMapper,
            OfferFetchable offerFetcher,
            OfferService offerService
    ) {
        return new OffersFacade(
                offerRepository,
                offerMapper,
                offerFetcher,
                offerService
        );
    }
}
