package com.endava.marketplace.backend.controller;

import com.endava.marketplace.backend.dto.*;
import com.endava.marketplace.backend.service.SaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/sales")
@Tag(name = "Sale", description = "Sales management module")
@SecurityRequirement(name = "Azure AD")
public class SaleController {
    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @Operation(
            summary = "Creates a new sale",
            description = "Creates a new Sale with all of it's attributes. It will be associated to a listing_id, buyer_id and a sale-status_id. The sale quantity is going to subtract to the listing stock and, if necessary, change its status",
            tags = {"Sale"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = {@Content(schema = @Schema(implementation = SaleDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Listing with given Id doesn't have enough stock", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Listing with given Id was not found", content = { @Content(schema = @Schema()) }),

    })
    @PostMapping()
    public ResponseEntity<SaleDTO> postSale(@RequestBody NewSaleRequestDTO sale) {
        return ResponseEntity.ok(saleService.saveSale(sale));
    }

    @Operation(
            summary = "Gets a sale by an id",
            description = "Gets a sale from the database that matches the id provided. If a sale by that id doesn't exist it will return null",
            tags = {"Sale"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = {@Content(schema = @Schema(implementation = SaleDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Sale with given Id was not found", content = { @Content(schema = @Schema()) }),

    })
    @GetMapping("/{id}")
    public ResponseEntity<SaleDTO> getSaleById(@PathVariable Long id) {
        return ResponseEntity.ok(saleService.findSaleById(id));
    }

    @Operation(
            summary = "Gets a set of sales related to a buyer",
            description = "Gets a set of sales from the database related to a buyer_id. If the buyer hasn't bought anything it will return null",
            tags = {"Sale"}
    )
    @GetMapping("/buyer/{id}")
    public Set<ListedSaleDTO> getSalesByBuyerId(@PathVariable Long id) {
        return saleService.findSales(id, false);
    }

    @Operation(
            summary = "Gets a set of sales related to a seller",
            description = "Gets a set of sales from the database related to a seller_id. If the seller hasn't sold anything it will return null",
            tags = {"Sale"}
    )
    @GetMapping("/seller/{id}")
    public Set<ListedSaleDTO> getSalesBySellerId(@PathVariable Long id) {
        return saleService.findSales(id, true);
    }

    @Operation(
            summary = "Cancels a sale",
            description = "Updates the status of a sale to \"Cancelled\", reintegrates its quantity to the listing stock and, if necessary, update the listing status",
            tags = {"Sale"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = {@Content(schema = @Schema(implementation = SaleDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Sale or Listing with given Id was not found", content = { @Content(schema = @Schema()) }),
    })
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<SaleDTO> cancelSale(@PathVariable Long id) {
        return ResponseEntity.ok(saleService.updateSaleStatus(id, "Cancelled"));
    }

    @Operation(
            summary = "Fulfills a sale",
            description = "Updates the status of a sale to \"Fulfilled\"",
            tags = {"Sale"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = {@Content(schema = @Schema(implementation = SaleDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Sale or Listing with given Id was not found", content = { @Content(schema = @Schema()) }),
    })
    @PatchMapping("/{id}/fulfill")
    public ResponseEntity<SaleDTO> fulfillSale(@PathVariable Long id) {
        return ResponseEntity.ok(saleService.updateSaleStatus(id, "Fulfilled"));
    }


    @Operation(
            summary = "Rates a Sale",
            description = "Updates the rating of a sale to the given value. When a sale is rated the rating score of the seller is also updated",
            tags = {"Sale"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = {@Content(schema = @Schema(implementation = SaleDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Sale with given Id was not found", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "409", description = "Sale is pending and cannot be rated or is already rated", content = { @Content(schema = @Schema()) })
    })
    @PatchMapping("/{id}/rate/{rating}")
    public ResponseEntity<SaleDTO> rateSale(@PathVariable Long id, @PathVariable Integer rating) {
        return ResponseEntity.ok(saleService.rateSale(id, rating));
    }
}
