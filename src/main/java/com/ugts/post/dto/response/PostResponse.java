package com.ugts.post.dto.response;

import com.ugts.product.entity.Product;
import com.ugts.user.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostResponse {
    private User user;
    private String title;
    private String description;
    Boolean isAvailable;
    private Product product;
}
