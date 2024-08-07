package com.ugts.user.dto.response;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ugts.post.dto.LikedPostDto;
import com.ugts.post.dto.response.PostResponse;
import com.ugts.wallet.dto.WalletResponse;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date dob;

    Set<AddressResponse> address;

    Set<RoleResponse> roles;

    Set<LikedPostDto> likedPosts;

    @JsonIgnoreProperties(value = {"comments", "user"})
    Set<PostResponse> createdPosts;

    Boolean isVerified;

    @JsonIgnoreProperties(value = {"user", "transactions"})
    WalletResponse wallet;
}
