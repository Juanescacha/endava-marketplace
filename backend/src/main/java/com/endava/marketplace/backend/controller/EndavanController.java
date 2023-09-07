package com.endava.marketplace.backend.controller;

import com.endava.marketplace.backend.model.Endavan;
import com.endava.marketplace.backend.service.EndavanService;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class EndavanController {
    private final EndavanService endavanService;

    public EndavanController(EndavanService endavanService) {this.endavanService = endavanService;}

    @PostMapping("/post")
    public Endavan createUser(){
        return endavanService.saveEndavan();
    }

    @GetMapping("/get/{id}")
    public Optional<Endavan> getEndavanById(@PathVariable Integer id){
        return endavanService.findEndavanById(id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteEndavanById(@PathVariable Integer id){
        endavanService.deleteEndavanById(id);
    }
}
