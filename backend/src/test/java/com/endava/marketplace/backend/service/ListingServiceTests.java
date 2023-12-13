package com.endava.marketplace.backend.service;

import com.endava.marketplace.backend.azure.StorageClient;
import com.endava.marketplace.backend.dto.*;
import com.endava.marketplace.backend.exception.EntityNotFoundException;
import com.endava.marketplace.backend.exception.InsufficientStockException;
import com.endava.marketplace.backend.exception.InvalidStatusException;
import com.endava.marketplace.backend.mapper.ListingMapper;
import com.endava.marketplace.backend.model.*;
import com.endava.marketplace.backend.repository.ListingRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@DisplayName("Listing Service Unit Tests")
public class ListingServiceTests {
    @Mock
    ListingRepository listingRepository;
    @Mock
    EndavanService endavanService;
    @Mock
    ListingCategoryService listingCategoryService;
    @Mock
    ListingStatusService listingStatusService;
    @Mock
    ListingMapper listingMapper;
    @Mock
    StorageClient storageClient;

    private ListingService listingService;

    private Endavan seller;
    private EndavanDTO sellerDTO;
    private ListingCategory categorySports;
    private ListingStatus draft;
    private ListingStatus available;
    private ListingStatus outOfStock;
    private Map<String, ListingStatus> statuses = new HashMap<>();

    @BeforeEach
    void setUp(){
        this.listingService = new ListingService(
                listingRepository,
                endavanService,
                listingCategoryService,
                listingStatusService,
                storageClient,
                listingMapper
        );

        RatingDTO ratingDTO = RatingDTO.builder().quantity(5).score(5.0).build();

        seller = Endavan.builder()
                .id(1L)
                .name("Endavan Seller")
                .email("seller@endava.com")
                .admin(false)
                .build();

        sellerDTO = EndavanDTO.builder()
                .id(1L)
                .name("Endavan Seller")
                .email("seller@endava.com")
                .rating(ratingDTO)
                .build();

        categorySports = ListingCategory.builder()
                .id(1L)
                .name("Sports")
                .active(true)
                .build();

        draft = ListingStatus.builder()
                .id(1L)
                .name("Draft")
                .build();

        available = ListingStatus.builder()
                .id(2L)
                .name("Available")
                .build();

        outOfStock = ListingStatus.builder()
                .id(3L)
                .name("Out of Stock")
                .build();

        statuses.put("Draft", draft);
        statuses.put("Available", available);
        statuses.put("Out of Stock", outOfStock);
    }

    @Test
    public void givenListingInfo_whenSaveListing_thenReturnsSavedListing() throws IOException {
        // Given
        List<MultipartFile> images = new ArrayList<>();
        List<String> urls = List.of("url1", "url2", "url3");
        String thumbUrl = "ThumbnailUrl";

        NewListingRequestDTO listingRequest = NewListingRequestDTO.builder()
                .id(1L)
                .seller_id(seller.getId())
                .category_id(categorySports.getId())
                .name("Listing Request")
                .detail("Listing Request Example")
                .price(500.0)
                .stock(5)
                .condition(10)
                .build();

        Listing listing = Listing.builder()
                .id(1L)
                .seller(seller)
                .category(categorySports)
                .name("Listing Request")
                .detail("Listing Request Example")
                .price(500.0)
                .stock(5)
                .condition(10)
                .build();

        ListingWithImagesDTO listingDto = ListingWithImagesDTO.builder()
                .id(1L)
                .seller(sellerDTO)
                .category(categorySports.getName())
                .name("Listing Request")
                .detail("Listing Request Example")
                .images(urls)
                .thumbnail(thumbUrl)
                .price(500.0)
                .stock(5)
                .condition(10)
                .build();

        when(listingMapper.toListing(listingRequest)).thenReturn(listing);
        when(listingStatusService.getListingStatuses()).thenReturn(statuses);
        when(storageClient.fetchImagesURLS(listing.getId())).thenReturn(urls);
        when(storageClient.fetchThumbnailURL(listing.getId())).thenReturn(thumbUrl);
        when(endavanService.loadEndavan(listing.getSeller().getId())).thenReturn(seller);
        when(listingCategoryService.loadListingCategory(listing.getCategory().getId())).thenReturn(categorySports);
        when(listingRepository.save(listing)).thenReturn(listing);
        doNothing().when(storageClient).uploadImages(images, listing.getId());
        when(listingMapper.toListingWithImagesDTO(listing)).thenReturn(listingDto);

        // When
        ListingWithImagesDTO savedListing = listingService.saveListing(listingRequest, images);

        // Then
        Assertions.assertThat(savedListing).isNotNull().isEqualTo(listingDto);
        verify(listingStatusService, times(2)).getListingStatuses();
        verify(listingMapper, times(1)).toListing(listingRequest);
        verify(storageClient, times(2)).fetchImagesURLS(listing.getId());
        verify(endavanService, times(1)).loadEndavan(seller.getId());
        verify(listingCategoryService, times(1)).loadListingCategory(categorySports.getId());
        verify(storageClient, times(1)).uploadImages(images, listing.getId());
        verify(listingMapper, times(1)).toListingWithImagesDTO(listing);

    }

    @Test
    public void givenListingWithoutCategory_whenSaveListing_thenThrowsInvalidStatusException() {
        // Given
        List<MultipartFile> images = new ArrayList<>();

        NewListingRequestDTO listingRequest = NewListingRequestDTO.builder()
                .id(1L)
                .seller_id(seller.getId())
                .category_id(null)
                .name("Listing Request")
                .detail("Listing Request Example")
                .price(500.0)
                .stock(5)
                .condition(10)
                .build();

        when(listingStatusService.getListingStatuses()).thenReturn(statuses);

        // When - Then
        Assertions.assertThatThrownBy(
                () -> listingService.saveListing(listingRequest, images))
                .isInstanceOf(InvalidStatusException.class)
                .hasMessageContaining("At least a Category has to be defined to save Listing as a draft");
    }

    @Test
    public void givenSellerId_whenFindListingDraftsBySellerId_thenReturnsListingDrafts(){
        // Given
        Long sellerId = seller.getId();

        Listing listing = Listing.builder()
                .id(1L)
                .name("Example Listing")
                .detail("Example listing for unit testing")
                .price(300.0)
                .stock(5)
                .condition(10)
                .seller(seller)
                .category(categorySports)
                .status(draft)
                .build();

        Set<Listing> data = new HashSet<>(List.of(listing));

        ListingDraftBySellerDTO listingDto = ListingDraftBySellerDTO.builder()
                .id(1L)
                .name("Example Listing")
                .detail("Example listing for unit testing")
                .category(categorySports.getName())
                .status(draft.getName())
                .build();

        Set<ListingDraftBySellerDTO> expectedResult = new HashSet<>(List.of(listingDto));

        when(listingStatusService.getListingStatuses()).thenReturn(statuses);
        when(listingRepository.findAllBySellerIdAndStatus(sellerId, draft)).thenReturn(data);
        when(listingMapper.toListingDraftBySellerDTOSet(data)).thenReturn(expectedResult);

        // When
        Set<ListingDraftBySellerDTO> foundListings = listingService.findListingDraftsBySellerId(sellerId);

        // Then
        Assertions.assertThat(foundListings).isNotNull().isNotEmpty().isEqualTo(expectedResult);
    }

    @Test
    public void givenListingId_whenFindListingById_thenReturnsListing(){
        // Given
        Long listingId = 1L;

        Listing listing = Listing.builder()
                .id(listingId)
                .name("Example Listing")
                .detail("Example listing for unit testing")
                .price(300.0)
                .stock(5)
                .condition(10)
                .seller(seller)
                .category(categorySports)
                .status(draft)
                .build();

        ListingWithImagesDTO listingWithoutImagesDto = ListingWithImagesDTO.builder()
                .id(listingId)
                .name("Example Listing")
                .detail("Example listing for unit testing")
                .price(300.0)
                .stock(5)
                .condition(10)
                .seller(sellerDTO)
                .category(categorySports.getName())
                .status(draft.getName())
                .build();

        ListingWithImagesDTO listingWithImagesDto = ListingWithImagesDTO.builder()
                .id(listingId)
                .name("Example Listing")
                .detail("Example listing for unit testing")
                .price(300.0)
                .stock(5)
                .condition(10)
                .seller(sellerDTO)
                .category(categorySports.getName())
                .status(draft.getName())
                .images(List.of("Example", "Images", "URLs"))
                .thumbnail("Thumbnail URL")
                .build();

        when(listingRepository.findById(listingId)).thenReturn(Optional.ofNullable(listing));
        when(listingMapper.toListingWithImagesDTO(listing)).thenReturn(listingWithoutImagesDto);
        when(storageClient.fetchImagesURLS(listingId)).thenReturn(List.of("Example", "Images", "URLs"));
        when(storageClient.fetchThumbnailURL(listingId)).thenReturn("Thumbnail URL");

        // When
        ListingWithImagesDTO foundListing = listingService.findListingById(listingId);

        // Then
        Assertions.assertThat(foundListing).isNotNull().isEqualTo(listingWithImagesDto);
    }

    @Test
    public void givenWrongListingId_whenFindListingById_thenReturnsListing(){
        // Given
        Long listingId = 1L;

        when(listingRepository.findById(listingId)).thenReturn(Optional.empty());

        // When - Then
        Assertions.assertThatThrownBy(
                () -> listingService.findListingById(listingId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Listing with ID: " + listingId + " wasn't found");
    }

    @Test
    public void givenListingName_whenFindListingByName_thenReturnsListings(){
        // Given
        String listingName = "Listing";
        Long listingId = 1L;

        Listing listing = Listing.builder()
                .id(listingId)
                .name("Example Listing")
                .build();
        Set<Listing> data = new HashSet<>(List.of(listing));

        ListingQuickSearchDTO listingQuickDto = ListingQuickSearchDTO.builder()
                .id(listingId)
                .name("Example Listing")
                .build();

        Set<ListingQuickSearchDTO> expectedResult = new HashSet<>(List.of(listingQuickDto));

        when(listingStatusService.getListingStatuses()).thenReturn(statuses);
        when(listingRepository.findTop5ByNameContainsIgnoreCaseAndStatusOrderByIdDesc(listingName, available)).thenReturn(data);
        when(listingMapper.toListingQuickSearchDTOSet(data)).thenReturn(expectedResult);

        // When
        Set<ListingQuickSearchDTO> foundListings = listingService.findListingByName(listingName);

        // Then
        Assertions.assertThat(foundListings).isNotNull().isEqualTo(expectedResult);
    }

    @Test
    public void givenListingId_whenDeleteListing_thenDeletesListing(){
        // Given
        Long endavanID = 1L;
        doNothing().when(listingRepository).deleteById(endavanID);

        // When
        listingService.deleteListingById(endavanID);

        // Then
        verify(listingRepository, times(1)).deleteById(endavanID);
    }

    @Test
    public void givenImageFiles_whenSaveListingImages_thenUploadsImages() throws IOException {
        // Given
        Long listingID = 1L;
        List<MultipartFile> images = new ArrayList<>();

        doNothing().when(storageClient).uploadImages(images, listingID);

        // When
        listingService.saveListingImages(images, listingID);

        // Then
        verify(storageClient, times(1)).uploadImages(images, listingID);
    }

    @Test
    public void givenListingId_whenRetrieveListingImages_thenReturnsImageUrls(){
        // Given
        Long listingID = 1L;
        List<String> urls = List.of("image1UrlExample", "image2UrlExample", "image3UrlExample");

        when(storageClient.fetchImagesURLS(listingID)).thenReturn(urls);

        // When
        List<String> savedImages = listingService.retrieveListingImages(listingID);

        // Then
        Assertions.assertThat(savedImages).isNotNull();
        Assertions.assertThat(savedImages.size()).isEqualTo(3);
        Assertions.assertThat(urls).isEqualTo(savedImages);
        verify(storageClient, times(1)).fetchImagesURLS(listingID);
    }

    @Test
    public void givenListingId_whenRetrieveListingThumbnail_thenReturnsThumbnailUrl(){
        // Given
        Long listingID = 1L;
        String urls = "thumbnailUrlExample";

        when(storageClient.fetchThumbnailURL(listingID)).thenReturn(urls);

        // When
        String savedThumbnail = listingService.retrieveListingThumbnail(listingID);

        // Then
        Assertions.assertThat(savedThumbnail).isNotNull();
        Assertions.assertThat(urls).isEqualTo(savedThumbnail);
        verify(storageClient, times(1)).fetchThumbnailURL(listingID);
    }
    @Test
    public void givenListingId_whenLoadingListing_thenReturnListing() {
        // Given
        Long listingId = 1L;

        Listing listing = Listing.builder()
                .id(listingId)
                .name("Example Listing")
                .detail("Example listing for unit testing")
                .price(300.0)
                .stock(5)
                .condition(10)
                .seller(seller)
                .category(categorySports)
                .status(draft)
                .build();

        when(listingRepository.findById(listingId)).thenReturn(Optional.of(listing));

        // When
        Listing foundListing = listingService.loadListing(listingId);

        // Then
        Assertions.assertThat(foundListing).isNotNull().isEqualTo(listing);
        verify(listingRepository, times(1)).findById(listingId);
    }

    @Test
    public void givenListingIdAndSaleQuantity_whenUpdateListingAtSaleCreation_thenUpdatesListing(){
        // Given
        Long listingId = 1L;
        Integer saleQuantity = 5;

        Listing listing = Listing.builder()
                .id(listingId)
                .name("Example Listing")
                .detail("Example listing for unit testing")
                .price(300.0)
                .stock(saleQuantity)
                .condition(10)
                .seller(seller)
                .category(categorySports)
                .status(draft)
                .build();

        when(listingRepository.findById(listingId)).thenReturn(Optional.ofNullable(listing));
        when(listingStatusService.getListingStatuses()).thenReturn(statuses);

        // When
        listingService.updateListingAtSaleCreation(listingId, saleQuantity);

        // Then
        verify(listingRepository, times(1)).save(listing);
        verify(listingStatusService, times(1)).getListingStatuses();
    }

    @Test
    public void givenWrongListingId_whenUpdateListingAtSaleCreation_thenThrowsEntityNotFoundException(){
        // Given
        Long listingId = 1L;
        Integer saleQuantity = 5;

        when(listingRepository.findById(listingId)).thenReturn(Optional.empty());

        // When - Then
        Assertions.assertThatThrownBy(
                () -> listingService.updateListingAtSaleCreation(listingId, saleQuantity))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Listing with id " + listingId + " wasn't found");

    }

    @Test
    public void givenListingIdAndSaleQuantity_whenUpdateListingAtSaleCreation_andNotEnoughStock_thenThrowsInsufficientStockException(){
        // Given
        Long listingId = 1L;
        int saleQuantity = 5;

        Listing listing = Listing.builder()
                .id(listingId)
                .name("Example Listing")
                .detail("Example listing for unit testing")
                .price(300.0)
                .stock(saleQuantity-1)
                .condition(10)
                .seller(seller)
                .category(categorySports)
                .status(draft)
                .build();

        when(listingRepository.findById(listingId)).thenReturn(Optional.ofNullable(listing));

        // When - Then
        Assertions.assertThatThrownBy(
                        () -> listingService.updateListingAtSaleCreation(listingId, saleQuantity))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessageContaining("Listing with ID: " + listingId + " doesn't have enough stock");
    }

    @Test
    public void givenListingIdAndSaleQuantity_whenUpdateListingAtSaleCancellation_thenUpdatesListing(){
        // Given
        Long listingId = 1L;
        Integer saleQuantity = 5;

        Listing listing = Listing.builder()
                .id(listingId)
                .name("Example Listing")
                .detail("Example listing for unit testing")
                .price(300.0)
                .stock(0)
                .condition(10)
                .seller(seller)
                .category(categorySports)
                .status(outOfStock)
                .build();

        when(listingRepository.findById(listingId)).thenReturn(Optional.ofNullable(listing));
        when(listingStatusService.getListingStatuses()).thenReturn(statuses);

        // When
        listingService.updateListingAtSaleCancellation(listingId, saleQuantity);

        // Then
        verify(listingRepository, times(1)).save(listing);
        verify(listingStatusService, times(2)).getListingStatuses();
    }

    @Test
    public void givenWrongListingIdAndSaleQuantity_whenUpdateListingAtSaleCancellation_thenThrowsEntityNotFoundException(){
        // Given
        Long listingId = 1L;
        Integer saleQuantity = 5;

        when(listingRepository.findById(listingId)).thenReturn(Optional.empty());

        // When - Then
        Assertions.assertThatThrownBy(
                () -> listingService.updateListingAtSaleCancellation(listingId, saleQuantity))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Listing with id " + listingId + " wasn't found");
    }

    @Test
    public void givenListingInfo_whenUpdateListing_thenUpdatesListing(){
        // Given
        Listing listing = Listing.builder()
                .id(1L)
                .name("Example Listing 1")
                .detail("Example listing for unit testing")
                .price(300.0)
                .stock(5)
                .condition(10)
                .seller(seller)
                .category(categorySports)
                .status(available)
                .build();

        when(listingRepository.save(listing)).thenReturn(listing);

        // When
        listingService.updateListing(listing);

        // Then
        verify(listingRepository, times(1)).save(listing);
    }

    @Test
    public void givenListingCategory_whenFindAllListingsByCategory_thenReturnListings(){
        // Given
        Listing listingOne = Listing.builder()
                .id(1L)
                .name("Example Listing 1")
                .detail("Example listing for unit testing")
                .price(300.0)
                .stock(5)
                .condition(10)
                .seller(seller)
                .category(categorySports)
                .status(available)
                .build();
        Listing listingTwo = Listing.builder()
                .id(2L)
                .name("Example Listing 2")
                .detail("Example listing for unit testing")
                .price(300.0)
                .stock(5)
                .condition(10)
                .seller(seller)
                .category(categorySports)
                .status(available)
                .build();
        List<Listing> data = List.of(listingOne, listingTwo);

        when(listingRepository.findAllByCategory(categorySports)).thenReturn(data);

        // When
        List<Listing> foundListings = listingService.findAllListingsByCategory(categorySports);

        // Then
        Assertions.assertThat(foundListings).isNotNull().isEqualTo(data);
    }

    @Test
    public void givenWrongListingId_whenLoadingListing_thenReturnEntityNotFoundException() {
        // Given
        Long listingId = 1L;

        when(listingRepository.findById(listingId)).thenReturn(Optional.empty());

        // When - Then
        Assertions.assertThatThrownBy(
                () -> listingService.loadListing(listingId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Listing with ID: " + listingId + "wasn't found");

    }
}
