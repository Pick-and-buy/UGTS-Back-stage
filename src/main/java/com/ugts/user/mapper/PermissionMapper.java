package com.ugts.user.mapper;

import com.ugts.user.dto.request.PermissionRequest;
import com.ugts.user.dto.response.PermissionResponse;
import com.ugts.user.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
