package com.endava.marketplace.backend.service;

import com.endava.marketplace.backend.model.ListingCategory;
import com.endava.marketplace.backend.repository.ListingCategoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListingCategoryServiceTests {
    @Mock
    private ListingCategoryRepository listingCategoryRepository;

    private ListingCategoryService listingCategoryService;

    @BeforeEach
    void setUp(){
        listingCategoryService = new ListingCategoryService(listingCategoryRepository);
    }

    @Test
    public void givenListingCategoryInfo_whenSaveListingCategory_thenReturnsSavedCategory() {
        ListingCategory listingCategory = ListingCategory.builder()
                .id(1L)
                .name("Category #1")
                .active(false)
                .listings(null)
                .build();

        when(listingCategoryRepository.save(Mockito.any(ListingCategory.class))).thenReturn(listingCategory);

        ListingCategory savedListingCategory = listingCategoryService.saveListingCategory(listingCategory);

        Assertions.assertThat(savedListingCategory).isNotNull();
        Assertions.assertThat(savedListingCategory.getId()).isGreaterThan(0);
        verify(listingCategoryRepository, times(1)).save(listingCategory);
    }
    @Test
    public void givenListingCategoryId_whenFindListingCategoryById_thenReturnsCategory(){
        Integer categoryId = 1;

        ListingCategory listingCategory = ListingCategory.builder()
                .id(1L)
                .name("Category #1")
                .active(false)
                .listings(null)
                .build();

        when(listingCategoryRepository.findById(categoryId)).thenReturn(Optional.ofNullable(listingCategory));
        Optional<ListingCategory> foundCategory = listingCategoryService.findListingCategoryById(categoryId);

        Assertions.assertThat(foundCategory).isNotNull();
        Assertions.assertThat(listingCategory.getId()).isEqualTo(foundCategory.get().getId());
    }
    @Test
    public void givenListingId_whenDeleteListings_thenDeletesListing(){
        Integer categoryID = 1;
        doNothing().when(listingCategoryRepository).deleteById(categoryID);

        listingCategoryService.deleteListingCategoryById(categoryID);

        verify(listingCategoryRepository, times(1)).deleteById(categoryID);
    }

}
