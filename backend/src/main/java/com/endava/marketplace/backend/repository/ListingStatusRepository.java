package com.endava.marketplace.backend.repository;

import com.endava.marketplace.backend.model.ListingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ListingStatusRepository extends JpaRepository<ListingStatus, Long> {
    Optional<ListingStatus> findListingStatusByNameEqualsIgnoreCase(String listingStatusName);
}
