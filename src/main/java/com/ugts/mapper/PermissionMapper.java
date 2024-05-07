package com.ugts.mapper;

import com.ugts.dto.request.PermissionRequest;
import com.ugts.dto.response.PermissionResponse;
import com.ugts.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
