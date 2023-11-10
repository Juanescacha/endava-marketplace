package com.endava.marketplace.backend.repository;

import com.endava.marketplace.backend.model.Listing;
import com.endava.marketplace.backend.model.ListingCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Long>, JpaSpecificationExecutor<Listing> {
    Set<Listing> findTop5ByNameContainsIgnoreCaseOrderByIdDesc(String name);

    List<Listing> findAllByCategory(ListingCategory listingCategory);
}
