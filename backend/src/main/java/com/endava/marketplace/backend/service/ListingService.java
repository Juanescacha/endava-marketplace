package com.endava.marketplace.backend.service;

import com.endava.marketplace.backend.azure.StorageClient;
import com.endava.marketplace.backend.model.Listing;
import com.endava.marketplace.backend.model.ListingCategory;
import com.endava.marketplace.backend.repository.ListingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ListingService {
    private final ListingRepository listingRepository;
    private final ListingCategoryService listingCategoryService;

    private final StorageClient storageClient;

    public ListingService(ListingRepository listingRepository, ListingCategoryService listingCategoryService, StorageClient storageClient) {
        this.listingRepository = listingRepository;
        this.listingCategoryService = listingCategoryService;
        this.storageClient = storageClient;
    }

    public Listing saveListing(Listing listing) {return listingRepository.save(listing);}

    public Page<Listing> findAllListings(String page){
        int actualPage;
        if (page == null){
            actualPage = 0;
        }
        else{
            actualPage = Integer.parseInt(page) - 1;
        }
        Pageable pageWithTenElements = PageRequest.of(actualPage, 10);
        return listingRepository.findAll(pageWithTenElements);
    }

    public Optional<Listing> findListingById(Integer listingId) {return listingRepository.findById(listingId);}

    public Set<Listing> findListingByName(String listingName) {
        return listingRepository.findTop5ByNameContainsIgnoreCaseOrderByIdDesc(listingName);
    }

    public Page<Listing> findListingByCategoryAndName(String category, String name, String page) {
        int actualPage;
        if (page == null){
            actualPage = 0;
        }
        else{
            actualPage = Integer.parseInt(page) - 1;
        }
        Pageable pageWithTenElements = PageRequest.of(actualPage, 10);

        // Search with name and category
        if (name != null && category != null){
            // Get the values from the params
            Integer categoryId =  Integer.parseInt(category);

            // Get the category to search for the listings
            Optional<ListingCategory> listingCategory = listingCategoryService.findListingCategoryById(categoryId);
            if(listingCategory.isPresent()){
                return listingRepository.findListingsByNameContainsIgnoreCaseAndCategoryOrderByIdDesc(name, listingCategory.get(), pageWithTenElements);
            }
            return null;
        }
        // Search with name only
        else if(category == null && name != null){
            return listingRepository.findListingsByNameContainsIgnoreCaseOrderByIdDesc(name, pageWithTenElements);
        }
        // Search with category only
        else if(category != null){
            Integer categoryId =  Integer.parseInt(category);
            Optional<ListingCategory> listingCategory = listingCategoryService.findListingCategoryById(categoryId);
            if(listingCategory.isPresent()){
                return listingRepository.findListingsByCategoryOrderByIdDesc(listingCategory.get(), pageWithTenElements);
            }
            return null;
        }
        else {
            return findAllListings(page);
        }
    }

    public void deleteListingById(Integer listingId) {listingRepository.deleteById(listingId);}

    private String createListingImagesContainer(Integer id) {
        String containerName = "em" + id;
        return storageClient.createContainer(containerName);
    }

    public void saveListingImagesToContainer(List<MultipartFile> images, Integer id) throws IOException {
        String containerName = createListingImagesContainer(id);
        storageClient.uploadImagesToContainer(images, containerName);
    }

    public List<String> findListingImagesUrls(Integer id) {
        String containerName = "em" + id;
        return storageClient.getAllImageUrlsFromContainer(containerName);
    }
}
