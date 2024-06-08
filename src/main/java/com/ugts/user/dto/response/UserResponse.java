package com.ugts.user.dto.response;

import java.time.LocalDate;
import java.util.Set;

import com.ugts.post.dto.response.PostResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String username;
    String avatar;
    String lastName;
    String firstName;
    String email;
    String phoneNumber;
    LocalDate dob;
    Set<RoleResponse> roles;
    Set<PostResponse> createdPosts;
}
