package com.ugts.post.service.impl;

import java.io.IOException;
import java.util.*;

import co.elastic.clients.elasticsearch.core.search.Hit;
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
import com.ugts.post.repository.PostSearchRepository;
import com.ugts.post.service.IPostService;
import com.ugts.product.entity.Product;
import com.ugts.product.entity.ProductImage;
import com.ugts.product.repository.ProductRepository;
import com.ugts.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.UpdateRequest;
import co.elastic.clients.elasticsearch.core.UpdateResponse;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImpl implements IPostService {

    PostRepository postRepository;

    ProductRepository productRepository;

    PostSearchRepository postSearchRepository;

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
        //adding new elastic document
        postSearchRepository.createOrUpdateDocument(post);

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
    public List<PostResponse> getAllPosts() throws IOException {
        List<Post> posts = postSearchRepository.findAll();
        return postMapper.getAllPosts(posts);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('USER')")
    public PostResponse updatePost(String postId, UpdatePostRequest request) throws IOException {
        //TODO : cần thêm trường update về status của post và UpdatePostRequest
        Post post = postRepository.findById(postId).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        var product = productRepository
                .findById(request.getProduct().getId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        product.setName(request.getProduct().getName());
        product.setPrice(request.getProduct().getPrice());
        product.setColor(request.getProduct().getColor());
        product.setSize(request.getProduct().getSize());
        product.setWidth(request.getProduct().getWidth());
        product.setHeight(request.getProduct().getHeight());
        product.setLength(request.getProduct().getLength());
        product.setReferenceCode(request.getProduct().getReferenceCode());
        product.setManufactureYear(request.getProduct().getManufactureYear());
        product.setInteriorMaterial(request.getProduct().getInteriorMaterial());
        product.setExteriorMaterial(request.getProduct().getExteriorMaterial());
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

        //TODO: update post in elastic document
        Map<String, Object> fields = new HashMap<>();
        if (request.getTitle() != null) {
            fields.put("title", request.getTitle());
        }
        //TODO : update status of post bellow
//        if () {
//            fields.put("status", post.setIsAvailable(true));
//        }
        updatePost(post.getId(), fields);

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
    public List<PostResponse> searchPostsByTitle(String keyword) throws IOException {
        String indexName = "posts";
        if (keyword == null || keyword.isEmpty()) {
            throw new IllegalArgumentException("Keyword must not be null or empty");
        }
        SearchRequest request = SearchRequest.of(s -> s
                .index(indexName)
                .query(q -> q
                        .match(t -> t
                                .field("title")
                                .query(keyword)
                        )
                )
                // Implement pagination if need
//                .from(0)
//                .size(10)
        );

        return getPostResponses(request);
    }

    @Override
    public List<PostResponse> searchPostsByStatus(boolean status) throws IOException {
        String indexName = "posts";
        try {
            SearchRequest request = SearchRequest.of(s -> s
                    .index(indexName)
                    .query(q -> q
                            .bool(b -> b
                                    .must(m -> m
                                            .term(t -> t
                                                    .field("isAvailable")
                                                    .value(status)
                                                    .boost(1.0f)
                                            )
                                    )
                            )
                    )
            );
            return getPostResponses(request);
        } catch (IOException e) {
            System.err.println("An error occurred during searchPostsByStatus: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    public void updatePost(String id, Map<String, Object> fields) throws IOException {
        if (id == null || id.isEmpty() || fields == null || fields.isEmpty()) {
            throw new IllegalArgumentException("ID and fields must not be null or empty");
        }

        if (postSearchRepository.findDocById(id) != null) {
            UpdateRequest<Object, Map<String, Object>> request = UpdateRequest.of(u -> u
                    .index("posts")
                    .id(id)
                    .doc(fields)
            );

            try {
                postSearchRepository.update(request, Post.class);
            } catch (IOException e) {
                System.err.println("An error occurred during document update: " + e.getMessage());
            }
        } else {
            System.out.println("Document with id " + id + " does not exist.");
        }
    }

    private List<PostResponse> getPostResponses(SearchRequest request) throws IOException {
        SearchResponse<Post> response = postSearchRepository.search(request, Post.class);
        List<Post> posts = new ArrayList<>();
        for (Hit<Post> hit : response.hits().hits()) {
            posts.add(hit.source());
        }
        return postMapper.getAllPosts(posts);
    }
}
