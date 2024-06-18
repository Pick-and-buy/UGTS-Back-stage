//package com.ugts.post.service;
//
//import com.ugts.brand.entity.Brand;
//import com.ugts.brand.entity.BrandLine;
//import com.ugts.brand.entity.Category;
//import com.ugts.brand.repository.BrandCollectionRepository;
//import com.ugts.brand.repository.BrandLineRepository;
//import com.ugts.brand.repository.BrandRepository;
//import com.ugts.brand.repository.CategoryRepository;
//import com.ugts.cloudService.GoogleCloudStorageService;
//import com.ugts.post.dto.request.CreatePostRequest;
//import com.ugts.post.dto.request.UpdatePostRequest;
//import com.ugts.post.dto.response.PostResponse;
//import com.ugts.post.entity.Post;
//import com.ugts.post.mapper.PostMapper;
//import com.ugts.post.repository.PostRepository;
//import com.ugts.post.service.impl.PostServiceImpl;
//import com.ugts.product.entity.Product;
//import com.ugts.product.repository.ProductRepository;
//import com.ugts.user.repository.UserRepository;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//public class PostServiceTest {
//    @InjectMocks
//    private PostServiceImpl postService;
//
//    @Mock
//    private PostRepository postRepository;
//
//    @Mock
//    private ProductRepository productRepository;
//
//    @Mock
//    private BrandRepository brandRepository;
//
//    @Mock
//    private PostMapper postMapper;
//
//    @Mock
//    private GoogleCloudStorageService googleCloudStorageService;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private CategoryRepository categoryRepository;
//
//    @Mock
//    private BrandLineRepository brandLineRepository;
//
//    @Mock
//    private BrandCollectionRepository brandCollectionRepository;
//
//    @Test
//    public void testCreatePost() throws IOException, IOException {
//        // Set up mocks
//        CreatePostRequest request = new CreatePostRequest();
//        request.setBrand(new Brand("brandName"));
//        request.setBrandLine(new BrandLine("brandLineName"));
//        request.setCategory(new Category("categoryName"));
//        request.setProduct(new Product("productName", 10.0, "description"));
//        request.setTitle("postTitle");
//        request.setDescription("postDescription");
//
//        Brand brand = new Brand("brandName");
//        BrandLine brandLine = new BrandLine("brandLineName");
//        Category category = new Category("categoryName");
//        Product product = new Product("productName", 10.0, "description");
//
//        when(brandRepository.findByName("brandName")).thenReturn(brand);
//        when(brandLineRepository.findByLineName("brandLineName")).thenReturn(brandLine);
//        when(categoryRepository.findByCategoryName("categoryName")).thenReturn(category);
//        when(productRepository.save(any(Product.class))).thenReturn(product);
//
//        MultipartFile[] files = new MultipartFile[0];
//
//        // Call the method
//        PostResponse response = postService.createPost(request, files);
//
//        // Verify the results
//        assertNotNull(response);
//        assertEquals("postTitle", response.getTitle());
//        assertEquals("postDescription", response.getDescription());
//        verify(postRepository).save(any(Post.class));
//        verify(googleCloudStorageService).uploadProductImagesToGCS(files, any(Long.class));
//    }
//
//    @Test
//    public void testGetAllPosts() {
//        // Set up mocks
//        List<Post> posts = new ArrayList<>();
//        posts.add(new Post("post1", "description1"));
//        posts.add(new Post("post2", "description2"));
//
//        when(postRepository.findAll()).thenReturn(posts);
//
//        // Call the method
//        List<PostResponse> responses = postService.getAllPosts();
//
//        // Verify the results
//        assertNotNull(responses);
//        assertEquals(2, responses.size());
//        assertEquals("post1", responses.get(0).getTitle());
//        assertEquals("post2", responses.get(1).getTitle());
//    }
//
//    @Test
//    public void testUpdatePost() {
//        // Set up mocks
//        String postId = "postId";
//        UpdatePostRequest request = new UpdatePostRequest();
//        request.setTitle("newTitle");
//        request.setDescription("newDescription");
//
//        Post post = new Post("oldTitle", "oldDescription");
//        Product product = new Product("productName", 10.0, "description");
//
//        when(postRepository.findById(postId)).thenReturn(post);
//        when(productRepository.findById(request.getProduct().getId())).thenReturn(product);
//
//        // Call the method
//        PostResponse response = postService.updatePost(postId, request);
//
//        // Verify the results
//        assertNotNull(response);
//        assertEquals("newTitle", response.getTitle());
//        assertEquals("newDescription", response.getDescription());
//        verify(postRepository).save(any(Post.class));
//    }
//}
