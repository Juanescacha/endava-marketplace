package com.endava.marketplace.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@Tag(name = "Test", description = "Server Testing module")
public class AuthController {
    @Operation(
            summary = "Get a response from a public test endpoint",
            description = "Get a response from the server if it's up and running. This endpoint doesn't require any authentication",
            tags = {"Test"}
    )
    @GetMapping("/public")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ok", content = { @Content(schema = @Schema(implementation = String.class)) }),
    })
    public String test() {
        return "This is a public test endpoint and its NOT protected by azure ad.";
    }

    @Operation(
            summary = "Get a response from a private test endpoint",
            description = "Get a response from the server if it's up and running. This endpoint requires a azure AD authentication via access token",
            tags = {"Test"},
            security = @SecurityRequirement(name = "Azure AD")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ok", content = { @Content(schema = @Schema(implementation = String.class)) }),
    })
    @GetMapping("/private")
    public String testPrivate() {
        return "This is a private test endpoint and is protected by azure ad";
    }
}
