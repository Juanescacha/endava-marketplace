package com.endava.marketplace.backend.service;

import com.endava.marketplace.backend.azure.StorageClient;
import com.endava.marketplace.backend.dto.*;
import com.endava.marketplace.backend.exception.EntityNotFoundException;
import com.endava.marketplace.backend.exception.InsufficientStockException;
import com.endava.marketplace.backend.exception.InvalidStatusException;
import com.endava.marketplace.backend.mapper.ListingMapper;
import com.endava.marketplace.backend.model.Listing;
import com.endava.marketplace.backend.model.ListingCategory;
import com.endava.marketplace.backend.model.ListingStatus;
import com.endava.marketplace.backend.repository.ListingRepository;
import com.endava.marketplace.backend.specification.ListingSpecification;
import jakarta.persistence.criteria.Predicate;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ListingService {
    private final ListingRepository listingRepository;

    private final EndavanService endavanService;

    private final ListingCategoryService listingCategoryService;

    @Getter
    private final ListingStatusService listingStatusService;

    private final ListingMapper listingMapper;

    private final StorageClient storageClient;

    public ListingService(
            ListingRepository listingRepository,
            EndavanService endavanService,
            ListingCategoryService listingCategoryService,
            ListingStatusService listingStatusService,
            StorageClient storageClient,
            ListingMapper listingMapper
    ) {
        this.listingRepository = listingRepository;
        this.endavanService = endavanService;
        this.listingCategoryService = listingCategoryService;
        this.listingStatusService = listingStatusService;
        this.storageClient = storageClient;
        this.listingMapper = listingMapper;
    }

    public ListingWithImagesDTO saveListing(NewListingRequestDTO newListingRequestDTO, List<MultipartFile> images) throws IOException {
        ListingStatus status = listingStatusService.getListingStatuses().get("Draft");
        List<String> imagesUrls = new ArrayList<>();
        Listing listing = listingMapper.toListing(newListingRequestDTO);

        if(listing.getCategory().getId() == null) {
            throw new InvalidStatusException("At least a Category has to be defined to save Listing as a draft");
        }

        if(listing.getId() != null) {
            imagesUrls = storageClient.fetchImagesURLS(listing.getId());
        }

        if(!listing.anyNull()) {
            if(images != null || !imagesUrls.isEmpty()) {
                status = listingStatusService.getListingStatuses().get("Available");
                listing.setDate(LocalDate.now());
            }
        }

        listing.setSeller(endavanService.loadEndavan(listing.getSeller().getId()));
        listing.setCategory(listingCategoryService.loadListingCategory(listing.getCategory().getId()));
        listing.setStatus(status);

        listing = listingRepository.save(listing);

        if(images != null) {
            storageClient.uploadImages(images, listing.getId());
            imagesUrls = storageClient.fetchImagesURLS(listing.getId());
        }

        ListingWithImagesDTO responseListing = listingMapper.toListingWithImagesDTO(listing);
        responseListing.setImages(imagesUrls);
        responseListing.setThumbnail(storageClient.fetchThumbnailURL(responseListing.getId()));

        return responseListing;
    }

    public Set<ListingDraftBySellerDTO> findListingDraftsBySellerId(Long id) {
        ListingStatus draft  = listingStatusService.getListingStatuses().get("Draft");
        return listingMapper.toListingDraftBySellerDTOSet(listingRepository.findAllBySellerIdAndStatus(id, draft));
    }

    public ListingWithImagesDTO findListingById(Long listingId) {
        Optional<Listing> foundListing = listingRepository.findById(listingId);

        if (foundListing.isEmpty()) {
            throw new EntityNotFoundException("Listing with ID: " + listingId + " wasn't found");
        }

        ListingWithImagesDTO listing = listingMapper.toListingWithImagesDTO(foundListing.get());
        listing.setImages(storageClient.fetchImagesURLS(listingId));
        listing.setThumbnail(storageClient.fetchThumbnailURL(listingId));
        return listing;
    }

    public Set<ListingQuickSearchDTO> findListingByName(String listingName) {
        ListingStatus available = listingStatusService.getListingStatuses().get("Available");
        return listingMapper.toListingQuickSearchDTOSet(listingRepository.findTop5ByNameContainsIgnoreCaseAndStatusOrderByIdDesc(listingName, available));
    }

    public Page<ListingPageDTO> findListings(Integer category, String name, Integer page) {
        Long availableStatusId = listingStatusService.getListingStatuses().get("Available").getId();
        int actualPage = (page == null) ? 0 : page - 1;
        Sort.Order orderById = new Sort.Order(Sort.Direction.DESC, "id");
        Pageable pageWithTenElements = PageRequest.of(actualPage, 10, Sort.by(orderById));

        Page<Listing> results = listingRepository.findAll((root, query, builder) -> {
            Predicate predicate = builder.conjunction();
            if (category != null) {
                predicate = builder.and(predicate, ListingSpecification.withCategoryId(category).toPredicate(root, query, builder));
            }
            if (name != null && !name.isEmpty()) {
                predicate = builder.and(predicate, ListingSpecification.withName(name).toPredicate(root, query, builder));
            }
            predicate = builder.and(predicate, ListingSpecification.withStatusId(availableStatusId).toPredicate(root, query, builder));
            return predicate;
        }, pageWithTenElements);

        return results.map(listing -> {
            ListingPageDTO listingDTO = listingMapper.toListingPageDTO(listing);
            listingDTO.setThumbnail(retrieveListingThumbnail(listingDTO.getId()));
            return listingDTO;
        });
    }

    public void deleteListingById(Long listingId) {
        listingRepository.deleteById(listingId);
    }

    public void saveListingImages(List<MultipartFile> images, Long listingId) throws IOException {
        storageClient.uploadImages(images, listingId);
    }

    public List<String> retrieveListingImages(Long listingId) {
        return storageClient.fetchImagesURLS(listingId);
    }

    public String retrieveListingThumbnail(Long listingId) {
        return storageClient.fetchThumbnailURL(listingId);
    }

    @Transactional
    public void updateListingAtSaleCreation(Long listingId, Integer saleQuantity) {
        Optional<Listing> foundListing = listingRepository.findById(listingId);

        if (foundListing.isEmpty()) {
            throw new EntityNotFoundException("Listing with id " + listingId + " wasn't found");
        }

        Listing listing = foundListing.get();
        Integer listingStock = listing.getStock();

        if (listingStock < saleQuantity) {
            throw new InsufficientStockException("Listing with ID: " + listingId + " doesn't have enough stock");
        }

        listing.setStock(listing.getStock() - saleQuantity);

        if (listingStock.equals(saleQuantity)) {
            listing.setStatus(listingStatusService.getListingStatuses().get("Out of Stock"));
        }

        listingRepository.save(listing);
    }

    @Transactional
    public void updateListingAtSaleCancellation(Long listingId, Integer saleQuantity) {
        Optional<Listing> foundListing = listingRepository.findById(listingId);

        if (foundListing.isEmpty()) {
            throw new EntityNotFoundException("Listing with id " + listingId + " wasn't found");
        }

        Listing listing = foundListing.get();
        listing.setStock(listing.getStock() + saleQuantity);

        if (listing.getStatus().equals(listingStatusService.getListingStatuses().get("Out of Stock"))) {
            listing.setStatus(listingStatusService.getListingStatuses().get("Available"));
        }

        listingRepository.save(listing);
    }

    public void updateListing(Listing listing) {
        listingRepository.save(listing);
    }

    public List<Listing> findAllListingsByCategory(ListingCategory listingCategory) {
        return listingRepository.findAllByCategory(listingCategory);
    }

    public Listing loadListing(Long id) {
        Optional<Listing> listing = listingRepository.findById(id);
        if(listing.isEmpty()) {
            throw new EntityNotFoundException("Listing with ID: " + id + "wasn't found");
        }
        return listing.get();
    }
}
