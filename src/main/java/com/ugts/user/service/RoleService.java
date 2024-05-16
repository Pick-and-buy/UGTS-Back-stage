package com.ugts.user.service;

import java.util.List;

import com.ugts.user.dto.request.RoleRequest;
import com.ugts.user.dto.response.RoleResponse;

public interface RoleService {
    RoleResponse create(RoleRequest request);

    List<RoleResponse> getAllRoles();
}
