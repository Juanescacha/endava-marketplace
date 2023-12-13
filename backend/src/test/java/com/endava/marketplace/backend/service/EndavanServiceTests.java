package com.endava.marketplace.backend.service;

import com.endava.marketplace.backend.dto.EndavanAdminDTO;
import com.endava.marketplace.backend.dto.EndavanDTO;
import com.endava.marketplace.backend.dto.RatingDTO;
import com.endava.marketplace.backend.exception.EntityAttributeAlreadySetException;
import com.endava.marketplace.backend.exception.EntityNotFoundException;
import com.endava.marketplace.backend.exception.NotEnoughPermissionsException;
import com.endava.marketplace.backend.mapper.EndavanMapper;
import com.endava.marketplace.backend.model.Endavan;
import com.endava.marketplace.backend.repository.EndavanRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

@DisplayName("Endavan Service Unit Tests")
@ExtendWith(MockitoExtension.class)
public class EndavanServiceTests {
    @Mock
    private EndavanRepository endavanRepository;
    @Mock
    private EndavanMapper endavanMapper;

    private EndavanService endavanService;

    @BeforeEach
    void setUp(){
        endavanService = Mockito.spy(new EndavanService(endavanRepository, endavanMapper));
    }


    @Test
    public void givenSavedAuthentication_whenSaveEndavan_thenReturnsSavedEndavan(){
        // Given
        JwtAuthenticationToken authentication = Mockito.mock(JwtAuthenticationToken.class);
        Jwt principal = Mockito.mock(Jwt.class);
        when(endavanService.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(principal);
        when(principal.getClaim("name")).thenReturn("Endavan");
        when(principal.getClaim("preferred_username")).thenReturn(null);
        when(principal.getClaim("upn")).thenReturn("email@example.com");

        Endavan endavan = Endavan.builder()
                .id(1L)
                .name("Endavan")
                .email("email@example.com")
                .admin(false)
                .build();

        EndavanDTO endavanDTO = EndavanDTO.builder()
                .id(1L)
                .name("Endavan")
                .email("email@example.com")
                .build();

        when(endavanRepository.findEndavanByEmailIgnoreCase("email@example.com"))
                .thenReturn(Optional.ofNullable(endavan));
        when(endavanMapper.toEndavanDTO(endavan)).thenReturn(endavanDTO);

        // When
        EndavanDTO savedEndavan = endavanService.saveEndavan();

        // Then
        Assertions.assertThat(savedEndavan.getId()).isEqualTo(endavan.getId());
        Assertions.assertThat(savedEndavan.getName()).isEqualTo(endavan.getName());
        Assertions.assertThat(savedEndavan.getEmail()).isEqualTo(endavan.getEmail());
    }

    @Test
    public void givenNotSavedAuthentication_whenSaveEndavan_thenReturnsSavedEndavan(){
        // Given
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

        RatingDTO ratingDto= RatingDTO.builder()
                .score(null)
                .quantity(0)
                .build();

        EndavanDTO endavanDTO = EndavanDTO.builder()
                .name("Endavan")
                .email("email@example.com")
                .rating(ratingDto)
                .build();

        when(endavanRepository.findEndavanByEmailIgnoreCase("email@example.com"))
                .thenReturn(Optional.empty());
        when(endavanRepository.save(any(Endavan.class))).thenReturn(endavan);
        when(endavanMapper.toEndavanDTO(any(Endavan.class))).thenReturn(endavanDTO);

        // When
        EndavanDTO savedEndavan = endavanService.saveEndavan();

        // Then
        Assertions.assertThat(savedEndavan.getId()).isEqualTo(endavan.getId());
        Assertions.assertThat(savedEndavan.getName()).isEqualTo(endavan.getName());
        Assertions.assertThat(savedEndavan.getEmail()).isEqualTo(endavan.getEmail());
    }

    @Test
    public void givenEndavanId_whenFindEndavanById_thenReturnEndavanDTO(){
        // Given
        Long endavanID = 1L;
        Endavan endavan = Endavan.builder()
                .id(endavanID)
                .name("Example Name")
                .email("email@example.com")
                .admin(false)
                .build();

        EndavanDTO endavanDTO = EndavanDTO.builder()
                .id(endavanID)
                .name("Example Name")
                .email("email@example.com")
                .build();

        when(endavanRepository.findById(endavanID)).thenReturn(Optional.ofNullable(endavan));
        when(endavanService.loadEndavan(endavanID)).thenReturn(endavan);
        when(endavanMapper.toEndavanDTO(endavan)).thenReturn(endavanDTO);

        // When
        EndavanDTO foundEndavan = endavanService.findEndavanById(endavanID);

        // Then
        Assertions.assertThat(foundEndavan).isNotNull();
        Assertions.assertThat(foundEndavan.getId()).isEqualTo(endavan.getId());
        Assertions.assertThat(foundEndavan.getName()).isEqualTo(endavan.getName());
        Assertions.assertThat(foundEndavan.getEmail()).isEqualTo(endavan.getEmail());
    }

    @Test
    public void givenEndavanId_whenDeleteEndavanById_thenDeletesUser(){
        Long endavanID = 1L;
        doNothing().when(endavanRepository).deleteById(endavanID);

        endavanService.deleteEndavanById(endavanID);

        verify(endavanRepository, times(1)).deleteById(endavanID);
    }

    @Test
    public void givenValidAdminAuthentication_whenUpdateAdminRole_thenReturnsUpdatedEndavanDTO(){
        // Given
        JwtAuthenticationToken authentication = Mockito.mock(JwtAuthenticationToken.class);
        Jwt principal = Mockito.mock(Jwt.class);
        when(endavanService.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(principal);
        when(principal.getClaim("name")).thenReturn("Endavan Admin");
        when(principal.getClaim("preferred_username")).thenReturn(null);
        when(principal.getClaim("upn")).thenReturn("admin@example.com");

        Endavan adminEndavan = Endavan.builder()
                .id(1L)
                .name("Endavan Admin")
                .email("admin@example.com")
                .admin(true)
                .build();

        Endavan notAdminEndavan = Endavan.builder()
                .id(2L)
                .name("Endavan normal")
                .email("notadmin@example.com")
                .admin(false)
                .build();

        Endavan updatedAdminEndavan = Endavan.builder()
                .id(2L)
                .name("Endavan normal")
                .email("notadmin@example.com")
                .admin(true)
                .build();

        EndavanAdminDTO updatedAdminEndavanDTO = EndavanAdminDTO.builder()
                .id(2L)
                .name("Endavan normal")
                .email("notadmin@example.com")
                .admin(true)
                .build();

        when(endavanRepository.findEndavanByEmailIgnoreCase("admin@example.com")).thenReturn(Optional.ofNullable(adminEndavan));
        when(endavanRepository.findById(2L)).thenReturn(Optional.ofNullable(notAdminEndavan));
        when(endavanRepository.save(Mockito.any(Endavan.class))).thenReturn(updatedAdminEndavan);
        when(endavanMapper.toEndavanAdminDTO(Mockito.any(Endavan.class))).thenReturn(updatedAdminEndavanDTO);

        // When
        EndavanAdminDTO updatedEndavan = endavanService.updateAdminRole(2L, true);

        // Then
        Assertions.assertThat(updatedEndavan.getAdmin()).isEqualTo(true);
    }

    @Test
    public void givenValidAdminAuthentication_whenUpdateAdminRole_AndPermissionsAlreadySet_thenReturnEntityAttributeAlreadySetException(){
        // Given
        Long alreadyAdminId = 2L;
        Boolean alreadyAdminFlag = true;

        JwtAuthenticationToken authentication = Mockito.mock(JwtAuthenticationToken.class);
        Jwt principal = Mockito.mock(Jwt.class);
        when(endavanService.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(principal);
        when(principal.getClaim("name")).thenReturn("Endavan Admin");
        when(principal.getClaim("preferred_username")).thenReturn(null);
        when(principal.getClaim("upn")).thenReturn("admin@example.com");

        Endavan adminEndavan = Endavan.builder()
                .id(1L)
                .name("Endavan Admin")
                .email("admin@example.com")
                .admin(true)
                .build();

        Endavan alreadyAdminEndavan = Endavan.builder()
                .id(alreadyAdminId)
                .name("Endavan normal")
                .email("notadmin@example.com")
                .admin(alreadyAdminFlag)
                .build();

        when(endavanRepository.findEndavanByEmailIgnoreCase("admin@example.com")).thenReturn(Optional.ofNullable(adminEndavan));
        when(endavanRepository.findById(2L)).thenReturn(Optional.ofNullable(alreadyAdminEndavan));

        // When - Then
        Assertions.assertThatThrownBy(
                        () -> endavanService.updateAdminRole(2L, true))
                .isInstanceOf(EntityAttributeAlreadySetException.class)
                .hasMessageContaining("The endavan with ID: " + alreadyAdminId
                        + " has its permissions already set to " + alreadyAdminFlag);
    }

    @Test
    public void givenValidAdminAuthentication_whenUpdateAdminRole_AndSameAdminId_ThenReturnNotEnoughPermissionsException(){
        // Given
        Long adminId = 1L;
        Boolean adminFlag = true;

        JwtAuthenticationToken authentication = Mockito.mock(JwtAuthenticationToken.class);
        Jwt principal = Mockito.mock(Jwt.class);
        when(endavanService.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(principal);
        when(principal.getClaim("name")).thenReturn("Endavan Admin");
        when(principal.getClaim("preferred_username")).thenReturn(null);
        when(principal.getClaim("upn")).thenReturn("admin@example.com");

        Endavan adminEndavan = Endavan.builder()
                .id(adminId)
                .name("Endavan Admin")
                .email("admin@example.com")
                .admin(true)
                .build();

        when(endavanRepository.findEndavanByEmailIgnoreCase("admin@example.com")).thenReturn(Optional.ofNullable(adminEndavan));
        when(endavanRepository.findById(adminId)).thenReturn(Optional.ofNullable(adminEndavan));

        // When - Then
        Assertions.assertThatThrownBy(
                        () -> endavanService.updateAdminRole(adminId, adminFlag))
                .isInstanceOf(NotEnoughPermissionsException.class)
                .hasMessageContaining("You can't modify your own Admin permissions");
    }

    @Test
    public void givenInvalidAdminAuthentication_whenUpdateAdminRole_ThenReturnNotEnoughPermissionsException(){
        // Given
        Long targetEndavanId = 1L;
        Boolean targetEndavanFlag = true;

        JwtAuthenticationToken authentication = Mockito.mock(JwtAuthenticationToken.class);
        Jwt principal = Mockito.mock(Jwt.class);
        when(endavanService.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(principal);
        when(principal.getClaim("name")).thenReturn("Endavan Admin");
        when(principal.getClaim("preferred_username")).thenReturn(null);
        when(principal.getClaim("upn")).thenReturn("endavan@example.com");

        Endavan notAdminEndavan = Endavan.builder()
                .id(1L)
                .name("Endavan Not Admin")
                .email("endavan@example.com")
                .admin(false)
                .build();

        Endavan targetEndavan = Endavan.builder()
                .id(targetEndavanId)
                .name("Endavan")
                .email("email@example.com")
                .admin(false)
                .build();

        when(endavanRepository.findEndavanByEmailIgnoreCase("endavan@example.com")).thenReturn(Optional.ofNullable(notAdminEndavan));
        when(endavanRepository.findById(targetEndavanId)).thenReturn(Optional.ofNullable(targetEndavan));

        // When - Then
        Assertions.assertThatThrownBy(
                        () -> endavanService.updateAdminRole(targetEndavanId, targetEndavanFlag))
                .isInstanceOf(NotEnoughPermissionsException.class)
                .hasMessageContaining("Actual user doesn't have Admin permissions");
    }

    @Test
    public void givenAuthentication_whenUpdateAdminRole_AndTargetEndavanNotFound_ThenReturnEntityNotFoundException(){
        // Given
        Long targetEndavanId = 2L;
        Boolean targetEndavanFlag = true;

        JwtAuthenticationToken authentication = Mockito.mock(JwtAuthenticationToken.class);
        Jwt principal = Mockito.mock(Jwt.class);
        when(endavanService.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(principal);
        when(principal.getClaim("name")).thenReturn("Endavan Admin");
        when(principal.getClaim("preferred_username")).thenReturn(null);
        when(principal.getClaim("upn")).thenReturn("endavan@example.com");

        Endavan adminEndavan = Endavan.builder()
                .id(1L)
                .name("Endavan Admin")
                .email("endavan@example.com")
                .admin(true)
                .build();

        when(endavanRepository.findEndavanByEmailIgnoreCase("endavan@example.com")).thenReturn(Optional.ofNullable(adminEndavan));
        when(endavanRepository.findById(2L)).thenReturn(Optional.empty());

        // When - Then
        Assertions.assertThatThrownBy(
                        () -> endavanService.updateAdminRole(targetEndavanId, targetEndavanFlag))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Endavan with ID: " + targetEndavanId + " wasn't found");
    }

    @Test
    public void givenAuthentication_whenCheckAdminRole_thenReturnsBoolean(){
        // Given
        JwtAuthenticationToken authentication = Mockito.mock(JwtAuthenticationToken.class);
        Jwt principal = Mockito.mock(Jwt.class);
        when(endavanService.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(principal);
        when(principal.getClaim("name")).thenReturn("Endavan");
        when(principal.getClaim("preferred_username")).thenReturn(null);
        when(principal.getClaim("upn")).thenReturn("email@example.com");

        Endavan endavan = Endavan.builder()
                .id(1L)
                .name("Endavan")
                .email("email@example.com")
                .admin(true)
                .build();

        when(endavanRepository.findEndavanByEmailIgnoreCase("email@example.com"))
                .thenReturn(Optional.ofNullable(endavan));

        // When
        Boolean adminFlag = endavanService.checkAdminRole();

        // Then
        Assertions.assertThat(adminFlag).isEqualTo(endavan.getAdmin());
    }

    @Test
    public void givenInvalidAuthentication_whenCheckAdminRole_thenReturnsFalse(){
        // Given
        JwtAuthenticationToken authentication = Mockito.mock(JwtAuthenticationToken.class);
        Jwt principal = Mockito.mock(Jwt.class);
        when(endavanService.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(principal);
        when(principal.getClaim("name")).thenReturn("Endavan");
        when(principal.getClaim("preferred_username")).thenReturn(null);
        when(principal.getClaim("upn")).thenReturn("email@example.com");

        when(endavanRepository.findEndavanByEmailIgnoreCase("email@example.com"))
                .thenReturn(Optional.empty());

        // When
        Boolean adminFlag = endavanService.checkAdminRole();

        // Then
        Assertions.assertThat(adminFlag).isEqualTo(false);
    }

//    @Test
//    public void givenEndavanInfo_whenFindEndavans_thenReturnPageWithResults(){
//        // Given
//        String name = "Endavan";
//        String email = "endavan";
//        Endavan endavanOne = Endavan.builder()
//                .id(1L)
//                .name("Endavan #1")
//                .email("endavan1@email.com")
//                .admin(false)
//                .build();
//
//        Endavan endavanTwo= Endavan.builder()
//                .id(2L)
//                .name("Endavan #2")
//                .email("endavan2@email.com")
//                .admin(false)
//                .build();
//
//        Page<Endavan> expectedEndavans = new PageImpl<>(List.of(endavanOne, endavanTwo));
//
//        EndavanAdminDTO endavanOneDTO = EndavanAdminDTO.builder()
//                .id(1L)
//                .name("Endavan #1")
//                .email("endavan1@email.com")
//                .admin(false)
//                .build();
//
//        EndavanAdminDTO endavanTwoDTO = EndavanAdminDTO.builder()
//                .id(2L)
//                .name("Endavan #2")
//                .email("endavan2@email.com")
//                .admin(false)
//                .build();
//
//        Page<EndavanAdminDTO> expectedEndavansDTO = new PageImpl<>(List.of(endavanOneDTO, endavanTwoDTO));
//
//        Sort.Order orderByName = new Sort.Order(Sort.Direction.ASC, "name");
//        Pageable pageWithFifteenElements = PageRequest.of(0, 15, Sort.by(orderByName));
//
//        Specification<Endavan> spec = EndavanSpecification.withName(name);
//        lenient().when(endavanRepository.findAll(spec, pageWithFifteenElements)).thenReturn(expectedEndavans);
//        //when(expectedEndavans.map(endavanMapper::toEndavanAdminDTO)).thenReturn(expectedEndavansDTO);
//
//        //doReturn(expectedEndavans).when(endavanRepository.findAll(spec,pageWithFifteenElements));
//        //given(endavanRepository.findAll(spec, pageWithFifteenElements)).willReturn(expectedEndavans);
//
//        // When
//        endavanService.findEndavans(name, null, 1);
//
//        // Then
//        verify(endavanRepository, times(1)).findAll(spec, pageWithFifteenElements);
//    }

    @Test
    public void givenEndavanId_whenLoadEndavan_thenReturnEndavan(){
        // Given
        Long endavanID = 1L;
        Endavan endavan = Endavan.builder()
                .id(endavanID)
                .name("Example Name")
                .email("email@example.com")
                .admin(false)
                .build();

        // When
        when(endavanRepository.findById(endavanID)).thenReturn(Optional.ofNullable(endavan));
        Endavan foundEndavan = endavanService.loadEndavan(endavanID);

        // Then
        Assertions.assertThat(foundEndavan).isNotNull();
        Assertions.assertThat(endavan.getId()).isEqualTo(foundEndavan.getId());

    }

    @Test
    public void givenWrongEndavanId_whenLoadEndavan_thenReturnEntityNotFoundException(){
        // Given
        Long wrongId = 1L;
        when(endavanRepository.findById(wrongId)).thenReturn(Optional.empty());

        // When - Then
        Assertions.assertThatThrownBy(
                        () -> endavanService.loadEndavan(wrongId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Endavan with ID: " + wrongId + " wasn't found");
    }
}
