package com.ugts.post.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.ugts.post.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class PostSearchRepository {
//    List<Post> findByTitle(String title);

    public static final String POSTS = "posts";

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    public String createOrUpdateDocument(Post post) throws IOException {
        IndexResponse response= elasticsearchClient.index(i->i
                .index(POSTS)
                .id(post.getId())
                .document(post));

        Map<String,String> responseMessages = Map.of(
                "Created","Document has been created",
                "Updated", "Document has been updated"
        );

        return responseMessages.getOrDefault(response.result().name(),"Error has occurred");

    }

    public Post findDocById(String postId) throws IOException {
        return elasticsearchClient.get(g->g.index(POSTS).id(postId),Post.class).source();
    }

    public List<Post> findAll() throws IOException {
        SearchRequest request = SearchRequest.of(s->s.index(POSTS));
        SearchResponse<Post> response = elasticsearchClient.search(request,Post.class);

        List<Post> posts = new ArrayList<>();
        response.hits().hits().forEach(object->{
            posts.add(object.source());

        });
        return posts;

    }

    public String deleteDocById(String postId) throws IOException {
        DeleteRequest deleteRequest = DeleteRequest.of(d->d.index(POSTS).id(postId));
        DeleteResponse response =elasticsearchClient.delete(deleteRequest);

        return new StringBuffer(response.result().name().equalsIgnoreCase("NOT_FOUND")
                ?"Document not found with id"+postId:"Document has been deleted").toString();
    }

    public String bulkSave(List<Post> posts) throws IOException {
        BulkRequest.Builder br = new BulkRequest.Builder();
        posts.forEach(post->br.operations(operation->
                operation.index(i->i
                        .index(POSTS)
                        .id(post.getId())
                        .document(post))));

        BulkResponse response =elasticsearchClient.bulk(br.build());
        if(response.errors()){
            return "Bulk has errors";
        } else {
            return "Bulk save success";
        }
    }

    public SearchResponse<Post> search(SearchRequest request, Class<Post> postClass) throws IOException {
        try {
            if (request != null && postClass != null) {
                return elasticsearchClient.search(request, postClass);
            }
            throw new IllegalArgumentException("Invalid input: request and postClass must not be null");
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    public void update(UpdateRequest request, Class<Post> postClass) throws IOException {
        if (request != null) {
            try {
                elasticsearchClient.update(request, postClass);
            } catch (IOException e) {
                System.err.println("An error occurred during document update: " + e.getMessage());
            }
        } else {
            throw new IllegalArgumentException("Invalid UpdateRequest provided");
        }
    }

}
