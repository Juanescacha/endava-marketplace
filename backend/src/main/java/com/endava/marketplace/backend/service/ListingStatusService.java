package com.endava.marketplace.backend.service;

import com.endava.marketplace.backend.model.ListingStatus;
import com.endava.marketplace.backend.repository.ListingStatusRepository;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ListingStatusService {
    private final ListingStatusRepository listingStatusRepository;

    @Getter
    private final List<ListingStatus> listingStatuses;

    public ListingStatusService(ListingStatusRepository listingStatusRepository) {
        this.listingStatusRepository = listingStatusRepository;

        listingStatuses = listingStatusRepository.findAll();
    }

    public Optional<ListingStatus> findListingStatusByName(String listingStatusName) {
        return listingStatusRepository.findListingStatusByNameEqualsIgnoreCase(listingStatusName);
    }
}
