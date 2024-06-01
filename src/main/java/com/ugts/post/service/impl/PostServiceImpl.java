package com.ugts.post.service.impl;

import java.io.IOException;
import java.util.HashSet;

import com.google.cloud.storage.Storage;
import com.ugts.CloudService.GoogleCloudStorageService;
import com.ugts.post.dto.request.CreatePostRequest;
import com.ugts.post.dto.response.PostResponse;
import com.ugts.post.entity.Post;
import com.ugts.post.mapper.PostMapper;
import com.ugts.post.repository.PostRepository;
import com.ugts.post.service.PostService;
import com.ugts.product.entity.Product;
import com.ugts.product.entity.ProductImage;
import com.ugts.product.repository.ProductRepository;
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
public class PostServiceImpl implements PostService {

    Storage storage;

    PostRepository postRepository;

    ProductRepository productRepository;

    PostMapper postMapper;

    GoogleCloudStorageService googleCloudStorageService;

    @NonFinal
    @Value("${google.cloud.storage.bucket}")
    String bucketName;

    @Override
    public PostResponse createPost(CreatePostRequest postRequest, MultipartFile file) throws IOException {
        String fileUrl = googleCloudStorageService.uploadFileToGCS(file);

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

        var post = Post.builder()
                .user(postRequest.getUser())
                .title(postRequest.getTitle())
                .description(postRequest.getDescription())
                .isAvailable(postRequest.getIsAvailable())
                .product(savedProduct)
                .build();

        return postMapper.postToPostResponse(postRepository.save(post));
    }
}
