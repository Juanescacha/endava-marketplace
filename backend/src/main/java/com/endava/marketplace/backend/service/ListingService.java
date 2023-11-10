package com.endava.marketplace.backend.service;

import com.endava.marketplace.backend.azure.StorageClient;
import com.endava.marketplace.backend.dto.ListingQuickSearchDTO;
import com.endava.marketplace.backend.dto.ListingWithImagesDTO;
import com.endava.marketplace.backend.dto.ListingDTO;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ListingService {
    private final ListingRepository listingRepository;

    @Getter
    private final ListingStatusService listingStatusService;

    private final ListingMapper listingMapper;

    private final StorageClient storageClient;

    public ListingService(
            ListingRepository listingRepository,
            ListingStatusService listingStatusService,
            StorageClient storageClient,
            ListingMapper listingMapper
    ) {
        this.listingRepository = listingRepository;
        this.listingStatusService = listingStatusService;
        this.storageClient = storageClient;
        this.listingMapper = listingMapper;
    }

    public ListingDTO saveListing(Listing listing) {
        return listingMapper.toListingDTO(listingRepository.save(listing));
    }

    public void saveListing2(Listing listing) {
        listingRepository.save(listing);
    }

    public List<Listing> findAllListingsByCategory(ListingCategory listingCategory) {
        return listingRepository.findAllByCategory(listingCategory);
    }

    public ListingWithImagesDTO findListingById(Long listingId) {
        Optional<Listing> foundListing = listingRepository.findById(listingId);
        if(foundListing.isPresent()) {
            ListingWithImagesDTO listing = listingMapper.toListingWithImagesDTO(foundListing.get());
            listing.setImages(storageClient.fetchImagesURLS(listingId));
            listing.setThumbnail(storageClient.fetchThumbnailURL(listingId));
            return listing;
        }
        return null;
    }

    public Set<ListingQuickSearchDTO> findListingByName(String listingName) {
        return listingMapper.toListingQuickSearchDTOSet(listingRepository.findTop5ByNameContainsIgnoreCaseOrderByIdDesc(listingName));
    }

    public Page<Listing> findListings(Integer category, String name, Integer page) {
        int actualPage = (page == null) ? 0: page - 1;
        Sort.Order orderById = new Sort.Order(Sort.Direction.DESC, "id");
        Pageable pageWithTenElements = PageRequest.of(actualPage, 10, Sort.by(orderById));

        return listingRepository.findAll((root, query, builder) -> {
            Predicate predicate = builder.conjunction();
            if (category != null) {
                predicate = builder.and(predicate, ListingSpecification.withCategoryId(category).toPredicate(root, query, builder));
            }
            if (name != null && !name.isEmpty()) {
                predicate = builder.and(predicate, ListingSpecification.withName(name).toPredicate(root, query, builder));
            }
            return predicate;
        }, pageWithTenElements);
    }

    public void deleteListingById(Long listingId) {listingRepository.deleteById(listingId);}

    public boolean updateListingAtSaleCreation(Long listingId, Integer saleQuantity) {
        Optional<Listing> foundListing = listingRepository.findById(listingId);

        if(foundListing.isPresent()) {
            Listing listing = foundListing.get();
            Integer listingStock = listing.getStock();

            if(listingStock >= saleQuantity) {
                listing.setStock(listing.getStock() - saleQuantity);

                if(listingStock.equals(saleQuantity)) {
                    Optional<ListingStatus> foundListingStatus = listingStatusService.findListingStatusByName("Out of Stock");
                    foundListingStatus.ifPresent(listing::setStatus);
                }

                listingRepository.save(listing);
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }

        return true;
    }

    public void updateListingAtSaleCancellation(Long listingId, Integer saleQuantity) {
        Optional<Listing> foundListing = listingRepository.findById(listingId);

        if(foundListing.isPresent()) {
            Listing listing = foundListing.get();

            listing.setStock(listing.getStock() + saleQuantity);

            if(listing.getStatus().getName().equals("Out of Stock")) {
                Optional<ListingStatus> foundListingStatus = listingStatusService.findListingStatusByName("Available");

                foundListingStatus.ifPresent(listing::setStatus);
            }

            listingRepository.save(listing);
        }
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
}
