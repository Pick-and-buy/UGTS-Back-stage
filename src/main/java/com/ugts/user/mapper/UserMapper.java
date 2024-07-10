package com.ugts.user.mapper;

import java.util.Set;

import com.ugts.authentication.dto.request.RegisterRequest;
import com.ugts.user.dto.response.UserResponse;
import com.ugts.user.entity.Address;
import com.ugts.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User register(RegisterRequest request);

    @Mapping(target = "address", source = "address", qualifiedByName = "setToAddress")
    UserResponse userToUserResponse(User user);

    @Named("setToAddress")
    default Address setToAddress(Set<Address> addresses) {
        if (addresses == null || addresses.isEmpty()) {
            return null;
        }
        return addresses.iterator().next();
    }
}
