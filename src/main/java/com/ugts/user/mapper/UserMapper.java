package com.ugts.user.mapper;

import com.ugts.authentication.dto.request.RegisterRequest;
import com.ugts.user.dto.response.UserResponse;
import com.ugts.user.entity.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {
    public UserResponse userToUserResponse(User user);

    public User register(RegisterRequest request);
}
