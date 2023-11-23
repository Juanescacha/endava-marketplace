package com.endava.marketplace.backend.service;

import com.endava.marketplace.backend.model.ListingStatus;
import com.endava.marketplace.backend.repository.ListingStatusRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.PostLoad;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ListingStatusService {
    private final ListingStatusRepository listingStatusRepository;

    @Getter
    private Map<String, ListingStatus> listingStatuses;

    public ListingStatusService(ListingStatusRepository listingStatusRepository) {
        this.listingStatusRepository = listingStatusRepository;
    }

    @PostConstruct
    @PostLoad
    private void getAllListingStatuses() {
        this.listingStatuses = listingStatusRepository.findAll().stream()
                .collect(Collectors.toMap(ListingStatus::getName, Function.identity()));
    }
}
