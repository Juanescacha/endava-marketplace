package com.endava.marketplace.backend.service;

import com.endava.marketplace.backend.dto.ListingCategoryDTO;
import com.endava.marketplace.backend.dto.ActiveListingCategoryDTO;
import com.endava.marketplace.backend.event.ListingCategoryStatusChangedEvent;
import com.endava.marketplace.backend.exception.*;
import com.endava.marketplace.backend.mapper.ListingCategoryMapper;
import com.endava.marketplace.backend.model.ListingCategory;
import com.endava.marketplace.backend.repository.ListingCategoryRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ListingCategoryService {
    private final ApplicationEventPublisher eventPublisher;

    private final ListingCategoryRepository listingCategoryRepository;

    private final ListingCategoryMapper listingCategoryMapper;

    public ListingCategoryService(
            ApplicationEventPublisher eventPublisher,
            ListingCategoryRepository listingCategoryRepository,
            ListingCategoryMapper listingCategoryMapper
    ) {
        this.eventPublisher = eventPublisher;
        this.listingCategoryRepository = listingCategoryRepository;
        this.listingCategoryMapper = listingCategoryMapper;
    }

    public ListingCategoryDTO saveListingCategory(String name) {
        String categoryName = capitalizeListingCategoryName(name);

        if (listingCategoryRepository
                .findAll()
                .stream()
                .anyMatch(lc -> lc.getName().equals(categoryName))
        ) {
            throw new EntityAlreadyExistsException("Listing Category '" + categoryName + "' already exists");
        }

        ListingCategory listingCategory = new ListingCategory();
        listingCategory.setName((categoryName));
        return listingCategoryMapper.toListingCategoryDTO(listingCategoryRepository.save(listingCategory));
    }

    public List<ListingCategoryDTO> fetchAllListingCategories() {
        return listingCategoryMapper.toListingCategoryDTOList(listingCategoryRepository.findAllByOrderByNameAsc());
    }

    public List<ActiveListingCategoryDTO> fetchAllActiveListingCategories() {
        return listingCategoryMapper.toActiveListingCategoryDTOList(listingCategoryRepository.findAllByActiveIsTrueOrderByNameAsc());
    }

    @Transactional
    public ListingCategoryDTO updateListingCategoryActiveStatus(Long id, Boolean newActiveStatus) {
        Optional<ListingCategory> foundListingCategory = listingCategoryRepository.findById(id);

        if (foundListingCategory.isEmpty()) {
            throw new EntityNotFoundException("Listing Category with id " + id + " wasn't found");
        }

        ListingCategory listingCategory = foundListingCategory.get();

        if ((listingCategory.getActive().equals(newActiveStatus))) {
            throw new EntityAttributeAlreadySetException("Active status of Listing Category with id " + id + " is already set to " + newActiveStatus);
        }

        listingCategory.setActive(newActiveStatus);
        eventPublisher.publishEvent(new ListingCategoryStatusChangedEvent(this, listingCategory));

        return listingCategoryMapper.toListingCategoryDTO(listingCategoryRepository.save(listingCategory));
    }

    @Transactional
    public ListingCategoryDTO updateListingCategoryName(Long id, String name) {

        Optional<ListingCategory> foundListingCategory = listingCategoryRepository.findById(id);

        if (foundListingCategory.isEmpty()) {
            throw new EntityNotFoundException("Listing Category with id " + id + " wasn't found");
        }

        ListingCategory listingCategory = foundListingCategory.get();
        String oldListingCategoryName = listingCategory.getName();
        String newListingCategoryName = capitalizeListingCategoryName(name);

        if (newListingCategoryName.equals(oldListingCategoryName)) {
            throw new EntityAttributeAlreadySetException("Listing Category with id " + id + " is already named '" + newListingCategoryName + "'");
        }

        if (listingCategoryRepository.findAll()
                .stream()
                .anyMatch(lc -> lc.getName().equals(newListingCategoryName))
        ) {
            throw new EntityAlreadyExistsException("There's already a Listing Category named '" + newListingCategoryName + "'");
        }

        listingCategory.setName(newListingCategoryName);
        return listingCategoryMapper.toListingCategoryDTO(listingCategoryRepository.save(listingCategory));
    }

    private String capitalizeListingCategoryName(String name) {
        return Arrays.stream(name.toLowerCase().split("\\s+"))
                .map(StringUtils::capitalize)
                .collect(Collectors.joining(" "));
    }

    protected ListingCategory loadListingCategory(Long id) {
        Optional<ListingCategory> listingCategory = listingCategoryRepository.findById(id);
        if(listingCategory.isEmpty()) {
            throw new EntityNotFoundException("Listing Category with id " + id + " wasn't found");
        }
        return listingCategory.get();
    }
}
