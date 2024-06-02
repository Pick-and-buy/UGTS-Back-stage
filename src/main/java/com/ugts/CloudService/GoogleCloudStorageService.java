package com.ugts.CloudService;

import java.io.IOException;
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

    public String uploadFileToGCS(MultipartFile multipartFile) throws IOException {
        String fileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(multipartFile.getContentType())
                .build();
        byte[] fileBytes = multipartFile.getBytes();
        Blob blob = storage.create(blobInfo, fileBytes);
        return String.format("https://storage.googleapis.com/%s/%s", bucketName, blob.getName());
    }
}
