package com.ugts.post.service;

import java.io.IOException;
import java.util.UUID;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class GoogleCloudStorageService {

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    public String uploadFile(MultipartFile file) throws IOException {
        Storage storage = StorageOptions.getDefaultInstance().getService();
        Bucket bucket = storage.get(bucketName);
        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        Blob blob = bucket.create(fileName, file.getBytes(), file.getContentType());
        return blob.getMediaLink();
    }
}
