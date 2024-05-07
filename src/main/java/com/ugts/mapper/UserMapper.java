package com.ugts.mapper;

import com.ugts.dto.request.RegisterRequest;
import com.ugts.dto.response.UserResponse;
import com.ugts.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User register(RegisterRequest request);

    UserResponse userToUserResponse(User user);
}
