package com.endava.marketplace.backend.azure;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.models.PublicAccessType;
import com.azure.storage.common.StorageSharedKeyCredential;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class StorageClient {
    @Value("${spring.cloud.azure.storage.account-name}")
    private String storageAccountName;

    @Value("${spring.cloud.azure.storage.account-key}")
    private String storageAccountKey;

    @Value("${spring.cloud.azure.storage.endpoint}")
    private String storageEndpoint;

    private BlobServiceClient serviceClient;

    @PostConstruct
    public void postConstruct() {
        StorageSharedKeyCredential credential = new StorageSharedKeyCredential(storageAccountName, storageAccountKey);
        serviceClient = new BlobServiceClientBuilder()
                .endpoint(storageEndpoint)
                .credential(credential)
                .buildClient();
    }

    public String createContainer(String containerName) {
        BlobContainerClient containerClient = serviceClient.createBlobContainer(containerName);
        containerClient.setAccessPolicy(PublicAccessType.BLOB, null);
        return containerClient.getBlobContainerName();
    }

    public void uploadImagesToContainer(List<MultipartFile> images, String containerName) throws IOException {
        BlobContainerClient containerClient = serviceClient.getBlobContainerClient(containerName);

        int count = 1;
        for(MultipartFile image: images) {
            String contentType = image.getContentType();
            String extension = contentType.split("/")[1];
            InputStream data = image.getInputStream();
            BlobHttpHeaders imageHeaders = new BlobHttpHeaders().setContentType(contentType);

            BlobClient blob = containerClient.getBlobClient(String.format("%s_%d.%s", containerClient.getBlobContainerName(), count, extension));

            blob.upload(data, image.getSize(), true);
            blob.setHttpHeaders(imageHeaders);

            count++;
        }
    }

    public List<String> getAllImageUrlsFromContainer(String containerName) {
        List<String> imageUrls = new ArrayList<>();

        BlobContainerClient containerClient = serviceClient.getBlobContainerClient(containerName);

        containerClient.listBlobs().forEach(blobItem -> imageUrls.add(containerClient.getBlobClient(blobItem.getName()).getBlobUrl()));

        return imageUrls;
    }
}
