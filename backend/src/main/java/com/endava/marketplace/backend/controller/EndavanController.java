package com.endava.marketplace.backend.controller;

import com.endava.marketplace.backend.dto.EndavanDTO;
import com.endava.marketplace.backend.model.Endavan;
import com.endava.marketplace.backend.service.EndavanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/api/endavans")
@Tag(name = "Endavan", description = "User management module")
@SecurityRequirement(name = "Azure AD")
public class EndavanController {
    private final EndavanService endavanService;
    private final WebClient webClient;

    public EndavanController(WebClient webClient, EndavanService endavanService) {
        this.endavanService = endavanService;
        this.webClient = webClient;
    }

    @Operation(
            summary = "Create a new user in the database",
            description = "Creates a new user in the database with the information present in the access token. " +
                    "In case that specific user already exists, nothing happens.",
            tags = {"Endavan"}
    )
    @PostMapping()
    public EndavanDTO createUser(){
        return endavanService.saveEndavan();
    }

    @Operation(
            summary = "Filtered search for endavans",
            description = "Gets all the lndavans according to the provided parameters. " +
                    "The search can be done by name, email or both and contains multiple pages." +
                    "The search can also be done without parameters to get all the listings in the database.",
            tags = {"Endavan"}
    )
    @GetMapping("/search")
    public Page<Endavan> getListingByCategoryAndName(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer page){
        return endavanService.findEndavans(name, email, page);
    }

    @Operation(
            summary = "Get user by id",
            description = "Gets an user from the database that matches the id provided",
            tags = {"Endavan"}
    )
    @GetMapping("/{id}")
    public EndavanDTO getEndavanById(@PathVariable Long id){
        return endavanService.findEndavanById(id);
    }

    @Operation(
            summary = "Delete user by id",
            description = "Deletes an user from the database that matches the id provided",
            tags = {"Endavan"}
    )
    @DeleteMapping("/{id}")
    public void deleteEndavanById(@PathVariable Long id){
        endavanService.deleteEndavanById(id);
    }

    @Operation(
            summary = "Get user profile picture",
            description = "Gets an user profile picture directly from his Endava account",
            tags = {"Endavan"}
    )
    @GetMapping(
            value = "/picture",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody byte[] getPicture(@RegisteredOAuth2AuthorizedClient("graph") OAuth2AuthorizedClient graph){
        return endavanService.getGraphPicture(graph, webClient);
    }

    @Operation(
            summary = "Update Admin role",
            description = "Turns on or off the admin privileges for the other Endavans. Can't be used to turn off my own admin permissions",
            tags = {"Endavan"}
    )
    @PatchMapping("/admin")
    public void updateAdminRole(@RequestParam Long endavanId, @RequestParam Boolean isAdmin){
        endavanService.updateAdminRole(endavanId, isAdmin);
    }

    @Operation(
            summary = "Check Admin role",
            description = "Checks if the actual user has admin privileges",
            tags = {"Endavan"}
    )
    @GetMapping("/isAdmin")
    public Boolean checkAdminRole(){
        return endavanService.checkAdminRole();
    }
}
