package com.ugts.post.controller;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ugts.dto.ApiResponse;
import com.ugts.post.dto.request.CreatePostRequest;
import com.ugts.post.dto.request.UpdatePostRequest;
import com.ugts.post.dto.response.PostResponse;
import com.ugts.post.service.IPostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {

    IPostService postService;

    ObjectMapper objectMapper;

    /**
     * Creates a new post at level 1 with the provided request JSON and product images.
     * Converts the JSON string into a CreatePostRequest object using the 'objectMapper'.
     * Delegates the creation of the post to 'postService.createPostLevel1' with the request and product images.
     * Returns an ApiResponse with a success message and the created PostResponse.
     *
     * @param requestJson The JSON string representing the request for the new post.
     * @param productImages An array of MultipartFile objects representing the images associated with the post.
     * @return An ApiResponse containing a success message and the created PostResponse.
     * @throws IOException If an error occurs during JSON processing.
     */
    @PostMapping(value = "/level-1", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<PostResponse> createPostLevel1(
            @RequestPart("request") String requestJson, @RequestPart("productImages") MultipartFile[] productImages)
            throws IOException {

        // Chuyển đổi JSON string thành đối tượng CreatePostRequest
        CreatePostRequest request = objectMapper.readValue(requestJson, CreatePostRequest.class);

        var result = postService.createPostLevel1(request, productImages);
        return ApiResponse.<PostResponse>builder()
                .message("Create Success")
                .result(result)
                .build();
    }

    /**
     * Creates a new post at level 2 with the provided request JSON, product images, video, and receipt proof.
     * Converts the JSON string into a CreatePostRequest object using the 'objectMapper'.
     * Delegates the creation of the post to 'postService.createPostLevel2' with the request, images, video, and receipt proof.
     *
     * @param requestJson The JSON string representing the request for the new post.
     * @param productImages An array of MultipartFile objects representing the images associated with the post.
     * @param productVideo The video file associated with the post (optional).
     * @param originalReceiptProof The receipt proof file associated with the post (optional).
     * @return An ApiResponse containing a success message and the created PostResponse.
     * @throws IOException If an error occurs during JSON processing.
     */
    @PostMapping(value = "/level-2", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<PostResponse> createPostLevel2(
            @RequestPart("request") String requestJson,
            @RequestPart("productImages") MultipartFile[] productImages,
            @RequestPart(value = "productVideo", required = false) MultipartFile productVideo,
            @RequestPart(value = "originalReceiptProof", required = false) MultipartFile originalReceiptProof)
            throws IOException {

        // Chuyển đổi JSON string thành đối tượng CreatePostRequest
        CreatePostRequest request = objectMapper.readValue(requestJson, CreatePostRequest.class);

        var result = postService.createPostLevel2(request, productImages, productVideo, originalReceiptProof);
        return ApiResponse.<PostResponse>builder()
                .message("Create Success")
                .result(result)
                .build();
    }

    /**
     * Retrieves all posts from the database.
     * Calls the 'getAllPosts' method of 'postService' to fetch all posts.
     * Returns an ApiResponse with a success message and a list of PostResponse objects.
     *
     * @return An ApiResponse containing a success message and a list of PostResponse objects.
     */
    @GetMapping
    public ApiResponse<List<PostResponse>> getAllPosts() {
        var result = postService.getAllPosts();
        return ApiResponse.<List<PostResponse>>builder()
                .message("Get All Post Success")
                .result(result)
                .build();
    }

    /**
     * Updates an existing post identified by the provided postId.
     * Parses the 'updateRequest' JSON string into an UpdatePostRequest object.
     * Calls 'postService.updatePost' with the postId, request, images, video, and receipt proof.
     *
     * @param postId The unique identifier of the post to be updated.
     * @param updateRequest The JSON string representing the updated post details.
     * @param productImages An array of images associated with the post (optional).
     * @param productVideo The video file associated with the post (optional).
     * @param originalReceiptProof The receipt proof file associated with the post (optional).
     * @return An ApiResponse indicating the success of the update operation and the updated PostResponse.
     * @throws IOException If an error occurs during JSON processing.
     */
    @PutMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<PostResponse> updatePost(
            @PathVariable String postId,
            @RequestPart(value = "request", required = false) String updateRequest,
            @RequestPart(value = "productImages", required = false) MultipartFile[] productImages,
            @RequestPart(value = "productVideo", required = false) MultipartFile productVideo,
            @RequestPart(value = "originalReceiptProof", required = false) MultipartFile originalReceiptProof)
            throws IOException {
        UpdatePostRequest request = objectMapper.readValue(updateRequest, UpdatePostRequest.class);

        var result = postService.updatePost(postId, request, productImages, productVideo, originalReceiptProof);
        return ApiResponse.<PostResponse>builder()
                .message("Update Success")
                .result(result)
                .build();
    }

    /**
     * Retrieves a post by its unique identifier.
     *
     * @param postId The unique identifier of the post to retrieve.
     * @return An ApiResponse indicating the success of the operation and the retrieved PostResponse.
     */
    @GetMapping(value = "/{postId}")
    public ApiResponse<PostResponse> getPostById(@PathVariable String postId) {
        var result = postService.getPostById(postId);
        return ApiResponse.<PostResponse>builder()
                .message("Get Post By Id Success")
                .result(result)
                .build();
    }

    /**
     * Retrieves a list of posts filtered by the specified brand name.
     *
     * @param name The name of the brand to filter the posts by.
     * @return An ApiResponse indicating the success of the operation and a list of PostResponse objects for the specified brand.
     */
    @GetMapping("/brands")
    public ApiResponse<List<PostResponse>> getPostByBrandName(@RequestParam String name) {
        var result = postService.getPostsByBrand(name);
        return ApiResponse.<List<PostResponse>>builder()
                .message("Get Post By Brand Name Success")
                .result(result)
                .build();
    }

    /**
     * Retrieves a list of posts associated with a specific user identified by their ID.
     *
     * @param id The unique identifier of the user to retrieve posts for.
     * @return An ApiResponse indicating the success of the operation and a list of PostResponse objects for the specified user.
     */
    @GetMapping("/user")
    public ApiResponse<List<PostResponse>> getPostsByUserId(@RequestParam String id) {
        var result = postService.getPostByUserId(id);
        return ApiResponse.<List<PostResponse>>builder()
                .message("Success")
                .result(result)
                .build();
    }

    /**
     * Searches for posts by title using the provided keyword.
     *
     * @param keyword The keyword to search for in post titles.
     * @return An ApiResponse containing a list of PostResponse objects matching the search criteria.
     * @throws IOException If an error occurs during the search process.
     */
    @GetMapping("/search/{keyword}")
    public ApiResponse<List<PostResponse>> searchPostsByTitle(@PathVariable String keyword) throws IOException {
        return ApiResponse.<List<PostResponse>>builder()
                .result(postService.searchPostsByTitle(keyword))
                .build();
    }

    /**
     * Retrieves a list of posts based on the provided status.
     *
     * @param status The status to filter the posts by.
     * @return An ApiResponse containing a list of PostResponse objects matching the specified status.
     * @throws IOException If an error occurs during the retrieval process.
     */
    @GetMapping("/status/{status}")
    public ApiResponse<List<PostResponse>> searchByStatus(@PathVariable boolean status) throws IOException {
        return ApiResponse.<List<PostResponse>>builder()
                .result(postService.searchPostsByStatus(status))
                .build();
    }

    @DeleteMapping
    public ApiResponse<Void> deletePost(@RequestParam String postId) {
        postService.deletePost(postId);
        return ApiResponse.<Void>builder().message("Delete success").build();
    }

    /**
     * Retrieves a list of posts associated with a specific brand line identified by its name.
     *
     * @param brandLineName The name of the brand line to retrieve posts for.
     * @return An ApiResponse indicating the success of the operation and a list of PostResponse objects for the specified brand line.
     */
    @GetMapping("/brandLine")
    public ApiResponse<List<PostResponse>> getPostsByBrandLine(@RequestParam String brandLineName) {
        var result = postService.getPostByBrandLine(brandLineName);
        return ApiResponse.<List<PostResponse>>builder()
                .message("Success")
                .result(result)
                .build();
    }

    /**
     * Retrieves a list of posts associated with a specific user who is being followed, identified by their ID.
     *
     * @param followedUserId The unique identifier of the user who is being followed.
     * @return An ApiResponse indicating the success of the operation and a list of PostResponse objects for the followed user.
     */
    @GetMapping("/followedUser")
    public ApiResponse<List<PostResponse>> getPostByFollowedUser(@RequestParam String followedUserId) {
        var result = postService.getPostsByFollowedUser(followedUserId);
        return ApiResponse.<List<PostResponse>>builder()
                .message("Success")
                .result(result)
                .build();
    }

    /**
     * Archives a post identified by the provided postId.
     *
     * @param postId The unique identifier of the post to be archived.
     * @return An ApiResponse indicating the success of archiving the post.
     */
    @PutMapping("/archive-post/{postId}")
    public ApiResponse<Void> archivePost(@PathVariable String postId) {
        postService.archivePost(postId);
        return ApiResponse.<Void>builder().message("Archive post success").build();
    }

}
