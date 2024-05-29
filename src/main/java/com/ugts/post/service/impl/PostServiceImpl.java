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
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImpl implements PostService {

    AmazonS3 amazonS3;

    PostRepository postRepository;

    ProductRepository productRepository;

    PostMapper postMapper;

    @NonFinal
    @Value("${aws.s3.bucket-name}")
    String awsBucketName;

    @Transactional
    @PreAuthorize("hasRole('USER')")
    public PostResponse savePost(CreatePostRequest postRequest, MultipartFile file) throws IOException {
        String fileUrl = uploadFileToS3(file);
        Product product = saveProductWithImage(createProduct(postRequest), fileUrl);
        Post post = createPost(postRequest, product);
        return postMapper.postToPostResponse(postRepository.save(post));
    }

    // Create a new post
    private Post createPost(CreatePostRequest postRequest, Product product) {
        return Post.builder()
                .user(postRequest.getUser())
                .title(postRequest.getTitle())
                .description(postRequest.getDescription())
                .isAvailable(postRequest.getIsAvailable())
                .product(product)
                .build();
    }

    // Create a new product
    private Product createProduct(CreatePostRequest postRequest) {
        return Product.builder()
                .name(postRequest.getProductName())
                .brand(postRequest.getBrand())
                .price(postRequest.getProductPrice())
                .color(postRequest.getProductColor())
                .size(postRequest.getProductSize())
                .condition(postRequest.getProductCondition())
                .material(postRequest.getProductMaterial())
                .isVerify(postRequest.getIsVerify())
                .build();
    }

    // save product image to database
    private Product saveProductWithImage(Product product, String imageUrl) {
        var productImage = ProductImage.builder()
                .imageUrl(imageUrl)
                .build();
        if (product.getImages() == null) {
            product.setImages(new HashSet<>());
        }
        product.getImages().add(productImage);
        return productRepository.save(product);
    }

    // upload file to s3
    private String uploadFileToS3(MultipartFile multipartFile) throws IOException {
        String fileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(multipartFile.getBytes())) {
            PutObjectRequest putObjectRequest = new PutObjectRequest(awsBucketName, fileName, inputStream, null);
            amazonS3.putObject(putObjectRequest);
        }

        return amazonS3.getUrl(awsBucketName, fileName).toString();
    }
}
