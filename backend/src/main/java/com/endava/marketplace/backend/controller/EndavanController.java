package com.endava.marketplace.backend.controller;

import com.endava.marketplace.backend.model.Endavan;
import com.endava.marketplace.backend.service.EndavanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@Tag(name = "Endavan", description = "User management module")
@SecurityRequirement(name = "Azure AD")
public class EndavanController {
    private final EndavanService endavanService;

    public EndavanController(EndavanService endavanService) {this.endavanService = endavanService;}

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
    public Optional<Endavan> getEndavanById(@PathVariable Integer id){
        return endavanService.findEndavanById(id);
    }
    @Operation(
            summary = "Delete user by id",
            description = "Deletes an user from the database that matches the id provided",
            tags = {"Endavan"}
    )
    @DeleteMapping("/delete/{id}")
    public void deleteEndavanById(@PathVariable Integer id){
        endavanService.deleteEndavanById(id);
    }
}
