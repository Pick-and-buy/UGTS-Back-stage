package com.ugts.post.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikedPostDto {
    String id;
    String title;
    Boolean isAvailable;
}
