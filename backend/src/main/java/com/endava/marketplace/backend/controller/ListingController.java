package com.endava.marketplace.backend.controller;

import com.endava.marketplace.backend.model.Listing;
import com.endava.marketplace.backend.service.ListingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/listings")
@Tag(name = "Listing", description = "Listings management module")
@SecurityRequirement(name = "Azure AD")
public class ListingController {
    private final ListingService listingService;

    public ListingController(ListingService listingService) {
        this.listingService = listingService;
    }
    @Operation(
            summary = "Creates a new Listing",
            description = "Creates a new listing with all of their attributes. It will be associated to their owner by id",
            tags = {"Listing"}
    )
    @PostMapping("/post")
    public Listing postListing(@RequestBody Listing listing) {return listingService.saveListing(listing);}

    @Operation(
            summary = "Get listing by id",
            description = "Gets a listing from the database that matches the id provided",
            tags = {"Listing"}
    )
    @GetMapping("/get/{id}")
    public Optional<Listing> getListingById(@PathVariable Integer id) {
        return listingService.findListingById(id);
    }

    @Operation(
            summary = "Search bar suggestions",
            description = "Gets the top 5 most recent listings that contain the name provided",
            tags = {"Listing"}
    )
    @GetMapping("/search/quick")
    public Set<Listing> getListingByName(@RequestParam String name){
        return listingService.findListingByName(name);
    }

    @Operation(
            summary = "Filtered search",
            description = "Gets all the listings according to the provided parameters. " +
                    "The search can be done by name, category (id) or both and contains multiple pages." +
                    "The search can also be done without parameters to get all the listings in the database.",
            tags = {"Listing"}
    )
    @GetMapping("/search/get")
    public Page<Listing> getListingByCategoryAndName(
            @RequestParam(required = false) Integer category,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer page){
        return listingService.findListings(category, name, page);
    }

    @Operation(
            summary = "Delete Listing",
            description = "Deletes a listing from the database that matches the id provided",
            tags = {"Listing"}
    )
    @DeleteMapping("delete/{id}")
    public void deleteListingById(@PathVariable Integer id) {listingService.deleteListingById(id);}

    @Operation(
            summary = "Save listing images to storage account",
            description = "Save a list of MultipartFile type images related to the desired listing to the project's storage account",
            tags = {"Listing"}
    )
    @PostMapping("/post/images/{id}")
    public void postListingImages(@RequestParam("images") List<MultipartFile> images, @PathVariable Integer id) throws IOException {
        listingService.saveListingImagesToContainer(images, id);
    }

    @Operation(
            summary = "Get listing images endpoints from storage account",
            description = "Get a list of endpoints redirecting to the desired listing images saved in the project's storage account",
            tags = {"Listing"}
    )
    @GetMapping("/get/images/{id}")
    public List<String> getBlobs(@PathVariable Integer id) {
        return listingService.findListingImagesUrls(id);
    }
}
