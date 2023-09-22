package com.endava.marketplace.backend.service;

import com.endava.marketplace.backend.model.ListingCategory;
import com.endava.marketplace.backend.repository.ListingCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class ListingCategoryService {
    private final ListingCategoryRepository listingCategoryRepository;

    public ListingCategoryService(ListingCategoryRepository listingCategoryRepository) {this.listingCategoryRepository = listingCategoryRepository;}

    public ListingCategory saveListingCategory(ListingCategory listingCategory) {return listingCategoryRepository.save(listingCategory);}

    public Optional<ListingCategory> findListingCategoryById(Integer listingCategoryId) {return listingCategoryRepository.findById(listingCategoryId);}


    public void deleteListingCategoryById(Integer listingCategoryId) {listingCategoryRepository.deleteById(listingCategoryId);}
}
