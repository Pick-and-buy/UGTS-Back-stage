package com.ugts.cloudService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GoogleCloudStorageService {

    Storage storage;

    @NonFinal
    @Value("${google.cloud.storage.bucket}")
    String bucketName;

    // General function to upload file to GCP
    private String uploadFileToGCSInternal(MultipartFile file, String folderName) throws IOException {
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        BlobId blobId = BlobId.of(bucketName, folderName + "/" + fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

        // convert file to bytes
        byte[] fileBytes = file.getBytes();

        // upload file to GCS
        Blob blob = storage.create(blobInfo, fileBytes);

        return String.format("https://storage.googleapis.com/%s/%s", bucketName, blob.getName());
    }

    // Upload multiple files to GCP
    public List<String> uploadFilesToGCS(MultipartFile[] multipartFiles, String folderName) throws IOException {
        List<String> uploadedFileUrls = new ArrayList<>();
        for (MultipartFile file : multipartFiles) {
            uploadedFileUrls.add(uploadFileToGCSInternal(file, folderName));
        }
        return uploadedFileUrls;
    }

    // Upload single file to GCP
    public String uploadFileToGCS(MultipartFile multipartFile, String folderName) throws IOException {
        return uploadFileToGCSInternal(multipartFile, folderName);
    }

    // Upload logo of brand to GCP
    public List<String> uploadBrandLogosToGCS(MultipartFile[] brandLogos, String brandId) throws IOException {
        String brandImagesFolder = "brand-images/" + brandId;
        return uploadFilesToGCS(brandLogos, brandImagesFolder);
    }

    // Upload product images to GCP
    public List<String> uploadProductImagesToGCS(MultipartFile[] productImages, String productId) throws IOException {
        String productImagesFolder = "product-images/" + productId;
        return uploadFilesToGCS(productImages, productImagesFolder);
    }

    // Upload user avatar to GCP
    public String uploadUserAvatarToGCS(MultipartFile avatar, String userId) throws IOException {
        String userAvatarsFolder = "user-avatars/" + userId;
        return uploadFileToGCS(avatar, userAvatarsFolder);
    }
}