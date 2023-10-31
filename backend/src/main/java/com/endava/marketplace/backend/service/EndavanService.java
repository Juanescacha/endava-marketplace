package com.endava.marketplace.backend.service;

import com.endava.marketplace.backend.dto.EndavanDTO;
import com.endava.marketplace.backend.mapper.EndavanMapper;
import com.endava.marketplace.backend.model.Endavan;
import com.endava.marketplace.backend.repository.EndavanRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.Optional;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@Service
public class EndavanService {
    private static final String GRAPH_PICTURE_ENDPOINT = "https://graph.microsoft.com/v1.0/me/photo/$value";
    private final EndavanRepository endavanRepository;

    private final EndavanMapper endavanMapper;

    public EndavanService(EndavanRepository endavanRepository, EndavanMapper endavanMapper) {
        this.endavanRepository = endavanRepository;
        this.endavanMapper = endavanMapper;
    }

    public EndavanDTO saveEndavan() {
        Endavan endavan = getEndavanInfo();
        Optional<Endavan> savedEndavan = endavanRepository.findEndavanByEmailIgnoreCase(endavan.getEmail());

        if (savedEndavan.isEmpty()){
            return endavanMapper.toEndavanDTO(endavanRepository.save(endavan));
        }

        endavan = savedEndavan.get();
        return endavanMapper.toEndavanDTO(endavan);
    }

    public EndavanDTO findEndavanById(Long endavanId) {
        return endavanRepository.findById(endavanId).map(endavanMapper::toEndavanDTO).orElse(null);
    }

    public void deleteEndavanById(Long endavanId) {endavanRepository.deleteById(endavanId);}

    public void updateAdminRole(Long endavanId, Boolean isAdmin){
        Endavan userInfo = getEndavanInfo();
        Optional<Endavan> user = endavanRepository.findEndavanByEmailIgnoreCase(userInfo.getEmail());

        if (user.isPresent()){
            if((!user.get().getId().equals(endavanId)) && (user.get().getAdmin())){
                endavanRepository.updateAdminRole(endavanId, isAdmin);
            }
        }
    }

    public byte[] getGraphPicture(OAuth2AuthorizedClient graph, WebClient webClient) {
        if (null != graph) {
            byte[] body = webClient
                    .get()
                    .uri(GRAPH_PICTURE_ENDPOINT)
                    .attributes(oauth2AuthorizedClient(graph))
                    .retrieve()
                    .bodyToMono(byte[].class)
                    .block();
            return body;
        }
        else return null;
    }

    private Endavan getEndavanInfo(){
        Authentication authentication = getAuthentication();
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
    protected Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
