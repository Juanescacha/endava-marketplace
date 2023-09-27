package com.endava.marketplace.backend.controller;

import com.endava.marketplace.backend.model.Sale;
import com.endava.marketplace.backend.service.SaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/sales")
@Tag(name = "Sales", description = "Sales management module")
@SecurityRequirement(name = "Azure AD")
public class SaleController {
    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @Operation(
            summary = "Creates a new sale",
            description = "Creates a new Sale with all of it's attributes. It will be associated to a listing_id, buyer_id and a sale-status_id",
            tags = {"Sale"}
    )
    @PostMapping("/post")
    public Sale postSale(@RequestBody Sale sale) {
        return saleService.saveSale(sale);
    }

    @Operation(
            summary = "Gets a sale by an id",
            description = "Gets a sale from the database that matches the id provided. If a sale by that id doesn't exist it will return null",
            tags = {"Sale"}
    )
    @GetMapping("/get/{id}")
    public Optional<Sale> getSaleById(@PathVariable Long id) {
        return saleService.findSaleById(id);
    }

    @Operation(
            summary = "Gets a set of sales related to a buyer",
            description = "Gets a set of sales from the database related to a buyer_id. If the buyer hasn't bought anything it will return null",
            tags = {"Sale"}
    )
    @GetMapping("/get/buyer/{id}")
    public Optional<Set<Sale>> getSalesByBuyerId(@PathVariable Long id) {
        return saleService.findSalesByBuyerId(id);
    }

    @Operation(
            summary = "Gets a set of sales related to a seller",
            description = "Gets a set of sales from the database related to a seller_id. If the seller hasn't sold anything it will return null",
            tags = {"Sale"}
    )
    @GetMapping("/get/seller/{id}")
    public Optional<Set<Sale>> getSalesBySellerId(@PathVariable Long id) {
        return saleService.findSalesBySellerId(id);
    }
}
