package com.endava.marketplace.backend.service;

import com.endava.marketplace.backend.model.ListingStatus;
import com.endava.marketplace.backend.repository.ListingStatusRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ListingStatusService {
    private final ListingStatusRepository listingStatusRepository;

    public ListingStatusService(ListingStatusRepository listingStatusRepository) {
        this.listingStatusRepository = listingStatusRepository;
    }

    public Optional<ListingStatus> findListingStatusByName(String listingStatusName) {
        return listingStatusRepository.findListingStatusByNameEqualsIgnoreCase(listingStatusName);
    }
}
