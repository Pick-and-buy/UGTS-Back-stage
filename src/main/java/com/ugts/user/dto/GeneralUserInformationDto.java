package com.ugts.user.dto;

import com.ugts.post.dto.LikedPostDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GeneralUserInformationDto {
     String id;
     String username;
     String avatar;
    String phoneNumber;
    String firstName;
    String lastName;
    Set<LikedPostDto> likedPosts;
}
