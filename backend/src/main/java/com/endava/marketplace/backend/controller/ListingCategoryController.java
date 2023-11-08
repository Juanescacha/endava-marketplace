package com.endava.marketplace.backend.controller;

import com.endava.marketplace.backend.dto.ListingCategoryDTO;
import com.endava.marketplace.backend.dto.SimpleListingCategoryDTO;
import com.endava.marketplace.backend.exception.BlankListingCategoryName;
import com.endava.marketplace.backend.exception.ListingCategoryAlreadyExists;
import com.endava.marketplace.backend.service.ListingCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        return new ResponseEntity<>(listingCategoryService.saveListingCategory(name), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Gets all listing categories",
            description = "Gets all listing categories that are saved in the database in ascending order",
            tags = {"Listing Category"}
    )
    @GetMapping()
    public ResponseEntity<List<ListingCategoryDTO>> getAllListingCategories() {
        return ResponseEntity.ok(listingCategoryService.fetchAllListingCategories());
    }

    @Operation(
            summary = "Gets all active listing categories",
            description = "Gets all active listing categories that are saved in the database in ascending order",
            tags = {"Listing Category"}
    )
    @GetMapping("/active")
    public ResponseEntity<List<SimpleListingCategoryDTO>> getAllActiveListingCategories() {
        return ResponseEntity.ok(listingCategoryService.fetchAllActiveListingCategories());
    }

    @Operation(
            summary = "Updates a Listing Category",
            description = "Updates the name of an existing Listing Category. The new name cannot be blank and it cannot be the same as the old one",
            tags = {"Listing Category"}
    )
    @PatchMapping("/{id}/rename")
    public ResponseEntity<Map<String, String>> patchListingCategoryName(@PathVariable Long id, @RequestParam String name)
            throws NullPointerException, BlankListingCategoryName, ListingCategoryAlreadyExists {
        return ResponseEntity.ok(listingCategoryService.updateListingCategoryName(id, name));
    }
}
