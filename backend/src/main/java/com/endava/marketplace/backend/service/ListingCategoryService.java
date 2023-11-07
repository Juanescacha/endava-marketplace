package com.endava.marketplace.backend.service;

import com.endava.marketplace.backend.exception.BlankListingCategory;
import com.endava.marketplace.backend.exception.ListingCategoryAlreadyExists;
import com.endava.marketplace.backend.model.ListingCategory;
import com.endava.marketplace.backend.repository.ListingCategoryRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ListingCategoryService {
    private final ListingCategoryRepository listingCategoryRepository;

    public ListingCategoryService(
            ListingCategoryRepository listingCategoryRepository
    ) {
        this.listingCategoryRepository = listingCategoryRepository;
    }

    public Map<String, String> saveListingCategory(String name) throws ListingCategoryAlreadyExists, BlankListingCategory {
        if(!name.isEmpty()) {
            String categoryName = Arrays.stream(name.split("\\s+"))
                    .map(StringUtils::capitalize)
                    .collect(Collectors.joining(" "));

            if(listingCategoryRepository.findAll()
                    .stream()
                    .noneMatch(lc -> lc.getName().equalsIgnoreCase(categoryName))
            ) {
                ListingCategory listingCategory = new ListingCategory();
                listingCategory.setName((categoryName));
                listingCategoryRepository.save(listingCategory);

                Map<String, String> success = new HashMap<>();
                success.put("success", "Listing Category '" + categoryName + "' was saved");
                return success;
            }
            else {
                throw new ListingCategoryAlreadyExists("Listing Category '" + categoryName + "' already exists");
            }
        }
        else {
            throw new BlankListingCategory("Listing Category name cannot be blank");
        }
    }

    public Optional<ListingCategory> findListingCategoryById(Long listingCategoryId) {
        return listingCategoryRepository.findById(listingCategoryId);
    }


    public void deleteListingCategoryById(Long listingCategoryId) {
        listingCategoryRepository.deleteById(listingCategoryId);
    }
}
