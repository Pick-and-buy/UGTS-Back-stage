package com.ugts.post.dto.response;

import java.util.Date;

import com.ugts.product.entity.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostResponse {
    private String id;
    private String title;
    private String description;
    private Boolean isAvailable;
    private Date createdAt;
    private Date updatedAt;
    private Product product;
}
