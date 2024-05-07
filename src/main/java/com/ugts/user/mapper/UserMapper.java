package com.ugts.user.mapper;

import com.ugts.authentication.dto.request.RegisterRequest;
import com.ugts.user.dto.response.UserResponse;
import com.ugts.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User register(RegisterRequest request);

    UserResponse userToUserResponse(User user);
}
