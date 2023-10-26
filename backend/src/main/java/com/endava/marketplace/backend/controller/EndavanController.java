package com.endava.marketplace.backend.controller;

import com.endava.marketplace.backend.model.Endavan;
import com.endava.marketplace.backend.service.EndavanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
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
    @PostMapping("/post")
    public Endavan createUser(){
        return endavanService.saveEndavan();
    }

    @Operation(
            summary = "Get user by id",
            description = "Gets an user from the database that matches the id provided",
            tags = {"Endavan"}
    )
    @GetMapping("/get/{id}")
    public Optional<Endavan> getEndavanById(@PathVariable Long id){
        return endavanService.findEndavanById(id);
    }
    @Operation(
            summary = "Delete user by id",
            description = "Deletes an user from the database that matches the id provided",
            tags = {"Endavan"}
    )
    @DeleteMapping("/delete/{id}")
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
}
