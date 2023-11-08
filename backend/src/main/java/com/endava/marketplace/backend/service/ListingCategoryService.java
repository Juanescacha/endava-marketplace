package com.endava.marketplace.backend.service;

import com.endava.marketplace.backend.dto.ListingCategoryDTO;
import com.endava.marketplace.backend.exception.BlankListingCategoryName;
import com.endava.marketplace.backend.exception.ListingCategoryAlreadyExists;
import com.endava.marketplace.backend.mapper.ListingCategoryMapper;
import com.endava.marketplace.backend.model.ListingCategory;
import com.endava.marketplace.backend.repository.ListingCategoryRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ListingCategoryService {
    private final ListingCategoryRepository listingCategoryRepository;

    private final ListingCategoryMapper listingCategoryMapper;

    public ListingCategoryService(
            ListingCategoryRepository listingCategoryRepository,
            ListingCategoryMapper listingCategoryMapper
    ) {
        this.listingCategoryRepository = listingCategoryRepository;
        this.listingCategoryMapper = listingCategoryMapper;
    }

    public Map<String, String> saveListingCategory(String name) throws ListingCategoryAlreadyExists, BlankListingCategoryName {
        if(!name.isEmpty()) {
            String categoryName = capitalizeListingCategoryName(name);

            if(listingCategoryRepository.findAll()
                    .stream()
                    .noneMatch(lc -> lc.getName().equals(categoryName))
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
            throw new BlankListingCategoryName("Listing Category name cannot be blank");
        }
    }

    public Set<ListingCategoryDTO> fetchAllActiveListingCategories() {
        return listingCategoryMapper.toListingCategoryDTOSet(listingCategoryRepository.findAllByActiveIsTrueOrderByNameAsc());
    }

    @Transactional
    public Map<String, String> updateListingCategoryName(Long id, String name) throws
            NullPointerException, BlankListingCategoryName, ListingCategoryAlreadyExists {

        Optional<ListingCategory> foundListingCategory = listingCategoryRepository.findById(id);


        if(foundListingCategory.isPresent()) {
            ListingCategory listingCategory = foundListingCategory.get();
            String oldListingCategoryName = listingCategory.getName();

            if(!name.isEmpty()) {
                String newListingCategoryName = capitalizeListingCategoryName(name);

                if(listingCategoryRepository.findAll()
                        .stream()
                        .noneMatch(lc -> lc.getName().equals(newListingCategoryName))
                ) {
                    if(!newListingCategoryName.equals(oldListingCategoryName)) {
                        listingCategory.setName(newListingCategoryName);
                        listingCategoryRepository.save(listingCategory);

                        Map<String, String> success = new HashMap<>();
                        success.put(
                                "success",
                                "Listing Category with name '" + oldListingCategoryName + "' was changed to '" + newListingCategoryName + "'"
                        );
                        return success;
                    }
                    else {
                        throw new ListingCategoryAlreadyExists("Listing Category with id " + id + " is already named '" + newListingCategoryName + "'");
                    }
                }
                else {
                    throw new ListingCategoryAlreadyExists("There's already a Listing Category named '" + newListingCategoryName + "'");
                }
            }
            else {
                throw new BlankListingCategoryName("New Listing Category name cannot be blank");
            }
        }
        else {
            throw new NullPointerException("Listing Category with id " + id + " wasn't found");
        }
    }

    private String capitalizeListingCategoryName(String name) {
        return Arrays.stream(name.toLowerCase().split("\\s+"))
                .map(StringUtils::capitalize)
                .collect(Collectors.joining(" "));
    }
}
