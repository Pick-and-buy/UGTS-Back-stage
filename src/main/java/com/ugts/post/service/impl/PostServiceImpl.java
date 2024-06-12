package com.ugts.post.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import co.elastic.clients.elasticsearch.core.search.Hit;
import com.ugts.brand.repository.BrandCollectionRepository;
import com.ugts.brand.repository.BrandLineRepository;
import com.ugts.brand.repository.BrandRepository;
import com.ugts.brand.repository.CategoryRepository;
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

    CategoryRepository categoryRepository;

    BrandLineRepository brandLineRepository;

    BrandCollectionRepository brandCollectionRepository;

    @Override
    @Transactional
    @PreAuthorize("hasRole('USER')")
    public PostResponse createPost(CreatePostRequest postRequest, MultipartFile[] files) throws IOException {
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
                .interiorColor(postRequest.getProduct().getInteriorColor())
                .exteriorColor(postRequest.getProduct().getExteriorColor())
                .size(postRequest.getProduct().getSize())
                .width(postRequest.getProduct().getWidth())
                .height(postRequest.getProduct().getHeight())
                .length(postRequest.getProduct().getLength())
                .drop(postRequest.getProduct().getDrop())
                .fit(postRequest.getProduct().getFit())
                .referenceCode(postRequest.getProduct().getReferenceCode())
                .manufactureYear(postRequest.getProduct().getManufactureYear())
                .material(postRequest.getProduct().getMaterial())
                .condition(postRequest.getProduct().getCondition())
                .accessories(postRequest.getProduct().getAccessories())
                .dateCode(postRequest.getProduct().getDateCode())
                .serialNumber(postRequest.getProduct().getSerialNumber())
                .purchasedPlace(postRequest.getProduct().getPurchasedPlace())
                .story(postRequest.getProduct().getStory())
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
        //adding new document
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
    public PostResponse updatePost(String postId, UpdatePostRequest request) {
        var post = postRepository.findById(postId).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        var product = productRepository
                .findById(request.getProduct().getId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        product.setName(request.getProduct().getName());
        product.setPrice(request.getProduct().getPrice());
        product.setInteriorColor(request.getProduct().getInteriorColor());
        product.setExteriorColor(request.getProduct().getExteriorColor());
        product.setSize(request.getProduct().getSize());
        product.setWidth(request.getProduct().getWidth());
        product.setHeight(request.getProduct().getHeight());
        product.setLength(request.getProduct().getLength());
        product.setDrop(request.getProduct().getDrop());
        product.setFit(request.getProduct().getFit());
        product.setReferenceCode(request.getProduct().getReferenceCode());
        product.setManufactureYear(request.getProduct().getManufactureYear());
        product.setMaterial(request.getProduct().getMaterial());
        product.setCondition(request.getProduct().getCondition());
        product.setAccessories(request.getProduct().getAccessories());
        product.setDateCode(request.getProduct().getDateCode());
        product.setSerialNumber(request.getProduct().getSerialNumber());
        product.setPurchasedPlace(request.getProduct().getPurchasedPlace());
        product.setStory(request.getProduct().getStory());

        var updatedProduct = productRepository.save(product);
        //TODO: update post in elastic document

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

    @Override
    public List<PostResponse> searchPostsByTitle(String keyword) throws IOException {
        SearchRequest request = SearchRequest.of(s -> s
                .index("posts")
                .query(q -> q
                        .match(t -> t
                                .field("title")
                                .query(keyword)
                        )
                )
        );

        return getPostResponses(request);
    }

    @Override
    public List<PostResponse> searchPostsByStatus(boolean status) throws IOException {
        SearchRequest request = SearchRequest.of(s -> s
                .index("posts")
                .query(q -> q
                        .bool(b -> b
                                .must(m -> m
                                        .term(t -> t
                                                .field("isAvailable")
                                                .value(status)
                                        )
                                )
                        )
                )
        );

        return getPostResponses(request);
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
