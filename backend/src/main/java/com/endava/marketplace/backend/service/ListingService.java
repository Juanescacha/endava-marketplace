package com.endava.marketplace.backend.service;

import com.endava.marketplace.backend.model.Listing;
import com.endava.marketplace.backend.repository.ListingRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ListingService {
    private final ListingRepository listingRepository;

    public ListingService(ListingRepository listingRepository) {this.listingRepository = listingRepository;}

    public Listing saveListing(Listing listing) {return listingRepository.save(listing);}

    public Optional<Listing> findListingById(Integer listingId) {return listingRepository.findById(listingId);}

    public void deleteListingById(Integer listingId) {listingRepository.deleteById(listingId);}
}
