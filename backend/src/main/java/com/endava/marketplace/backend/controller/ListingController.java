package com.endava.marketplace.backend.controller;

import com.endava.marketplace.backend.model.Listing;
import com.endava.marketplace.backend.service.ListingService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    @GetMapping("get/all")
    public Page<Listing> getAllListings(@RequestParam(required = false) String page){
        return listingService.findAllListings(page);
    }
    @GetMapping("/search/quick")
    public Set<Listing> getListingByName(@RequestParam String name){
        return listingService.findListingByName(name);
    }

    @GetMapping("/search/get")
    public Page<Listing> getListingByCategoryAndName(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String page){
        return listingService.findListingByCategoryAndName(category, name, page);
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
