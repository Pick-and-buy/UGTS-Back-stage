package com.ugts.post.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.UUID;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ugts.post.dto.request.CreatePostRequest;
import com.ugts.post.dto.response.PostResponse;
import com.ugts.post.entity.Post;
import com.ugts.post.mapper.PostMapper;
import com.ugts.post.repository.PostRepository;
import com.ugts.post.service.PostService;
import com.ugts.product.entity.Product;
import com.ugts.product.entity.ProductImage;
import com.ugts.product.repository.ProductRepository;
import com.ugts.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImpl implements PostService {

    Storage storage;

    PostRepository postRepository;

    UserRepository userRepository;

    ProductRepository productRepository;

    GoogleCloudStorageService storageService;
    BrandRepository brandRepository;
    
    PostMapper postMapper;

    GoogleCloudStorageService googleCloudStorageService;

    @NonFinal
    @Value("${google.cloud.storage.bucket}")
    String bucketName;

    @Override
    public PostResponse createPost(CreatePostRequest postRequest, MultipartFile file) throws IOException {
        String fileUrl = googleCloudStorageService.uploadFileToGCS(file);
    @Transactional
    @PreAuthorize("hasRole('USER')")
    @Override
    public PostResponse createPost(CreatePostRequest request, MultipartFile productImage) throws IOException {
    public PostResponse createPost(CreatePostRequest request) throws IOException {
        // Upload image to Google Cloud Storage
        MultipartFile image = request.getImage();
        String imageUrl = null;
        if (image != null) {
            imageUrl = storageService.uploadFile(image);
        }

        var product = Product.builder()
                .name(postRequest.getProductName())
                .brand(postRequest.getBrand())
                .price(postRequest.getProductPrice())
                .color(postRequest.getProductColor())
                .size(postRequest.getProductSize())
                .condition(postRequest.getProductCondition())
                .material(postRequest.getProductMaterial())
                .isVerify(postRequest.getIsVerify())
                .build();

        var productImage = ProductImage.builder().imageUrl(fileUrl).build();

        if (product.getImages() == null) {
            product.setImages(new HashSet<>());
        }
        product.getImages().add(productImage);

        Product savedProduct = productRepository.save(product);

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(multipartFile.getBytes())) {
            PutObjectRequest putObjectRequest = new PutObjectRequest(awsBucketName, fileName, inputStream, null);
            amazonS3.putObject(putObjectRequest);
        }

        return amazonS3.getUrl(awsBucketName, fileName).toString();
    }
}
