package com.ugts.post.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import com.ugts.brand.repository.BrandRepository;
import com.ugts.cloudService.GoogleCloudStorageService;
import com.ugts.exception.AppException;
import com.ugts.exception.ErrorCode;
import com.ugts.post.dto.request.CreatePostRequest;
import com.ugts.post.dto.request.UpdatePostRequest;
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
    public PostResponse createPost(CreatePostRequest postRequest, MultipartFile[] files) throws IOException {
        // check brand existed
        var brand = brandRepository
                .findByName(postRequest.getBrand().getName())
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_EXISTED));

        // create product process
        var product = Product.builder()
                .name(postRequest.getProduct().getName())
                .brand(brand)
                .price(postRequest.getProduct().getPrice())
                .color(postRequest.getProduct().getColor())
                .size(postRequest.getProduct().getSize())
                .condition(postRequest.getProduct().getCondition())
                .material(postRequest.getProduct().getMaterial())
                .accessories(postRequest.getProduct().getAccessories())
                .dateCode(postRequest.getProduct().getDateCode())
                .serialNumber(postRequest.getProduct().getSerialNumber())
                .purchasedPlace(postRequest.getProduct().getPurchasedPlace())
                .isVerify(false)
                .build();

        var newProduct = productRepository.save(product);

        // get user from context holder
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
                .product(newProduct)
                .build();

        // save new post into database
        var newPost = postRepository.save(post);

        // upload product image to GCS
        List<String> fileUrls = googleCloudStorageService.uploadProductImagesToGCS(files, post.getId());

        // add product image for each URL
        for (String fileUrl : fileUrls) {
            // check if product image null
            if (product.getImages() == null) {
                product.setImages(new HashSet<>());
            }

            // add product image to product
            var productImage =
                    ProductImage.builder().product(product).imageUrl(fileUrl).build();
            product.getImages().add(productImage);
        }

        // save product into database
        productRepository.save(product);

        return postMapper.postToPostResponse(postRepository.save(newPost));
    }

    @Override
    public List<PostResponse> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return postMapper.getAllPosts(posts);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('USER')")
    public PostResponse updatePost(String postId, UpdatePostRequest request) {
        var post = postRepository.findById(postId).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        var product = productRepository
                .findById(request.getProduct().getId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        product.setName(request.getProduct().getName());
        product.setPrice(request.getProduct().getPrice());
        product.setColor(request.getProduct().getColor());
        product.setSize(request.getProduct().getSize());
        product.setCondition(request.getProduct().getCondition());
        product.setMaterial(request.getProduct().getMaterial());
        product.setAccessories(request.getProduct().getAccessories());
        product.setDateCode(request.getProduct().getDateCode());
        product.setSerialNumber(request.getProduct().getSerialNumber());
        product.setPurchasedPlace(request.getProduct().getPurchasedPlace());

        var updatedProduct = productRepository.save(product);

        post.setTitle(request.getTitle());
        post.setDescription(request.getDescription());
        post.setIsAvailable(true);
        post.setProduct(updatedProduct);
        post.setUpdatedAt(new Date());

        Post updatedPost = postRepository.save(post);
        return postMapper.postToPostResponse(updatedPost);
    }

    @Override
    public PostResponse getPostById(String postId) {
        var post = postRepository.findById(postId).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
        return postMapper.postToPostResponse(post);
    }

    @Override
    public List<PostResponse> getPostsByBrand(String brandName) {
        var posts = postRepository.findAllByBrandName(brandName);
        return postMapper.getAllPosts(posts);
    }
}
