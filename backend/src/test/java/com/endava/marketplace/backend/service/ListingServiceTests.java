package com.endava.marketplace.backend.service;

import com.endava.marketplace.backend.azure.StorageClient;
import com.endava.marketplace.backend.model.Listing;
import com.endava.marketplace.backend.repository.ListingRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ListingServiceTests {
    @Mock
    private ListingRepository listingRepository;
    @Mock
    private StorageClient storageClient;

    private ListingService listingService;

    @BeforeEach
    void setUp(){
        listingService = new ListingService(listingRepository, storageClient);
    }

    @Test
    public void givenListingInfo_whenSaveListing_thenReturnsSavedListing(){
        Listing listing = Listing.builder()
                .id(1L)
                .name("listing #1")
                .detail("example detail")
                .price(100000.0)
                .stock(1)
                .condition(5)
                .date(LocalDate.of(2023,10,9))
                .build();

        when(listingRepository.save(Mockito.any(Listing.class))).thenReturn(listing);

        Listing savedListing = listingService.saveListing(listing);

        Assertions.assertThat(savedListing).isNotNull();
        Assertions.assertThat(savedListing.getId()).isGreaterThan(0);
        verify(listingRepository, times(1)).save(listing);
    }

    @Test
    public void givenListingId_whenFindListingById_thenReturnsListing(){
        Integer listingId = 1;

        Listing listing = Listing.builder()
                .id(1L)
                .name("listing #1")
                .detail("example detail")
                .price(100000.0)
                .stock(1)
                .condition(5)
                .date(LocalDate.of(2023,10,9))
                .build();

        when(listingRepository.findById(listingId)).thenReturn(Optional.ofNullable(listing));
        Optional<Listing> foundListing = listingService.findListingById(listingId);

        Assertions.assertThat(foundListing).isNotNull();
        Assertions.assertThat(listing.getId()).isEqualTo(foundListing.get().getId());
    }

    @Test
    public void givenListingName_whenFindListingByName_thenReturnsListing(){
        String listingName = "Listing";

        Set<Listing> listings = new HashSet<>();

        Listing listingOne = Listing.builder()
                .id(1L)
                .name("listing #1")
                .detail("example detail")
                .price(100000.0)
                .stock(1)
                .condition(5)
                .date(LocalDate.of(2023,10,9))
                .build();

        Listing listingTwo = Listing.builder()
                .id(2L)
                .name("listing #2")
                .detail("example detail")
                .price(100000.0)
                .stock(1)
                .condition(5)
                .date(LocalDate.of(2023,10,9))
                .build();

        listings.add(listingOne);
        listings.add(listingTwo);

        when(listingRepository.findTop5ByNameContainsIgnoreCaseOrderByIdDesc(listingName)).thenReturn(listings);
        Set<Listing> foundListings = listingService.findListingByName(listingName);

        Assertions.assertThat(foundListings).isNotNull();
        Assertions.assertThat(listings).isEqualTo(foundListings);
    }

    @Test
    public void givenListingId_whenDeleteListings_thenDeletesListing(){
        Integer listingID = 1;
        doNothing().when(listingRepository).deleteById(listingID);

        listingService.deleteListingById(listingID);

        verify(listingRepository, times(1)).deleteById(listingID);
    }

    @Test
    public void givenImageFiles_whenSaveListingImages_thenUploadsImages() throws IOException {
        Long listingID = 1L;
        List<MultipartFile> images = new ArrayList<>();

        doNothing().when(storageClient).uploadImages(images, listingID);

        listingService.saveListingImages(images, listingID);

        verify(storageClient, times(1)).uploadImages(images, listingID);
    }

    @Test
    public void givenListingId_whenRetrieveListingImages_thenReturnsImageUrls(){
        Long listingID = 1L;
        List<String> urls = new ArrayList<>();
        urls.add("image1UrlExample");
        urls.add("image2UrlExample");
        urls.add("image3UrlExample");

        when(storageClient.fetchImagesURLS(listingID)).thenReturn(urls);

        List<String> savedImages = listingService.retrieveListingImages(listingID);

        Assertions.assertThat(savedImages).isNotNull();
        Assertions.assertThat(savedImages.size()).isEqualTo(3);
        Assertions.assertThat(urls).isEqualTo(savedImages);
        verify(storageClient, times(1)).fetchImagesURLS(listingID);
    }

    @Test
    public void givenListingId_whenRetrieveListingThumbnail_thenReturnsThumbnailUrl(){
        Long listingID = 1L;
        String urls = "thumbnailUrlExample";

        when(storageClient.fetchThumbnailURL(listingID)).thenReturn(urls);

        String savedThumbnail = listingService.retrieveListingThumbnail(listingID);

        Assertions.assertThat(savedThumbnail).isNotNull();
        Assertions.assertThat(urls).isEqualTo(savedThumbnail);
        verify(storageClient, times(1)).fetchThumbnailURL(listingID);
    }
}
