package com.endava.marketplace.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class AuthController {
    @GetMapping("/message")
    public String test(){
        return "This is a test endpoint protected by azure ad.";
    }
}
