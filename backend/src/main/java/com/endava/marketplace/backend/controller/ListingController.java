package com.endava.marketplace.backend.controller;

import com.endava.marketplace.backend.model.Listing;
import com.endava.marketplace.backend.service.ListingService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/listings")
public class ListingController {
    private final ListingService listingService;

    public ListingController(ListingService listingService) {
        this.listingService = listingService;
    }

    @PostMapping("/post")
    public Listing postListing(@RequestBody Listing listing) {return listingService.saveListing(listing);}

    @GetMapping("/get/{id}")
    public Optional<Listing> getListingById(@PathVariable Integer id) {
        return listingService.findListingById(id);
    }

    @DeleteMapping("delete/{id}")
    public void deleteListingById(@PathVariable Integer id) {listingService.deleteListingById(id);}

    @PostMapping("/post/images/{id}")
    public void postListingImages(@RequestParam("images") List<MultipartFile> images, @PathVariable Integer id) throws IOException {
        listingService.saveListingImagesToContainer(images, id);
    }

    @GetMapping("/get/images/{id}")
    public List<String> getBlobs(@PathVariable Integer id) {
        return listingService.findListingImagesUrls(id);
    }
}
