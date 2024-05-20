package com.ugts.post.dto.request;

import com.ugts.brand.entity.Brand;
import com.ugts.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostRequest {
    private User user;
    private String title;
    private String description;
    Boolean isAvailable;

    Brand brand;
    String productName;
    double productPrice;
    String color;
    String size;
    String condition;
    String material;
    Boolean isVerify;

    private MultipartFile image;
}
