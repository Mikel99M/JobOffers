package com.joboffers.infrastracture.offer;

import com.joboffers.domain.offer.OfferRequestDto;
import com.joboffers.domain.offer.OfferResponseDto;
import com.joboffers.domain.offer.OffersFacade;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/offers")
@AllArgsConstructor
public class OfferController {

    private final OffersFacade offersFacade;

    @GetMapping
    public ResponseEntity<List<OfferResponseDto>> getOffers() {

        List<OfferResponseDto> result = offersFacade.fetchAllOffersAndSaveIfNotExists();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfferResponseDto> fetchOfferById(@PathVariable String id) {
        OfferResponseDto response = offersFacade.fetchOfferById(id);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<OfferResponseDto> addNewOffer(@RequestBody OfferRequestDto request) {
        OfferResponseDto response = offersFacade.addOffer(request);
        return ResponseEntity.status(201).body(response);
    }

}
