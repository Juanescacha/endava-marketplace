package com.endava.marketplace.backend.service;

import com.endava.marketplace.backend.model.Endavan;
import com.endava.marketplace.backend.repository.EndavanRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class EndavanService {
    private final EndavanRepository endavanRepository;

    public EndavanService(EndavanRepository endavanRepository) {
        this.endavanRepository = endavanRepository;
    }

    public Endavan saveEndavan() {
        Endavan endavan = getEndavanInfo();
        Optional<Endavan> savedEndavan = endavanRepository.findEndavanByEmailIgnoreCase(endavan.getEmail());
        if (savedEndavan.isEmpty()){
            return endavanRepository.save(endavan);
        }
        endavan = savedEndavan.get();
        return endavan;
    }

    public Optional<Endavan> findEndavanById(Integer endavanId) {return endavanRepository.findById(endavanId);}

    public void deleteEndavanById(Integer endavanId) {endavanRepository.deleteById(endavanId);}

    private Endavan getEndavanInfo(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtAuthenticationToken auth = (JwtAuthenticationToken) authentication;
        Jwt principal = (Jwt) auth.getPrincipal();
        String name  = principal.getClaim("name");
        String email = principal.getClaim("preferred_username");
        if (email == null){
            email = principal.getClaim("upn");
        }
        System.out.println(email);

        return new Endavan(null, name, email, false, null, null, null);
    }
}
