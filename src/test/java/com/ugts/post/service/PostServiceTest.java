package com.ugts.post.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.*;

import com.ugts.brand.entity.Brand;
import com.ugts.brand.repository.BrandRepository;
import com.ugts.brandLine.entity.BrandLine;
import com.ugts.brandLine.repository.BrandLineRepository;
import com.ugts.category.entity.Category;
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
import com.ugts.post.service.impl.PostServiceImpl;
import com.ugts.product.entity.Condition;
import com.ugts.product.entity.Product;
import com.ugts.product.repository.ProductRepository;
import com.ugts.user.entity.User;
import com.ugts.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@TestPropertySource("/test.properties")
public class PostServiceTest {
    @Mock
    private PostRepository postRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    GoogleCloudStorageService googleCloudStorageService;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private PostMapper postMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BrandLineRepository brandLineRepository;

    @InjectMocks
    private PostServiceImpl postService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private PostServiceImpl postServiceImpl;

    private Post post;
    private Product productDTO;
    private User user;
    private UpdatePostRequest updatePostRequest;

    @BeforeEach
    void setUp() {

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("098765466");
    }

    @Test
    void createPost_success() throws IOException {
        CreatePostRequest request = new CreatePostRequest();
        request.setTitle("Gucci Handbags Spring 2021 collections");
        request.setDescription("Sản Phẩm Bán Chạy Nhất 2021");

        Brand brandDTO = new Brand();
        brandDTO.setName("Gucci");
        request.setBrand(brandDTO);

        BrandLine brandLineDTO = new BrandLine();
        brandLineDTO.setLineName("Spring 2021");
        request.setBrandLine(brandLineDTO);

        Category categoryDTO = new Category();
        categoryDTO.setCategoryName("Handbags");
        request.setCategory(categoryDTO);
        Product productDTO = new Product();
        productDTO.setName("GUCCI BLONDIE Small BAG");
        productDTO.setPrice(350.0);
        productDTO.setColor("Green");
        productDTO.setSize("Small");
        productDTO.setWidth("15");
        productDTO.setHeight("10");
        productDTO.setLength("25");
        productDTO.setReferenceCode("none");
        productDTO.setManufactureYear("2024");
        productDTO.setExteriorMaterial("Plush suede");
        productDTO.setInteriorMaterial("Caton");
        productDTO.setAccessories("None");
        productDTO.setDateCode("none");
        productDTO.setSerialNumber("None");
        productDTO.setPurchasedPlace("FPT HL");
        productDTO.setStory("None");
        productDTO.setCondition(Condition.GOOD);
        request.setProduct(productDTO);
        MultipartFile[] files = new MultipartFile[0];

        Brand brand = new Brand();
        BrandLine brandLine = new BrandLine();
        Category category = new Category();
        Product product = new Product();
        Post post = new Post();
        user = new User();
        PostResponse postResponse = new PostResponse();
        postResponse.setTitle("Gucci Handbags Spring 2021 collections");

        when(brandRepository.findByName(anyString())).thenReturn(Optional.of(brand));
        when(brandLineRepository.findByLineName(anyString())).thenReturn(Optional.of(brandLine));
        when(categoryRepository.findByCategoryName(anyString())).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.of(user));
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(postMapper.postToPostResponse(any(Post.class))).thenReturn(postResponse);
        PostResponse result = postService.createPost(request, files);
        assertNotNull(result);
        assertEquals(postResponse.getTitle(), result.getTitle());
    }

    @Test
    void createPost_InvalidRequest_fail() {
        CreatePostRequest invalidRequest = new CreatePostRequest();
        MultipartFile[] files = new MultipartFile[0];

        AppException exception = assertThrows(AppException.class, () -> {
            postService.createPost(invalidRequest, files);
        });

        assertEquals(ErrorCode.INVALID_INPUT, exception.getErrorCode());
    }

    @Test
    void createPost_WhenBrandNotExisted_fail() {
        CreatePostRequest request = new CreatePostRequest();
        request.setTitle("Gucci Handbags Spring 2021 collections");
        request.setDescription("Sản Phẩm Bán Chạy Nhất 2021");

        Brand brandDTO = new Brand();
        brandDTO.setName("abc");
        request.setBrand(brandDTO);

        BrandLine brandLineDTO = new BrandLine();
        brandLineDTO.setLineName("Spring 2021");
        request.setBrandLine(brandLineDTO);

        Category categoryDTO = new Category();
        categoryDTO.setCategoryName("Handbags");
        request.setCategory(categoryDTO);
        Product productDTO = new Product();
        productDTO.setName("GUCCI BLONDIE Small BAG");
        productDTO.setPrice(350.0);
        productDTO.setColor("Green");
        productDTO.setSize("Small");
        productDTO.setWidth("15");
        productDTO.setHeight("10");
        productDTO.setLength("25");
        productDTO.setReferenceCode("none");
        productDTO.setManufactureYear("2024");
        productDTO.setExteriorMaterial("Plush suede");
        productDTO.setInteriorMaterial("Caton");
        productDTO.setAccessories("None");
        productDTO.setDateCode("none");
        productDTO.setSerialNumber("None");
        productDTO.setPurchasedPlace("FPT HL");
        productDTO.setStory("None");
        productDTO.setCondition(Condition.GOOD);
        request.setProduct(productDTO);

        MultipartFile[] files = new MultipartFile[0];

        User user = new User();
        BrandLine brandLine = new BrandLine();
        Category category = new Category();
        Product product = new Product();
        Post post = new Post();
        PostResponse postResponse = new PostResponse();
        when(brandRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(brandLineRepository.findByLineName(anyString())).thenReturn(Optional.of(brandLine));
        when(categoryRepository.findByCategoryName(anyString())).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.of(user));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(postMapper.postToPostResponse(any(Post.class))).thenReturn(postResponse);

        AppException exception = assertThrows(AppException.class, () -> {
            postService.createPost(request, files);
        });

        assertEquals(ErrorCode.BRAND_NOT_EXISTED, exception.getErrorCode());
    }

    @Test
    void createPost_BrandLineNotExisted_fail() {
        CreatePostRequest request = new CreatePostRequest();
        request.setTitle("Gucci Handbags Spring 2021 collections");
        request.setDescription("Sản Phẩm Bán Chạy Nhất 2021");

        Brand brandDTO = new Brand();
        brandDTO.setName("Gucci");
        request.setBrand(brandDTO);

        BrandLine brandLineDTO = new BrandLine();
        brandLineDTO.setLineName("Winter");
        request.setBrandLine(brandLineDTO);

        Category categoryDTO = new Category();
        categoryDTO.setCategoryName("Handbags");
        request.setCategory(categoryDTO);
        Product productDTO = new Product();
        productDTO.setName("GUCCI BLONDIE Small BAG");
        productDTO.setPrice(350.0);
        productDTO.setColor("Green");
        productDTO.setSize("Small");
        productDTO.setWidth("15");
        productDTO.setHeight("10");
        productDTO.setLength("25");
        productDTO.setReferenceCode("none");
        productDTO.setManufactureYear("2024");
        productDTO.setExteriorMaterial("Plush suede");
        productDTO.setInteriorMaterial("Caton");
        productDTO.setAccessories("None");
        productDTO.setDateCode("none");
        productDTO.setSerialNumber("None");
        productDTO.setPurchasedPlace("FPT HL");
        productDTO.setStory("None");
        productDTO.setCondition(Condition.GOOD);
        request.setProduct(productDTO);

        MultipartFile[] files = new MultipartFile[0];
        Brand brand = new Brand();
        Category category = new Category();
        Product product = new Product();
        Post post = new Post();
        user = new User();
        PostResponse postResponse = new PostResponse();
        when(brandRepository.findByName(anyString())).thenReturn(Optional.of(brand));
        when(brandLineRepository.findByLineName(anyString())).thenReturn(Optional.empty());
        when(categoryRepository.findByCategoryName(anyString())).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.of(user));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(postMapper.postToPostResponse(any(Post.class))).thenReturn(postResponse);
        AppException exception = assertThrows(AppException.class, () -> {
            postService.createPost(request, files);
        });

        assertEquals(ErrorCode.BRAND_LINE_NOT_EXISTED, exception.getErrorCode());
    }

    @Test
    void createPost_WhenCategoryNotExisted_fail() {
        CreatePostRequest request = new CreatePostRequest();
        request.setTitle("Gucci Handbags Spring 2021 collections");
        request.setDescription("Sản Phẩm Bán Chạy Nhất 2021");

        Brand brandDTO = new Brand();
        brandDTO.setName("Gucci");
        request.setBrand(brandDTO);

        BrandLine brandLineDTO = new BrandLine();
        brandLineDTO.setLineName("Winter");
        request.setBrandLine(brandLineDTO);

        Category categoryDTO = new Category();
        categoryDTO.setCategoryName("Handbags");
        request.setCategory(categoryDTO);

        Product productDTO = new Product();
        productDTO.setName("GUCCI BLONDIE Small BAG");
        productDTO.setPrice(350.0);
        productDTO.setColor("Green");
        productDTO.setSize("Small");
        productDTO.setWidth("15");
        productDTO.setHeight("10");
        productDTO.setLength("25");
        productDTO.setReferenceCode("none");
        productDTO.setManufactureYear("2024");
        productDTO.setExteriorMaterial("Plush suede");
        productDTO.setInteriorMaterial("Caton");
        productDTO.setAccessories("None");
        productDTO.setDateCode("none");
        productDTO.setSerialNumber("None");
        productDTO.setPurchasedPlace("FPT HL");
        productDTO.setStory("None");
        productDTO.setCondition(Condition.GOOD);
        request.setProduct(productDTO);

        MultipartFile[] files = new MultipartFile[0];
        Brand brand = new Brand();
        BrandLine brandLine = new BrandLine();
        Product product = new Product();
        Post post = new Post();
        user = new User();
        PostResponse postResponse = new PostResponse();
        when(brandRepository.findByName(anyString())).thenReturn(Optional.of(brand));
        when(brandLineRepository.findByLineName(anyString())).thenReturn(Optional.of(brandLine));
        when(categoryRepository.findByCategoryName(anyString())).thenReturn(Optional.empty());
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.of(user));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(postMapper.postToPostResponse(any(Post.class))).thenReturn(postResponse);
        AppException exception = assertThrows(AppException.class, () -> {
            postService.createPost(request, files);
        });

        assertEquals(ErrorCode.CATEGORY_NOT_EXISTED, exception.getErrorCode());
    }

//    @Test
//    void UpdatePost_success() throws IOException {
//        UpdatePostRequest request = new UpdatePostRequest();
//        request.setTitle("Gucci Handbags Spring 2024 collections");
//        request.setDescription("Updated Description");
//
//        Product productDTO = new Product();
//        productDTO.setId("fa4328d6-b76c-41ae-99ac-2ffc5688bbee");
//        productDTO.setName("Updated Product Name");
//        request.setProduct(productDTO);
//
//        Post existingPost = new Post();
//        existingPost.setId("f925dfd3-d157-40f0-a755-d9f77a0ca1f6");
//
//        Product existingProduct = new Product();
//        existingProduct.setId("fa4328d6-b76c-41ae-99ac-2ffc5688bbee");
//
//        when(postRepository.findById(anyString())).thenReturn(Optional.of(existingPost));
//        when(productRepository.findById(anyString())).thenReturn(Optional.of(existingProduct));
//        when(postRepository.save(any(Post.class))).thenReturn(existingPost);
//        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);
//        when(postMapper.postToPostResponse(any(Post.class))).thenReturn(new PostResponse());
//
//        PostResponse response = postService.updatePost("f925dfd3-d157-40f0-a755-d9f77a0ca1f6", request);
//
//        assertNotNull(response);
//        verify(postRepository, times(1)).save(any(Post.class));
//        verify(productRepository, times(1)).save(any(Product.class));
//        verify(postMapper, times(1)).postToPostResponse(any(Post.class));
//    }

//    @Test
//    void updatePost_PostNotFound_fail() {
//        UpdatePostRequest request = new UpdatePostRequest();
//        request.setProduct(new Product());
//
//        when(postRepository.findById(anyString())).thenReturn(Optional.empty());
//
//        AppException exception = assertThrows(AppException.class, () -> {
//            postService.updatePost("f925dfd3-d157-40f0-a755-d9f77a0ca1f", request);
//        });
//
//        assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
//    }
//
//    @Test
//    void updatePost_ProductNotFound_ThrowsException() {
//        UpdatePostRequest request = new UpdatePostRequest();
//        Product productDTO = new Product();
//        productDTO.setId("a4328d6-b76c-41ae-99ac-2ffc5688bbe");
//        request.setProduct(productDTO);
//
//        Post existingPost = new Post();
//        existingPost.setId("f925dfd3-d157-40f0-a755-d9f77a0ca1f6");
//
//        when(postRepository.findById(anyString())).thenReturn(Optional.of(existingPost));
//        when(productRepository.findById(anyString())).thenReturn(Optional.empty());
//
//        AppException exception = assertThrows(AppException.class, () -> {
//            postService.updatePost("f925dfd3-d157-40f0-a755-d9f77a0ca1f6", request);
//        });
//
//        assertEquals(ErrorCode.PRODUCT_NOT_EXISTED, exception.getErrorCode());
//    }

    @Test
    void getPostById_Success() {
        String postId = "f925dfd3-d157-40f0-a755-d9f77a0ca1f6";
        Post post = new Post();
        post.setId(postId);
        PostResponse postResponse = new PostResponse();
        postResponse.setId(postId);
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postMapper.postToPostResponse(post)).thenReturn(postResponse);

        PostResponse response = postService.getPostById(postId);

        assertNotNull(response);
        assertEquals(postId, response.getId());
        verify(postRepository, times(1)).findById(postId);
        verify(postMapper, times(1)).postToPostResponse(post);
    }

    @Test
    void getPostById_PostNotFound_fail() {
        String postId = "f925dfd3-d157-40f0-a755-d9f77a0ca1f";

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            postService.getPostById(postId);
        });

        assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
        verify(postRepository, times(1)).findById(postId);
        verify(postMapper, times(0)).postToPostResponse(any(Post.class));
    }

    @Test
    void getPostsByBrand_Success() {
        String brandName = "Gucci";
        Post post1 = new Post();
        Post post2 = new Post();
        List<Post> posts = Arrays.asList(post1, post2);

        PostResponse postResponse1 = new PostResponse();
        PostResponse postResponse2 = new PostResponse();
        List<PostResponse> postResponses = Arrays.asList(postResponse1, postResponse2);

        when(postRepository.findAllByBrandName(brandName)).thenReturn(posts);
        when(postMapper.getAllPosts(posts)).thenReturn(postResponses);

        List<PostResponse> responses = postService.getPostsByBrand(brandName);

        assertNotNull(responses);
        assertEquals(2, responses.size());
        verify(postRepository, times(1)).findAllByBrandName(brandName);
        verify(postMapper, times(1)).getAllPosts(posts);
    }

    @Test
    void getPostsByBrand_NoPostsFound_fail() {
        String brandName = "Chanel";
        List<Post> emptyPosts = Collections.emptyList();

        when(postRepository.findAllByBrandName(brandName)).thenReturn(emptyPosts);
        when(postMapper.getAllPosts(emptyPosts)).thenReturn(Collections.emptyList());

        List<PostResponse> responses = postService.getPostsByBrand(brandName);

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
        verify(postRepository, times(1)).findAllByBrandName(brandName);
        verify(postMapper, times(1)).getAllPosts(emptyPosts);
    }

    @Test
    void getPostByUserId_Success() {
        String userId = "210b7e8f-d75e-4962-972d-dcd6ac713b03";
        User user = new User();
        user.setId(userId);

        Post post1 = new Post();
        Post post2 = new Post();
        List<Post> posts = Arrays.asList(post1, post2);

        PostResponse postResponse1 = new PostResponse();
        PostResponse postResponse2 = new PostResponse();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(postRepository.findPostsByUserId(userId)).thenReturn(posts);
        when(postMapper.postToPostResponse(post1)).thenReturn(postResponse1);
        when(postMapper.postToPostResponse(post2)).thenReturn(postResponse2);

        List<PostResponse> responses = postService.getPostByUserId(userId);

        assertNotNull(responses);
        assertEquals(2, responses.size());
        verify(userRepository, times(1)).findById(userId);
        verify(postRepository, times(1)).findPostsByUserId(userId);
        verify(postMapper, times(1)).postToPostResponse(post1);
        verify(postMapper, times(1)).postToPostResponse(post2);
    }

    @Test
    void getPostByUserId_NoPostsFound_fail() {
        String userId = "210b7e8f-d75e-4962-972d-dcd6ac713b03";
        User user = new User();
        user.setId(userId);

        List<Post> emptyPosts = Collections.emptyList();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(postRepository.findPostsByUserId(userId)).thenReturn(emptyPosts);

        List<PostResponse> responses = postService.getPostByUserId(userId);

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
        verify(userRepository, times(1)).findById(userId);
        verify(postRepository, times(1)).findPostsByUserId(userId);
        verify(postMapper, never()).postToPostResponse(any(Post.class));
    }

    @Test
    void getPostByUserId_UserNotFound() {
        String userId = "210b7e8f-d75e-4962-972d-dcd6ac713b0";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            postService.getPostByUserId(userId);
        });

        assertEquals(ErrorCode.USER_NOT_EXISTED, exception.getErrorCode());
        verify(userRepository, times(1)).findById(userId);
        verify(postRepository, never()).findPostsByUserId(anyString());
        verify(postMapper, never()).postToPostResponse(any(Post.class));
    }

    @Test
    void searchPostsByTitle_Success() throws IOException {
        String keyword = "Gucci";
        Post post1 = new Post();
        Post post2 = new Post();
        List<Post> posts = Arrays.asList(post1, post2);

        PostResponse postResponse1 = new PostResponse();
        PostResponse postResponse2 = new PostResponse();
        List<PostResponse> postResponses = Arrays.asList(postResponse1, postResponse2);

        when(postRepository.findByTitleContainingKeyword(keyword)).thenReturn(posts);
        when(postMapper.getAllPosts(posts)).thenReturn(postResponses);

        List<PostResponse> responses = postService.searchPostsByTitle(keyword);

        assertNotNull(responses);
        assertEquals(2, responses.size());
        verify(postRepository, times(1)).findByTitleContainingKeyword(keyword);
        verify(postMapper, times(1)).getAllPosts(posts);
    }

    @Test
    void searchPostsByTitle_EmptyKeyword_fail() {
        IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class, () -> {
            postService.searchPostsByTitle("");
        });

        IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class, () -> {
            postService.searchPostsByTitle(null);
        });

        assertEquals("Keyword must not be null or empty", exception1.getMessage());
        assertEquals("Keyword must not be null or empty", exception2.getMessage());

        verify(postRepository, never()).findByTitleContainingKeyword(anyString());
        verify(postMapper, never()).getAllPosts(anyList());
    }

    @Test
    void searchPostsByTitle_NoPostsFound_fail() throws IOException {
        String keyword = "abc";
        List<Post> emptyPosts = Collections.emptyList();
        List<PostResponse> emptyPostResponses = Collections.emptyList();

        when(postRepository.findByTitleContainingKeyword(keyword)).thenReturn(emptyPosts);
        when(postMapper.getAllPosts(emptyPosts)).thenReturn(emptyPostResponses);

        List<PostResponse> responses = postService.searchPostsByTitle(keyword);

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
        verify(postRepository, times(1)).findByTitleContainingKeyword(keyword);
        verify(postMapper, times(1)).getAllPosts(emptyPosts);
    }

    @Test
    void searchPostsByStatus_True_Success() throws IOException {
        boolean status = true;
        Post post1 = new Post();
        Post post2 = new Post();
        List<Post> posts = Arrays.asList(post1, post2);

        PostResponse postResponse1 = new PostResponse();
        PostResponse postResponse2 = new PostResponse();
        List<PostResponse> postResponses = Arrays.asList(postResponse1, postResponse2);

        when(postRepository.findPostsByIsAvailable(status)).thenReturn(posts);
        when(postMapper.getAllPosts(posts)).thenReturn(postResponses);

        List<PostResponse> responses = postService.searchPostsByStatus(status);

        assertNotNull(responses);
        assertEquals(2, responses.size());
        verify(postRepository, times(1)).findPostsByIsAvailable(status);
        verify(postMapper, times(1)).getAllPosts(posts);
    }

    @Test
    void searchPostsByStatus_Fail_Success() throws IOException {
        boolean status = false;
        Post post1 = new Post();
        Post post2 = new Post();
        List<Post> posts = Arrays.asList(post1, post2);

        PostResponse postResponse1 = new PostResponse();
        PostResponse postResponse2 = new PostResponse();
        List<PostResponse> postResponses = Arrays.asList(postResponse1, postResponse2);

        when(postRepository.findPostsByIsAvailable(status)).thenReturn(posts);
        when(postMapper.getAllPosts(posts)).thenReturn(postResponses);

        List<PostResponse> responses = postService.searchPostsByStatus(status);

        assertNotNull(responses);
        assertEquals(2, responses.size());
        verify(postRepository, times(1)).findPostsByIsAvailable(status);
        verify(postMapper, times(1)).getAllPosts(posts);
    }

    @Test
    void searchPostsByStatus_fail() throws IOException {
        boolean status = true;
        List<Post> emptyPosts = Collections.emptyList();
        List<PostResponse> emptyPostResponses = Collections.emptyList();

        when(postRepository.findPostsByIsAvailable(status)).thenReturn(emptyPosts);
        when(postMapper.getAllPosts(emptyPosts)).thenReturn(emptyPostResponses);

        List<PostResponse> responses = postService.searchPostsByStatus(status);

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
        verify(postRepository, times(1)).findPostsByIsAvailable(status);
        verify(postMapper, times(1)).getAllPosts(emptyPosts);
    }

    @Test
    void searchPostsByStatus_NoUnavailablePosts() throws IOException {
        boolean status = false;
        List<Post> emptyPosts = Collections.emptyList();
        List<PostResponse> emptyPostResponses = Collections.emptyList();

        when(postRepository.findPostsByIsAvailable(status)).thenReturn(emptyPosts);
        when(postMapper.getAllPosts(emptyPosts)).thenReturn(emptyPostResponses);

        List<PostResponse> responses = postService.searchPostsByStatus(status);

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
        verify(postRepository, times(1)).findPostsByIsAvailable(status);
        verify(postMapper, times(1)).getAllPosts(emptyPosts);
    }

    @Test
    void deletePost_Success() {
        String postId = "f925dfd3-d157-40f0-a755-d9f77a0ca1f";
        Post post = new Post();

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        doNothing().when(postRepository).deleteById(postId);

        postService.deletePost(postId);

        verify(postRepository, times(1)).deleteById(postId);
    }
    //    @Test
    //    void deletePost_PostNotFound() {
    //        String postId = "f925dfd3-d157-40f0-a755-d9f77a0ca1";
    //
    //        when(postRepository.findById(postId)).thenReturn(Optional.empty());
    //
    //        AppException exception = assertThrows(AppException.class, () -> postService.deletePost(postId));
    //
    //        assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    //        verify(postRepository, never()).deleteById(anyString());
    //    }
}
