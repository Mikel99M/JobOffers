package com.joboffers.domain.offer;

import org.springframework.data.jpa.repository.JpaRepository;

interface OfferRepository extends JpaRepository<Offer, Long> {
}
