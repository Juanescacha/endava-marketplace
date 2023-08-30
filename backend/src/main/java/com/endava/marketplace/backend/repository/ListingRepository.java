package com.endava.marketplace.backend.repository;

import com.endava.marketplace.backend.model.Listing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Integer> {
}
