package com.ugts.post.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;

import com.ugts.brand.repository.BrandRepository;
import com.ugts.cloudService.GoogleCloudStorageService;
import com.ugts.exception.AppException;
import com.ugts.exception.ErrorCode;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImpl implements PostService {

    PostRepository postRepository;

    ProductRepository productRepository;

    BrandRepository brandRepository;

    PostMapper postMapper;

    GoogleCloudStorageService googleCloudStorageService;

    UserRepository userRepository;

    @Override
    @Transactional
    @PreAuthorize("hasRole('USER')")
    public PostResponse createPost(CreatePostRequest postRequest, MultipartFile file) throws IOException {
        // check brand existed
        var brand = brandRepository
                .findByName(postRequest.getBrand().getName())
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_EXISTED));

        // create product process
        var product = Product.builder()
                .name(postRequest.getProductName())
                .brand(brand)
                .price(postRequest.getProductPrice())
                .color(postRequest.getProductColor())
                .size(postRequest.getProductSize())
                .condition(postRequest.getProductCondition())
                .material(postRequest.getProductMaterial())
                .isVerify(false)
                .build();

        var contextHolder = SecurityContextHolder.getContext();
        String phoneNumber = contextHolder.getAuthentication().getName();

        var user = userRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // create post process
        var post = Post.builder()
                .user(user)
                .title(postRequest.getTitle())
                .description(postRequest.getDescription())
                .isAvailable(true)
                .createdAt(new Date())
                .updatedAt(new Date())
                .product(product)
                .build();

        // upload product image to GCS
        String fileUrl = googleCloudStorageService.uploadFileToGCS(file, postRequest.getId());

        // add product image
        var productImage =
                ProductImage.builder().product(product).imageUrl(fileUrl).build();

        // check if product image null
        if (product.getImages() == null) {
            product.setImages(new HashSet<>());
        }

        // add image to product
        product.getImages().add(productImage);

        productRepository.save(product);

        return postMapper.postToPostResponse(postRepository.save(post));
    }
}