package com.endava.marketplace.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class AuthController {
    @GetMapping("/public")
    public String test(){
        return "This is a public test endpoint and its NOT protected by azure ad.";
    }
    @GetMapping("/private")
    public String testPrivate(){
        return "This is a private test endpoint and is protected by azure ad";
    }
}
