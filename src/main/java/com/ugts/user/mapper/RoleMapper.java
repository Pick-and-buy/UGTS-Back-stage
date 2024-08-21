package com.ugts.user.mapper;

import com.ugts.user.dto.request.RoleRequest;
import com.ugts.user.dto.response.RoleResponse;
import com.ugts.user.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
