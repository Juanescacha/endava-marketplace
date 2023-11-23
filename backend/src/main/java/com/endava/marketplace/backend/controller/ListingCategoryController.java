package com.endava.marketplace.backend.controller;

import com.endava.marketplace.backend.dto.ListingCategoryDTO;
import com.endava.marketplace.backend.dto.ActiveListingCategoryDTO;
import com.endava.marketplace.backend.exception.*;
import com.endava.marketplace.backend.service.ListingCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Validated
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
    public ResponseEntity<ListingCategoryDTO> postListingCategory(
            @RequestParam @NotEmpty(message = "Name cannot be empty") String name) {
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
    public ResponseEntity<List<ActiveListingCategoryDTO>> getAllActiveListingCategories() {
        return ResponseEntity.ok(listingCategoryService.fetchAllActiveListingCategories());
    }

    @Operation(
            summary = "Enables a Listing Category",
            description = "Sets the active value of a given Listing Category to true. If a Listing has this Category, its Status is set to Available",
            tags = {"Listing Category"}
    )
    @PatchMapping("/{id}/enable")
    public ResponseEntity<ListingCategoryDTO> enableListingCategory(@PathVariable Long id)
            throws EntityNotFoundException, EntityAttributeAlreadySetException {
        return ResponseEntity.ok(listingCategoryService.updateListingCategoryActiveStatus(id, true));
    }

    @Operation(
            summary = "Disables a Listing Category",
            description = "Sets the active value of a given Listing Category to false. If a Listing has this Category, its Status is set to Blocked",
            tags = {"Listing Category"}
    )
    @PatchMapping("/{id}/disable")
    public ResponseEntity<ListingCategoryDTO> disableListingCategory(@PathVariable Long id) {
        return ResponseEntity.ok(listingCategoryService.updateListingCategoryActiveStatus(id, false));
    }

    @Operation(
            summary = "Updates the name of a Listing Category",
            description = "Updates the name of an existing Listing Category. The new name cannot be blank and it cannot be the same as the old one",
            tags = {"Listing Category"}
    )
    @PatchMapping("/{id}/rename")
    public ResponseEntity<ListingCategoryDTO> patchListingCategoryName(
            @PathVariable Long id,
            @RequestParam @NotEmpty(message = "Name cannot be empty") String name) {
        return ResponseEntity.ok(listingCategoryService.updateListingCategoryName(id, name));
    }
}
