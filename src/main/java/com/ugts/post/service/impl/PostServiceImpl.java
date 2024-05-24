package com.ugts.post.service.impl;

import java.io.IOException;
import java.util.Date;

import com.ugts.brand.repository.BrandRepository;
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
    BrandRepository brandRepository;

    AwsS3Service storageService;

    PostMapper postMapper;

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


        // Fetch user and product
        var user = userRepository
                .findById(request.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        var brand = brandRepository.findByName(request.getBrand().getName()).orElse(null);

        String imageUrl = storageService.uploadFile(productImage);

        Product product = Product.builder()
                .name(request.getProductName())
                .brand(brand)
                .price(request.getProductPrice())
                .color(request.getProductColor())
                .size(request.getProductSize())
                .condition(request.getProductCondition())
                .material(request.getProductMaterial())
                .isVerify(false)
                .build();

        ProductImage image = new ProductImage(null, product, imageUrl);
        product.getImages().add(image);
        productRepository.save(product);

        Post post = Post.builder()
                .user(user)
                .title(request.getTitle())
                .description(request.getDescription())
                .isAvailable(true)
                .product(product)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        return postMapper.postToPostResponse(postRepository.save(post));
    }
}
