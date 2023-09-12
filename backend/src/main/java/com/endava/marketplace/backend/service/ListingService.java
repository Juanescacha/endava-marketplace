package com.endava.marketplace.backend.service;

import com.endava.marketplace.backend.azure.StorageClient;
import com.endava.marketplace.backend.model.Listing;
import com.endava.marketplace.backend.repository.ListingRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ListingService {
    private final ListingRepository listingRepository;

    private final StorageClient storageClient;

    public ListingService(ListingRepository listingRepository, StorageClient storageClient) {
        this.listingRepository = listingRepository;
        this.storageClient = storageClient;
    }

    public Listing saveListing(Listing listing) {return listingRepository.save(listing);}

    public Optional<Listing> findListingById(Integer listingId) {return listingRepository.findById(listingId);}

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
