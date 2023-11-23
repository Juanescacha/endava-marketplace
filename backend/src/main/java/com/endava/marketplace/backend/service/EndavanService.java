package com.endava.marketplace.backend.service;

import com.endava.marketplace.backend.dto.EndavanAdminDTO;
import com.endava.marketplace.backend.dto.EndavanDTO;
import com.endava.marketplace.backend.exception.EntityAttributeAlreadySetException;
import com.endava.marketplace.backend.exception.EntityNotFoundException;
import com.endava.marketplace.backend.exception.NotEnoughPermissionsException;
import com.endava.marketplace.backend.mapper.EndavanMapper;
import com.endava.marketplace.backend.model.Endavan;
import com.endava.marketplace.backend.repository.EndavanRepository;
import com.endava.marketplace.backend.specification.EndavanSpecification;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public EndavanDTO findEndavanById(Long id) {
        return endavanMapper.toEndavanDTO(loadEndavan(id));
    }

    public void deleteEndavanById(Long endavanId) {
        endavanRepository.deleteById(endavanId);
    }

    @Transactional
    public EndavanAdminDTO updateAdminRole(Long endavanId, Boolean admin) {
        Endavan adminInfo = getEndavanInfo();
        Optional<Endavan> endavanAdmin = endavanRepository.findEndavanByEmailIgnoreCase(adminInfo.getEmail());
        Optional<Endavan> optionalEndavan = endavanRepository.findById(endavanId);

        if (optionalEndavan.isPresent() && endavanAdmin.isPresent()){
            Endavan endavan = optionalEndavan.get();
            if(endavanAdmin.get().getAdmin()){
                if(!endavanAdmin.get().getId().equals(endavanId)){
                    if(endavan.getAdmin() != admin){
                        endavan.setAdmin(admin);
                        return endavanMapper.toEndavanAdminDTO(endavanRepository.save(endavan));
                    }
                    throw new EntityAttributeAlreadySetException("The endavan with ID: " + endavanId + " has its permissions already set to " + admin);
                }
                throw new NotEnoughPermissionsException("You can't modify your own Admin permissions");
            }
            throw new NotEnoughPermissionsException("Actual user doesn't have Admin permissions");
        }
        throw new EntityNotFoundException("Endavan with ID: " + endavanId + " wasn't found");
    }

    public Boolean checkAdminRole(){
        Endavan userInfo = getEndavanInfo();
        Optional<Endavan> user = endavanRepository.findEndavanByEmailIgnoreCase(userInfo.getEmail());

        if (user.isPresent()){
            return user.get().getAdmin();
        }
        return false;
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

    public Page<EndavanAdminDTO> findEndavans(String name, String email, Integer page) {
        int actualPage = (page == null) ? 0: page - 1;
        Sort.Order orderByName = new Sort.Order(Sort.Direction.ASC, "name");
        Pageable pageWithFifteenElements = PageRequest.of(actualPage, 15, Sort.by(orderByName));

        Page<Endavan> results = endavanRepository.findAll((root, query, builder) -> {
            Predicate predicate = builder.conjunction();
            if (name != null && !name.isEmpty()) {
                predicate = builder.and(predicate, EndavanSpecification.withName(name).toPredicate(root, query, builder));
            }
            if (email != null && !email.isEmpty()) {
                predicate = builder.and(predicate, EndavanSpecification.withEmail(email).toPredicate(root, query, builder));
            }
            return predicate;
        }, pageWithFifteenElements);

        return results.map(endavanMapper::toEndavanAdminDTO);
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

        return new Endavan(null, name, email, false, null, null, null);
    }
    protected Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    protected Endavan loadEndavan(Long id) {
        Optional<Endavan> endavan = endavanRepository.findById(id);
        if(endavan.isEmpty()) {
            throw new EntityNotFoundException("Endavan with ID: " + id + " wasn't found");
        }
        return endavan.get();
    }
}
