package com.ugts.user.service;

import java.util.List;

import com.ugts.user.dto.request.PermissionRequest;
import com.ugts.user.dto.response.PermissionResponse;

public interface PermissionService {
    PermissionResponse create(PermissionRequest request);

    List<PermissionResponse> getAllPermissions();

    void deletePermission(String permissionId);
}
