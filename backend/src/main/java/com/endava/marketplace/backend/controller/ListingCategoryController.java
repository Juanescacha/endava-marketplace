package com.endava.marketplace.backend.controller;

import com.endava.marketplace.backend.exception.BlankListingCategoryName;
import com.endava.marketplace.backend.exception.ListingCategoryAlreadyExists;
import com.endava.marketplace.backend.service.ListingCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Listing Category", description = "Listing categories management module")
public class ListingCategoryController {
    private final ListingCategoryService listingCategoryService;

    public ListingCategoryController(ListingCategoryService listingCategoryService) {
        this.listingCategoryService = listingCategoryService;
    }

    @Operation(
            summary = "Saves a Listing Category",
            description = "Saves a Listing Category to the database if its not blank and it doesn't already exists in the database",
            tags = {"Listing Category"}
    )
    @PostMapping()
    public ResponseEntity<Map<String, String>> postListingCategory(@RequestParam String name) throws ListingCategoryAlreadyExists, BlankListingCategoryName {
        return ResponseEntity.ok(listingCategoryService.saveListingCategory(name));
    }

    @Operation(
            summary = "Updates a Listing Category",
            description = "Saves a Listing Category to the database if its not blank and it doesn't already exists in the database",
            tags = {"Listing Category"}
    )
    @PatchMapping("/{id}/rename")
    public ResponseEntity<Map<String, String>> patchListingCategoryName(@PathVariable Long id, @RequestParam String name)
            throws NullPointerException, BlankListingCategoryName, ListingCategoryAlreadyExists {
        return ResponseEntity.ok(listingCategoryService.updateListingCategoryName(id, name));
    }
}
