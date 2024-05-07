package com.ugts.service;

import java.util.List;

import com.ugts.dto.request.PermissionRequest;
import com.ugts.dto.response.PermissionResponse;

public interface PermissionService {
    PermissionResponse create(PermissionRequest request);

    List<PermissionResponse> getAllPermissions();

    void deletePermission(String permissionId);
}
