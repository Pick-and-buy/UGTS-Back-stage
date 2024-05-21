package com.ugts.post.service.impl;

import java.io.IOException;
import java.util.Date;

import com.ugts.exception.AppException;
import com.ugts.exception.ErrorCode;
import com.ugts.post.dto.request.CreatePostRequest;
import com.ugts.post.dto.response.PostResponse;
import com.ugts.post.entity.Post;
import com.ugts.post.mapper.PostMapper;
import com.ugts.post.repository.PostRepository;
import com.ugts.post.service.GoogleCloudStorageService;
import com.ugts.post.service.PostService;
import com.ugts.product.entity.Product;
import com.ugts.product.entity.ProductImage;
import com.ugts.product.repository.ProductRepository;
import com.ugts.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImpl implements PostService {

    PostRepository postRepository;

    UserRepository userRepository;

    ProductRepository productRepository;

    GoogleCloudStorageService storageService;

    PostMapper postMapper;

    @PreAuthorize("hasRole('USER')")
    @Override
    public PostResponse createPost(CreatePostRequest request) throws IOException {
        // Upload image to Google Cloud Storage
        MultipartFile image = request.getImage();
        String imageUrl = storageService.uploadFile(image);

        // Fetch user and product
        var user = userRepository
                .findById(String.valueOf(request.getUser().getId()))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        var product = productRepository.findByName(request.getProductName()).orElse(null);

        if (product == null) {
            product = new Product();
            product.setName(request.getProductName());
            product.setPrice(request.getProductPrice());
            product = productRepository.save(product);
        }

        // Add image to product
        ProductImage productImage = new ProductImage();
        productImage.setProduct(product);
        productImage.setImageUrl(imageUrl);
        product.getImages().add(productImage);

        Post post = Post.builder()
                .user(user)
                .title(request.getTitle())
                .description(request.getDescription())
                .isAvailable(request.getIsAvailable())
                .product(product)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        // Save post
        var newPost = postRepository.save(post);
        return postMapper.postToPostResponse(newPost);
    }
}
