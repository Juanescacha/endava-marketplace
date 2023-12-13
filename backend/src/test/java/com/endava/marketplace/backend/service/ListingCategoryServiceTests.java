package com.endava.marketplace.backend.service;

import com.endava.marketplace.backend.dto.ActiveListingCategoryDTO;
import com.endava.marketplace.backend.dto.ListingCategoryDTO;
import com.endava.marketplace.backend.exception.EntityAlreadyExistsException;
import com.endava.marketplace.backend.exception.EntityAttributeAlreadySetException;
import com.endava.marketplace.backend.exception.EntityNotFoundException;
import com.endava.marketplace.backend.mapper.ListingCategoryMapper;
import com.endava.marketplace.backend.model.ListingCategory;
import com.endava.marketplace.backend.repository.ListingCategoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@DisplayName("Listing Category Service Unit Tests")
@ExtendWith(MockitoExtension.class)
public class ListingCategoryServiceTests {
    @Mock
    private ListingCategoryRepository listingCategoryRepository;
    @Mock
    ListingCategoryMapper listingCategoryMapper;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    private ListingCategoryService listingCategoryService;

    @BeforeEach
    void setUp(){
        listingCategoryService = new ListingCategoryService(
                eventPublisher,
                listingCategoryRepository,
                listingCategoryMapper
        );
    }

    @Test
    public void givenListingCategoryName_whenSaveListingCategory_thenReturnsSavedCategoryDTO() {
        // Given
        String categoryName = "listingCategory";
        ListingCategory listingCategory = ListingCategory.builder()
                .id(1L)
                .name(categoryName)
                .active(false)
                .build();

        ListingCategoryDTO listingCategoryDTO = ListingCategoryDTO.builder()
                .id(1L)
                .name(categoryName)
                .active(false)
                .build();

        when(listingCategoryRepository.findAll()).thenReturn(Collections.emptyList());
        when(listingCategoryRepository.save(any(ListingCategory.class))).thenReturn(listingCategory);
        when(listingCategoryMapper.toListingCategoryDTO(any(ListingCategory.class))).thenReturn(listingCategoryDTO);

        // When
        ListingCategoryDTO savedListingCategory = listingCategoryService.saveListingCategory(categoryName);

        // Then
        Assertions.assertThat(savedListingCategory).isNotNull();
        Assertions.assertThat(savedListingCategory.getId()).isEqualTo(1L);
        Assertions.assertThat(savedListingCategory.getName()).isEqualTo(categoryName);
        Assertions.assertThat(savedListingCategory.getActive()).isEqualTo(false);
    }

    @Test
    public void givenListingCategoryInvalidName_whenSaveListingCategory_thenReturnsEntityAlreadyExistsException() {
        // Given
        String categoryName = "Sports";
        ListingCategory listingCategory = ListingCategory.builder()
                .id(1L)
                .name(categoryName)
                .active(false)
                .build();

        List<ListingCategory> results = List.of(listingCategory);

        when(listingCategoryRepository.findAll()).thenReturn(results);

        System.out.println(listingCategoryRepository
                .findAll()
                .stream()
                .anyMatch(lc -> lc.getName().equals(categoryName)));

        // When - Then
        Assertions.assertThatThrownBy(() -> listingCategoryService.saveListingCategory(categoryName))
                .isInstanceOf(EntityAlreadyExistsException.class)
                .hasMessageContaining("Listing Category '" + categoryName + "' already exists");
    }

    @Test
    public void whenFetchAllListingCategories_thenReturnsAllListingCategories(){
        // Given
        ListingCategory categorySports = ListingCategory.builder()
                .id(1L)
                .name("Sports")
                .active(false)
                .build();
        ListingCategory categoryTech = ListingCategory.builder()
                .id(2L)
                .name("Tech")
                .active(true)
                .build();
        List<ListingCategory> results = List.of(categorySports, categoryTech);

        ListingCategoryDTO categorySportsDto = ListingCategoryDTO.builder()
                .id(1L)
                .name("Sports")
                .active(false)
                .build();
        ListingCategoryDTO categoryTechDto = ListingCategoryDTO.builder()
                .id(2L)
                .name("Tech")
                .active(true)
                .build();
        List<ListingCategoryDTO> resultsDto = List.of(categorySportsDto, categoryTechDto);

        when(listingCategoryRepository.findAllByOrderByNameAsc()).thenReturn(results);
        when(listingCategoryMapper.toListingCategoryDTOList(any())).thenReturn(resultsDto);

        // When
        List<ListingCategoryDTO> foundListings = listingCategoryService.fetchAllListingCategories();

        // Then
        Assertions.assertThat(foundListings).isEqualTo(resultsDto);
    }

    @Test
    public void whenFetchAllActiveListingCategories_thenReturnsAllActiveListingCategories(){
        // Given
        ListingCategory categoryTech = ListingCategory.builder()
                .id(2L)
                .name("Tech")
                .active(true)
                .build();
        List<ListingCategory> results = List.of(categoryTech);

        ActiveListingCategoryDTO categoryTechDto = ActiveListingCategoryDTO.builder()
                .id(2L)
                .name("Tech")
                .build();
        List<ActiveListingCategoryDTO> resultsDto = List.of(categoryTechDto);

        when(listingCategoryRepository.findAllByActiveIsTrueOrderByNameAsc()).thenReturn(results);
        when(listingCategoryMapper.toActiveListingCategoryDTOList(any())).thenReturn(resultsDto);

        // When
        List<ActiveListingCategoryDTO> foundListings = listingCategoryService.fetchAllActiveListingCategories();

        // Then
        Assertions.assertThat(foundListings).isEqualTo(resultsDto);
    }

    @Test
    public void givenCategoryInfo_whenUpdateListingCategoryActiveStatus_thenStatusIsUpdated(){
        // Given
        Long categoryId = 1L;
        Boolean newActiveStatus = true;
        ListingCategory categorySports = ListingCategory.builder()
                .id(categoryId)
                .name("Sports")
                .active(false)
                .build();

        ListingCategory categorySportsUpdated = ListingCategory.builder()
                .id(categoryId)
                .name("Sports")
                .active(newActiveStatus)
                .build();

        ListingCategoryDTO categorySportsUpdatedDto = ListingCategoryDTO.builder()
                .id(categoryId)
                .name("Sports")
                .active(newActiveStatus)
                .build();

        when(listingCategoryRepository.findById(categoryId)).thenReturn(Optional.ofNullable(categorySports));
        when(listingCategoryRepository.save(any(ListingCategory.class))).thenReturn(categorySportsUpdated);
        when(listingCategoryMapper.toListingCategoryDTO(any(ListingCategory.class))).thenReturn(categorySportsUpdatedDto);

        // When
        ListingCategoryDTO resultCategory = listingCategoryService.updateListingCategoryActiveStatus(categoryId, newActiveStatus);

        // Then
        Assertions.assertThat(resultCategory.getId()).isEqualTo(categorySportsUpdatedDto.getId());
        Assertions.assertThat(resultCategory.getActive()).isEqualTo(categorySportsUpdatedDto.getActive());
    }

    @Test
    public void givenInvalidId_whenUpdateListingCategoryActiveStatus_thenReturnsEntityNotFoundException(){
        // Given
        Long categoryId = 1L;
        Boolean newActiveStatus = true;

        when(listingCategoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When - Then
        Assertions.assertThatThrownBy(() -> listingCategoryService.updateListingCategoryActiveStatus(categoryId,newActiveStatus))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Listing Category with id " + categoryId + " wasn't found");
    }

    @Test
    public void givenInvalidStatus_whenUpdateListingCategoryActiveStatus_thenStatusIsUpdated(){
        // Given
        Long categoryId = 1L;
        Boolean newActiveStatus = false;
        ListingCategory categorySports = ListingCategory.builder()
                .id(categoryId)
                .name("Sports")
                .active(false)
                .build();

        when(listingCategoryRepository.findById(categoryId)).thenReturn(Optional.ofNullable(categorySports));

        // When - Then
        Assertions.assertThatThrownBy(() -> listingCategoryService.updateListingCategoryActiveStatus(categoryId,newActiveStatus))
                .isInstanceOf(EntityAttributeAlreadySetException.class)
                .hasMessageContaining("Active status of Listing Category with id " + categoryId + " is already set to " + newActiveStatus);
    }

    @Test
    public void givenCategoryInfo_whenUpdateListingCategoryName_thenNameIsUpdated(){
        // Given
        Long categoryId = 1L;
        String newName = "Tech";
        ListingCategory categorySports = ListingCategory.builder()
                .id(categoryId)
                .name("Sports")
                .active(false)
                .build();

        ListingCategory categorySportsUpdated = ListingCategory.builder()
                .id(categoryId)
                .name(newName)
                .active(false)
                .build();

        ListingCategoryDTO categorySportsUpdatedDto = ListingCategoryDTO.builder()
                .id(categoryId)
                .name(newName)
                .active(false)
                .build();

        when(listingCategoryRepository.findById(categoryId)).thenReturn(Optional.ofNullable(categorySports));
        when(listingCategoryRepository.findAll()).thenReturn(Collections.emptyList());
        when(listingCategoryRepository.save(any(ListingCategory.class))).thenReturn(categorySportsUpdated);
        when(listingCategoryMapper.toListingCategoryDTO(any(ListingCategory.class))).thenReturn(categorySportsUpdatedDto);

        // When
        ListingCategoryDTO resultCategory = listingCategoryService.updateListingCategoryName(categoryId, newName);

        // Then
        Assertions.assertThat(resultCategory.getId()).isEqualTo(categorySportsUpdatedDto.getId());
        Assertions.assertThat(resultCategory.getName()).isEqualTo(categorySportsUpdatedDto.getName());
    }

    @Test
    public void givenInvalidId_whenUpdateListingCategoryName_thenReturnEntityNotFoundException(){
        // Given
        Long categoryId = 1L;
        String newName = "Tech";

        when(listingCategoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When - Then
        Assertions.assertThatThrownBy(() -> listingCategoryService.updateListingCategoryName(categoryId, newName))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Listing Category with id " + categoryId + " wasn't found");
    }

    @Test
    public void givenSameName_whenUpdateListingCategoryName_thenReturnEntityAttributeAlreadySetException(){
        // Given
        Long categoryId = 1L;
        String newName = "Sports";
        ListingCategory categorySports = ListingCategory.builder()
                .id(categoryId)
                .name("Sports")
                .active(false)
                .build();

        when(listingCategoryRepository.findById(categoryId)).thenReturn(Optional.ofNullable(categorySports));

        // When - Then
        Assertions.assertThatThrownBy(() -> listingCategoryService.updateListingCategoryName(categoryId, newName))
                .isInstanceOf(EntityAttributeAlreadySetException.class)
                .hasMessageContaining("Listing Category with id " + categoryId + " is already named '" + newName + "'");
    }

    @Test
    public void givenUsedName_whenUpdateListingCategoryName_thenReturnEntityAlreadyExistsException(){
        // Given
        Long categoryId = 1L;
        String newName = "Tech";
        ListingCategory categorySports = ListingCategory.builder()
                .id(categoryId)
                .name("Sports")
                .active(false)
                .build();
        ListingCategory categoryTech = ListingCategory.builder()
                .id(2L)
                .name("Tech")
                .active(false)
                .build();

        when(listingCategoryRepository.findById(categoryId)).thenReturn(Optional.ofNullable(categorySports));
        when(listingCategoryRepository.findAll()).thenReturn(List.of(categoryTech));

        // When - Then
        Assertions.assertThatThrownBy(() -> listingCategoryService.updateListingCategoryName(categoryId, newName))
                .isInstanceOf(EntityAlreadyExistsException.class)
                .hasMessageContaining("There's already a Listing Category named '" + newName + "'");
    }

    @Test
    public void givenCategoryId_whenLoadListingCategory_thenReturnsListingCategory(){
        // Given
        Long categoryId = 1L;
        ListingCategory category = ListingCategory.builder()
                .id(categoryId)
                .name("Sports")
                .active(false)
                .build();

        // When
        when(listingCategoryRepository.findById(categoryId)).thenReturn(Optional.ofNullable(category));
        ListingCategory foundCategory =  listingCategoryService.loadListingCategory(categoryId);

        // Then
        Assertions.assertThat(foundCategory).isNotNull();
        Assertions.assertThat(foundCategory.getId()).isEqualTo(category.getId());
    }

    @Test
    public void givenWrongCategoryId_whenLoadListingCategory_thenReturnEntityNotFoundException(){
        // Given
        Long wrongId = 1L;
        when(listingCategoryRepository.findById(wrongId)).thenReturn(Optional.empty());

        // When - Then
        Assertions.assertThatThrownBy(
                        () -> listingCategoryService.loadListingCategory(wrongId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Listing Category with id " + wrongId + " wasn't found");
    }
}
