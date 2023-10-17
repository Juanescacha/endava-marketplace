package com.endava.marketplace.backend.service;

import com.endava.marketplace.backend.model.Endavan;
import com.endava.marketplace.backend.repository.EndavanRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EndavanServiceTests {
    @Mock
    private EndavanRepository endavanRepository;
    private EndavanService endavanService;
    @BeforeEach
    void setUp(){
        endavanService = Mockito.spy(new EndavanService(endavanRepository));
    }

    @Test
    public void givenEndavanId_whenFindEndavanById_thenReturnEndavan(){
        Integer endavanID = 1;

        Endavan endavan = Endavan.builder()
                .id(1L)
                .name("Name")
                .email("email@example.com")
                .admin(false)
                .listings(null)
                .questions(null)
                .sales(null)
                .build();

        when(endavanRepository.findById(endavanID)).thenReturn(Optional.ofNullable(endavan));

        Optional<Endavan> foundEndavan = endavanService.findEndavanById(endavanID);

        Assertions.assertThat(foundEndavan).isNotNull();
        Assertions.assertThat(endavan.getId()).isEqualTo(foundEndavan.get().getId());
    }

    @Test
    public void givenEndavanId_whenDeleteEndavanById_thenDeletesUser(){
        // Arrange
        Integer endavanID = 1;
        doNothing().when(endavanRepository).deleteById(endavanID);

        // Act
        endavanService.deleteEndavanById(endavanID);

        // Assert
        verify(endavanRepository, times(1)).deleteById(endavanID);
    }

    @Test
    public void givenAuthentication_whenSaveEndavan_thenReturnsSavedEndavan(){
        JwtAuthenticationToken authentication = Mockito.mock(JwtAuthenticationToken.class);
        Jwt principal = Mockito.mock(Jwt.class);
        when(endavanService.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(principal);
        when(principal.getClaim("name")).thenReturn("Endavan");
        when(principal.getClaim("preferred_username")).thenReturn(null);
        when(principal.getClaim("upn")).thenReturn("email@example.com");

        Endavan endavan = Endavan.builder()
                .name("Endavan")
                .email("email@example.com")
                .admin(false)
                .build();

        when(endavanRepository.findEndavanByEmailIgnoreCase("email@example.com"))
                .thenReturn(Optional.ofNullable(endavan));

        Endavan savedEndavan = endavanService.saveEndavan();

        Assertions.assertThat(savedEndavan).isEqualTo(endavan);
    }
}
