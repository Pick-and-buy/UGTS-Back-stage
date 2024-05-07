package com.ugts.mapper;

import com.ugts.dto.request.RoleRequest;
import com.ugts.dto.response.RoleResponse;
import com.ugts.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
