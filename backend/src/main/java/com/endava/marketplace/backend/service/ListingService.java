package com.endava.marketplace.backend.service;

import com.endava.marketplace.backend.azure.StorageClient;
import com.endava.marketplace.backend.model.Listing;
import com.endava.marketplace.backend.model.ListingStatus;
import com.endava.marketplace.backend.repository.ListingRepository;
import com.endava.marketplace.backend.specification.ListingSpecification;
import jakarta.persistence.criteria.Predicate;
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

    private final ListingStatusService listingStatusService;

    private final StorageClient storageClient;

    public ListingService(ListingRepository listingRepository, ListingStatusService listingStatusService, StorageClient storageClient) {
        this.listingRepository = listingRepository;
        this.listingStatusService = listingStatusService;
        this.storageClient = storageClient;
    }

    public Listing saveListing(Listing listing) {return listingRepository.save(listing);}

    public Optional<Listing> findListingById(Long listingId) {return listingRepository.findById(listingId);}

    public Set<Listing> findListingByName(String listingName) {
        return listingRepository.findTop5ByNameContainsIgnoreCaseOrderByIdDesc(listingName);
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
        Optional<Listing> foundListing = findListingById(listingId);

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
        Optional<Listing> foundListing = findListingById(listingId);

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
