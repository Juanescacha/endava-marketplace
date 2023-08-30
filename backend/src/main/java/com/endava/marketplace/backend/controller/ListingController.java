package com.endava.marketplace.backend.controller;

import com.endava.marketplace.backend.model.Listing;
import com.endava.marketplace.backend.service.ListingService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/listings")
public class ListingController {
    private final ListingService listingService;

    public ListingController(ListingService listingService) {this.listingService = listingService;}

    @PostMapping("/post")
    public Listing postListing(@RequestBody Listing listing) {return listingService.saveListing(listing);}

    @GetMapping("/get/{id}")
    public Optional<Listing> getListingById(@PathVariable Integer id) {
        return listingService.findListingById(id);
    }

    @DeleteMapping("delete/{id}")
    public void deleteListingById(@PathVariable Integer id) {listingService.deleteListingById(id);}
}
