package com.ugts.post.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import com.ugts.brand.repository.BrandRepository;
import com.ugts.brandLine.entity.BrandLine;
import com.ugts.brandLine.repository.BrandLineRepository;
import com.ugts.category.entity.Category;
import com.ugts.category.repository.CategoryRepository;
import com.ugts.common.cloudService.GoogleCloudStorageService;
import com.ugts.common.exception.AppException;
import com.ugts.common.exception.ErrorCode;
import com.ugts.post.dto.request.CreatePostRequest;
import com.ugts.post.dto.request.UpdatePostRequest;
import com.ugts.post.dto.response.PostResponse;
import com.ugts.post.entity.Post;
import com.ugts.post.mapper.PostMapper;
import com.ugts.post.repository.PostRepository;
import com.ugts.post.service.IPostService;
import com.ugts.product.entity.Product;
import com.ugts.product.entity.ProductData;
import com.ugts.product.entity.ProductImage;
import com.ugts.product.entity.VerifiedLevel;
import com.ugts.product.repository.ProductDataRepository;
import com.ugts.product.repository.ProductRepository;
import com.ugts.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
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

    ProductDataRepository productDataRepository;

    /**
     * Creates a new post at level 1 with the provided post request and product images.
     * Validates the post request, creates a new product at level 1, saves the new post,
     * and returns the response of the new post.
     *
     * @param postRequest The request containing details of the post to be created.
     * @param productImages The images of the product associated with the post.
     * @return The response of the newly created post.
     * @throws IOException If an I/O error occurs during the process.
     */
    @Override
    @Transactional
    @PreAuthorize("hasRole('USER')")
    public PostResponse createPostLevel1(CreatePostRequest postRequest, MultipartFile[] productImages)
            throws IOException {
        checkPostInput(postRequest);
        Product product = createProduct(postRequest, VerifiedLevel.LEVEL_1);
        Post newPost = saveNewPost(postRequest, product, productImages);
        return postMapper.postToPostResponse(newPost);
    }

    /**
     * Validates the provided CreatePostRequest object.
     * Checks if the postRequest is null or if any of its brand, brandLine, category, or product fields are null.
     * Throws an AppException with ErrorCode.INVALID_INPUT if validation fails.
     */
    private void checkPostInput(CreatePostRequest postRequest) {
        // Validate the CreatePostRequest object
        if (postRequest == null
                || postRequest.getBrand() == null
                || postRequest.getBrandLine() == null
                || postRequest.getCategory() == null
                || postRequest.getProduct() == null) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
    }

    /**
     * Creates a new product based on the provided CreatePostRequest and VerifiedLevel.
     * Retrieves the brand, brand line, and category from their respective repositories based on the request data.
     * Constructs a new product object with the extracted information and the verified level.
     * Saves the product in the database and returns the created product.
     */
    @Transactional
    protected Product createProduct(CreatePostRequest postRequest, VerifiedLevel verifiedLevel) {
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
                .verifiedLevel(verifiedLevel)
                .build();
        productRepository.save(product);

        // TODO: auto fill with product data by name if some field is missing
        autoCompleteProductData(postRequest, product);
        return productRepository.save(product);
    }

    @Transactional
    @Modifying
    protected void autoCompleteProductData(CreatePostRequest postRequest, Product product) {
        // Gọi phương thức repository với các điều kiện tùy chọn
        Optional<ProductData> productDataOptional = productDataRepository.findSimilarProduct(
                postRequest.getProduct().getName(),
                postRequest.getBrand().getName(),
                postRequest.getBrandLine().getLineName(),
                postRequest.getCategory().getCategoryName()
                );
            BrandLine brandLine = brandLineRepository
                    .findByLineName(postRequest.getBrandLine().getLineName())
                    .orElseThrow(() -> new AppException(ErrorCode.BRAND_LINE_NOT_EXISTED));
            Category category = categoryRepository
                    .findByCategoryName(postRequest.getCategory().getCategoryName())
                    .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));

            // Nếu tìm thấy sản phẩm, cập nhật các trường còn thiếu trong post
            productDataOptional.ifPresent(productData -> {
                try {
                    product.setBrandLine(brandLine);
                    product.setCategory(category);
                    product.setStory(productData.getStory());
                    product.setExteriorMaterial(productData.getExteriorMaterial());
                    product.setInteriorMaterial(productData.getInteriorMaterial());
                    product.setColor(productData.getColor());
                    product.setSize(productData.getSize());
                    product.setWidth(productData.getWidth());
                    product.setHeight(productData.getHeight());
                    product.setLength(productData.getLength());
                    productRepository.save(product);
                } catch (Exception e) {
                    log.error("Auto complete product data failed: {}", e.getMessage());
                }
            });
    }

    /**
     * Saves a new post based on the provided post request, product, and product images.
     * Retrieves the user from the context holder using the phone number from the authentication.
     * Creates a new post with the user, title, description, product, and timestamps.
     * Handles boosting the post if requested and saves the new post into the database.
     * Uploads product images to Google Cloud Storage and saves the product in the database.
     *
     * @param postRequest The request containing details of the post to be created.
     * @param product The product associated with the post.
     * @param productImages The images of the product associated with the post.
     * @return The newly saved post.
     * @throws IOException If an I/O error occurs during the process.
     */
    @Transactional
    protected Post saveNewPost(CreatePostRequest postRequest, Product product, MultipartFile[] productImages)
            throws IOException {
        // get user from context holder
        var contextHolder = SecurityContextHolder.getContext();
        String phoneNumber = contextHolder.getAuthentication().getName();

        var user = userRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // create post process
        var post = Post.builder()
                .user(user)
                .title(product.getName())
                .description(postRequest.getDescription())
                .isAvailable(true)
                .createdAt(new Date())
                .updatedAt(new Date())
                .product(product)
                .isArchived(false)
                .lastPriceForSeller(postRequest.getLastPriceForSeller())
                .build();
        postRepository.save(post);

        if (postRequest.getBoosted()) {
            boostPost(post.getId(), 2);
        } else {
            post.setBoosted(false);
            post.setBoostEndTime(null);
        }
        // save new post into database
        var newPost = postRepository.save(post);

        // upload product images to GCS
        uploadProductImagesToGCS(productImages, product);

        // save product into database
        productRepository.save(product);
        return postRepository.save(newPost);
    }

    /**
     * Uploads the provided product images to Google Cloud Storage (GCS).
     * Retrieves the image URLs from the GoogleCloudStorageService by uploading each image in the array of productImages
     * associated with the given product ID. Then, for each image URL, creates a new ProductImage object
     * and adds it to the product's list of images after checking and initializing the list if necessary.
     *
     * @param productImages An array of MultipartFile objects representing the images to be uploaded.
     * @param product The Product object to which the uploaded images will be associated.
     * @throws IOException If an I/O error occurs during the upload process.
     */
    private void uploadProductImagesToGCS(MultipartFile[] productImages, Product product) throws IOException {
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
    }

    /**
     * Creates a new post at level 2 with the provided post request, product images, video, and original receipt proof.
     * Validates the post request, creates a new product at level 2, saves the new post,
     * uploads the product video and original receipt proof to Google Cloud Storage,
     * and returns the response of the new post.
     *
     * @param postRequest The request containing details of the post to be created.
     * @param productImages The images of the product associated with the post.
     * @param productVideo The video of the product associated with the post.
     * @param originalReceiptProof The original receipt proof of the product associated with the post.
     * @return The response of the newly created post.
     * @throws IOException If an I/O error occurs during the process.
     */
    @Override
    @Transactional
    @PreAuthorize("hasRole('USER')")
    public PostResponse createPostLevel2(
            CreatePostRequest postRequest,
            MultipartFile[] productImages,
            MultipartFile productVideo,
            MultipartFile originalReceiptProof)
            throws IOException {

        checkPostInput(postRequest);
        Product product = createProduct(postRequest, VerifiedLevel.LEVEL_2);
        Post newPost = saveNewPost(postRequest, product, productImages);

        // upload product video to GCS
        String videoUrl = googleCloudStorageService.uploadProductVideoToGCS(productVideo, product.getId());
        product.setProductVideo(videoUrl);

        // upload originalReceiptProofUrls to GCS
        String originalReceiptProofUrls =
                googleCloudStorageService.uploadOriginalReceiptProofToGCS(originalReceiptProof, product.getId());
        product.setOriginalReceiptProof(originalReceiptProofUrls);

        // save product into database
        productRepository.save(product);

        return postMapper.postToPostResponse(newPost);
    }

    /**
     * Retrieves all posts in descending order based on their boost status.
     * Fetches all posts from the database sorted by boosted status in descending order.
     * Maps the retrieved posts to PostResponse objects using the PostMapper.
     *
     * @return A list of PostResponse objects representing all posts sorted by boost status.
     */
    @Override
    public List<PostResponse> getAllPosts() {
        List<Post> posts = postRepository.findAllOrderByBoostedDesc();
        return postMapper.getAllPosts(posts);
    }

    /**
     * Updates an existing post with the provided details.
     * Retrieves the post by its ID from the post repository.
     * Retrieves the product by its ID from the product repository.
     * Uploads new product images, video, and original receipt proof to Google Cloud Storage.
     * Updates the post title, description, product, and timestamps.
     * Handles boosting the post if requested.
     *
     * @param postId The ID of the post to be updated.
     * @param request The updated details for the post.
     * @param productImages The new images for the product associated with the post.
     * @param productVideo The new video for the product associated with the post.
     * @param originalReceiptProof The new original receipt proof for the product.
     * @return The response of the updated post.
     * @throws IOException If an I/O error occurs during the process.
     */
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
        uploadProductImagesToGCS(productImages, product);

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

        if (request.getBoosted() && post.getBoostEndTime() == null) {
            boostPost(post.getId(), 2);
        } else {
            post.setBoosted(false);
            post.setBoostEndTime(null);
        }

        Post updatedPost = postRepository.save(post);
        return postMapper.postToPostResponse(updatedPost);
    }

    /**
     * Retrieves a post by its ID from the database.
     * Throws an {@link AppException} with {@link ErrorCode#POST_NOT_FOUND} if the post with the given ID is not found.
     *
     * @param postId The ID of the post to retrieve.
     * @return The response representing the post.
     */
    @Override
    public PostResponse getPostById(String postId) {
        var post = postRepository.findById(postId).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
        return postMapper.postToPostResponse(post);
    }

    /**
     * Retrieves all posts associated with a specific brand by the given brand name.
     *
     * @param brandName The name of the brand to filter the posts.
     * @return A list of PostResponse objects representing the posts related to the specified brand.
     */
    @Override
    public List<PostResponse> getPostsByBrand(String brandName) {
        var posts = postRepository.findAllByBrandName(brandName);
        return postMapper.getAllPosts(posts);
    }

    /**
     * Searches for posts by the provided keyword in their titles.
     *
     * @param keyword The keyword to search for in post titles.
     * @return A list of PostResponse objects representing the posts that match the keyword.
     * @throws IllegalArgumentException If the keyword is null or empty.
     */
    @Override
    public List<PostResponse> searchPostsByTitle(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            throw new IllegalArgumentException("Keyword must not be null or empty");
        }
        return postMapper.getAllPosts(postRepository.findByTitleContainingKeyword(keyword));
    }

    /**
     * Searches for posts based on the availability status.
     *
     * @param status The availability status of the posts to search for.
     * @return A list of PostResponse objects representing the posts with the specified availability status.
     */
    @Override
    public List<PostResponse> searchPostsByStatus(boolean status) {
        return postMapper.getAllPosts(postRepository.findPostsByIsAvailable(status));
    }

    /**
     * Retrieves all posts associated with a specific user by the given user ID.
     *
     * @param userId The ID of the user to filter the posts.
     * @return A list of PostResponse objects representing the posts related to the specified user.
     * @throws AppException If the user with the given ID is not found.
     */
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

    /**
     * Retrieves all posts associated with a specific brand line by the given brand line name.
     *
     * @param brandLineName The name of the brand line to filter the posts.
     * @return A list of PostResponse objects representing the posts related to the specified brand line.
     */
    @Override
    public List<PostResponse> getPostByBrandLine(String brandLineName) {
        var posts = postRepository.findAllByBrandLine(brandLineName);
        return postMapper.getAllPosts(posts);
    }

    /**
     * Retrieves all posts associated with a specific user by the given user ID.
     *
     * @param followedUserId The ID of the user to filter the posts.
     * @return A list of PostResponse objects representing the posts related to the specified user.
     * @throws AppException If the user with the given ID is not found.
     */
    @Override
    public List<PostResponse> getPostsByFollowedUser(String followedUserId) {
        var user =
                userRepository.findById(followedUserId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return postRepository.findPostsByFollowedUsers(user.getId()).stream()
                .map(postMapper::postToPostResponse)
                .toList();
    }

    /**
     * Boosts a post by setting it as boosted and updating the boost end time.
     *
     * @param postId The ID of the post to be boosted.
     * @param hours The number of hours to boost the post for.
     */
    @Override
    @Modifying
    @Transactional
    public void boostPost(String postId, int hours) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
        try {
            post.setBoosted(true);
            post.setBoostEndTime(LocalDateTime.now().plusHours(hours));
            postRepository.save(post);
        } catch (Exception e) {
            log.error("An error occurred while boost post: {}", e.getMessage());
        }
    }

    /**
     * Archives a post by setting its 'isArchived' flag to true.
     *
     * @param postId The ID of the post to be archived.
     */
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void archivePost(String postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
        post.setIsArchived(true);
        postRepository.save(post);
    }
}
