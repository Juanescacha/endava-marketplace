package com.endava.marketplace.backend.repository;

import com.endava.marketplace.backend.model.Listing;
import com.endava.marketplace.backend.model.ListingCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Integer> {
    Set<Listing> findTop5ByNameContainsIgnoreCaseOrderByIdDesc(String name);
    Page<Listing> findListingsByNameContainsIgnoreCaseOrderByIdDesc(String name, Pageable pageable);
    Page<Listing> findListingsByCategoryOrderByIdDesc(ListingCategory category, Pageable pageable);
    Page<Listing> findListingsByNameContainsIgnoreCaseAndCategoryOrderByIdDesc(String name, ListingCategory category, Pageable pageable);
}
