package com.endava.marketplace.backend.controller;

import com.endava.marketplace.backend.dto.EndavanAdminDTO;
import com.endava.marketplace.backend.dto.EndavanDTO;
import com.endava.marketplace.backend.service.EndavanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<EndavanDTO> createUser(){
        return ResponseEntity.ok(endavanService.saveEndavan());
    }

    @Operation(
            summary = "Filtered search for endavans",
            description = "Gets all the endavans according to the provided parameters. " +
                    "The search can be done by name, email or both and contains multiple pages." +
                    "The search can also be done without parameters to get all the listings in the database.",
            tags = {"Endavan"}
    )
    @GetMapping("/search")
    public ResponseEntity<Page<EndavanAdminDTO>> getEndavanByNameAndEmailDTO(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer page){
        return ResponseEntity.ok(endavanService.findEndavans(name, email, page));
    }

    @Operation(
            summary = "Get user by id",
            description = "Gets an user from the database that matches the id provided",
            tags = {"Endavan"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = {@Content(schema = @Schema(implementation = EndavanDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Endavan with given Id was not found", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("/{id}")
    public ResponseEntity<EndavanDTO> getEndavanById(@PathVariable Long id) {
        return ResponseEntity.ok(endavanService.findEndavanById(id));
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = {@Content(schema = @Schema(implementation = EndavanAdminDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Endavan with given Id was not found", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Actual endavan doesn't have enough permissions to perform this action", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "409", description = "Target endavan has this property already set to that value", content = { @Content(schema = @Schema()) })
    })
    @PatchMapping("/update-admin")
    public ResponseEntity<EndavanAdminDTO> updateAdminRole(@RequestParam Long endavanId, @RequestParam  Boolean admin) {
        return ResponseEntity.ok(endavanService.updateAdminRole(endavanId, admin));
    }

    @Operation(
            summary = "Check Admin role",
            description = "Checks if the actual user has admin privileges",
            tags = {"Endavan"}
    )
    @GetMapping("/check-admin")
    public ResponseEntity<Boolean> checkAdminRole(){
        return ResponseEntity.ok(endavanService.checkAdminRole());
    }
}
