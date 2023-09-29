package com.endava.marketplace.backend.repository;

import com.endava.marketplace.backend.model.Listing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Integer>, JpaSpecificationExecutor<Listing> {
    Set<Listing> findTop5ByNameContainsIgnoreCaseOrderByIdDesc(String name);
}
