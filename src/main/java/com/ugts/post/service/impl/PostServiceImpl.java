package com.ugts.post.service.impl;

import java.io.IOException;
import java.util.*;

import com.ugts.brand.repository.BrandRepository;
import com.ugts.brandLine.repository.BrandLineRepository;
import com.ugts.category.repository.CategoryRepository;
import com.ugts.cloudService.GoogleCloudStorageService;
import com.ugts.exception.AppException;
import com.ugts.exception.ErrorCode;
import com.ugts.post.dto.request.CreatePostRequest;
import com.ugts.post.dto.request.UpdatePostRequest;
import com.ugts.post.dto.response.PostResponse;
import com.ugts.post.entity.Post;
import com.ugts.post.mapper.PostMapper;
import com.ugts.post.repository.PostRepository;
import com.ugts.post.service.IPostService;
import com.ugts.product.entity.Product;
import com.ugts.product.entity.ProductImage;
import com.ugts.product.entity.VerifiedLevel;
import com.ugts.product.repository.ProductRepository;
import com.ugts.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostServiceImpl implements IPostService {

    PostRepository postRepository;

    ProductRepository productRepository;

    BrandRepository brandRepository;

    PostMapper postMapper;

    GoogleCloudStorageService googleCloudStorageService;

    UserRepository userRepository;

    CategoryRepository categoryRepository;

    BrandLineRepository brandLineRepository;

    @Override
    @Transactional
    @PreAuthorize("hasRole('USER')")
    public PostResponse createPostLevel1(CreatePostRequest postRequest, MultipartFile[] productImages)
            throws IOException {
        // Validate the CreatePostRequest object
        if (postRequest == null
                || postRequest.getBrand() == null
                || postRequest.getBrandLine() == null
                || postRequest.getCategory() == null
                || postRequest.getProduct() == null) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }

        // check brand existed
        var brand = brandRepository
                .findByName(postRequest.getBrand().getName())
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_EXISTED));

        var brandLine = brandLineRepository
                .findByLineName(postRequest.getBrandLine().getLineName())
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_LINE_NOT_EXISTED));

        var category = categoryRepository
                .findByCategoryName(postRequest.getCategory().getCategoryName())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));

        // create product process
        var product = Product.builder()
                .name(postRequest.getProduct().getName())
                .brand(brand)
                .brandLine(brandLine)
                .category(category)
                .price(postRequest.getProduct().getPrice())
                .color(postRequest.getProduct().getColor())
                .size(postRequest.getProduct().getSize())
                .width(postRequest.getProduct().getWidth())
                .height(postRequest.getProduct().getHeight())
                .length(postRequest.getProduct().getLength())
                .referenceCode(postRequest.getProduct().getReferenceCode())
                .manufactureYear(postRequest.getProduct().getManufactureYear())
                .interiorMaterial(postRequest.getProduct().getInteriorMaterial())
                .exteriorMaterial(postRequest.getProduct().getExteriorMaterial())
                .condition(postRequest.getCondition())
                .accessories(postRequest.getProduct().getAccessories())
                .dateCode(postRequest.getProduct().getDateCode())
                .serialNumber(postRequest.getProduct().getSerialNumber())
                .purchasedPlace(postRequest.getProduct().getPurchasedPlace())
                .story(postRequest.getProduct().getStory())
                .verifiedLevel(VerifiedLevel.LEVEL_1)
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

        // upload product images to GCS
        List<String> fileUrls = googleCloudStorageService.uploadProductImagesToGCS(productImages, product.getId());

        // add product image for each URL
        for (String fileUrl : fileUrls) {
            // check if product image null
            if (product.getImages() == null) {
                product.setImages(new ArrayList<>());
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
    @Transactional
    @PreAuthorize("hasRole('USER')")
    public PostResponse createPostLevel2(
            CreatePostRequest postRequest,
            MultipartFile[] productImages,
            MultipartFile productVideo,
            MultipartFile originalReceiptProof)
            throws IOException {
        // Validate the CreatePostRequest object
        if (postRequest == null
                || postRequest.getBrand() == null
                || postRequest.getBrandLine() == null
                || postRequest.getCategory() == null
                || postRequest.getProduct() == null) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }

        // check brand existed
        var brand = brandRepository
                .findByName(postRequest.getBrand().getName())
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_EXISTED));

        var brandLine = brandLineRepository
                .findByLineName(postRequest.getBrandLine().getLineName())
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_LINE_NOT_EXISTED));

        var category = categoryRepository
                .findByCategoryName(postRequest.getCategory().getCategoryName())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));

        // create product process
        var product = Product.builder()
                .name(postRequest.getProduct().getName())
                .brand(brand)
                .brandLine(brandLine)
                .category(category)
                .price(postRequest.getProduct().getPrice())
                .color(postRequest.getProduct().getColor())
                .size(postRequest.getProduct().getSize())
                .width(postRequest.getProduct().getWidth())
                .height(postRequest.getProduct().getHeight())
                .length(postRequest.getProduct().getLength())
                .referenceCode(postRequest.getProduct().getReferenceCode())
                .manufactureYear(postRequest.getProduct().getManufactureYear())
                .interiorMaterial(postRequest.getProduct().getInteriorMaterial())
                .exteriorMaterial(postRequest.getProduct().getExteriorMaterial())
                .condition(postRequest.getCondition())
                .accessories(postRequest.getProduct().getAccessories())
                .dateCode(postRequest.getProduct().getDateCode())
                .serialNumber(postRequest.getProduct().getSerialNumber())
                .purchasedPlace(postRequest.getProduct().getPurchasedPlace())
                .story(postRequest.getProduct().getStory())
                .verifiedLevel(VerifiedLevel.LEVEL_2)
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

        // upload product images to GCS
        List<String> fileUrls = googleCloudStorageService.uploadProductImagesToGCS(productImages, product.getId());

        // add product image for each URL
        for (String fileUrl : fileUrls) {
            // check if product image null
            if (product.getImages() == null) {
                product.setImages(new ArrayList<>());
            }

            // add product image to product
            var productImage =
                    ProductImage.builder().product(product).imageUrl(fileUrl).build();
            product.getImages().add(productImage);
        }

        // upload product video to GCS
        String videoUrl = googleCloudStorageService.uploadProductVideoToGCS(productVideo, product.getId());
        product.setProductVideo(videoUrl);

        // upload originalReceiptProofUrls to GCS
        String originalReceiptProofUrls =
                googleCloudStorageService.uploadOriginalReceiptProofToGCS(originalReceiptProof, product.getId());
        product.setOriginalReceiptProof(originalReceiptProofUrls);

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
    public PostResponse updatePost(
            String postId,
            UpdatePostRequest request,
            MultipartFile[] productImages,
            MultipartFile productVideo,
            MultipartFile originalReceiptProof)
            throws IOException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        var product = productRepository
                .findById(request.getProduct().getId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        // upload product images to GCS
        List<String> imageUrls = googleCloudStorageService.uploadProductImagesToGCS(productImages, product.getId());

        // add product image for each URL
        for (String imageUrl : imageUrls) {
            // check if product image null
            if (product.getImages() == null) {
                product.setImages(new ArrayList<>());
            }

            // add product image to product
            var productImage =
                    ProductImage.builder().product(product).imageUrl(imageUrl).build();
            product.getImages().add(productImage);
        }

        // upload product video to GCS
        String videoUrl = googleCloudStorageService.uploadProductVideoToGCS(productVideo, product.getId());
        product.setProductVideo(videoUrl);

        // upload originalReceiptProofUrls to GCS
        String originalReceiptProofUrls =
                googleCloudStorageService.uploadOriginalReceiptProofToGCS(originalReceiptProof, product.getId());
        product.setOriginalReceiptProof(originalReceiptProofUrls);

        var updatedProduct = productRepository.save(product);

        post.setTitle(request.getTitle());
        post.setDescription(request.getDescription());
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

    @Override
    public List<PostResponse> searchPostsByTitle(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            throw new IllegalArgumentException("Keyword must not be null or empty");
        }
        return postMapper.getAllPosts(postRepository.findByTitleContainingKeyword(keyword));
    }

    @Override
    public List<PostResponse> searchPostsByStatus(boolean status) {
        return postMapper.getAllPosts(postRepository.findPostsByIsAvailable(status));
    }

    @Override
    public List<PostResponse> getPostByUserId(String userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return postRepository.findPostsByUserId(user.getId()).stream()
                .map(postMapper::postToPostResponse)
                .toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('USER')")
    public void deletePost(String postId) {
        postRepository.deleteById(postId);
    }

    @Override
    public List<PostResponse> getPostByBrandLine(String brandLineName) {
        var posts = postRepository.findAllByBrandLine(brandLineName);
        return postMapper.getAllPosts(posts);
    }

    @Override
    public List<PostResponse> getPostsByFollowedUser(String followedUserId) {
        var user =
                userRepository.findById(followedUserId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return postRepository.findPostsByFollowedUsers(user.getId()).stream()
                .map(postMapper::postToPostResponse)
                .toList();
    }
}
