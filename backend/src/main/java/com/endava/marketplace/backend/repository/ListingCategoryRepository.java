package com.endava.marketplace.backend.repository;

import com.endava.marketplace.backend.model.ListingCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListingCategoryRepository extends JpaRepository<ListingCategory, Long> {

    List<ListingCategory> findAllByOrderByNameAsc();

    List<ListingCategory> findAllByActiveIsTrueOrderByNameAsc();
}
