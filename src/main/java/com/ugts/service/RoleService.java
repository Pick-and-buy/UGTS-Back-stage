package com.ugts.service;

import java.util.List;

import com.ugts.dto.request.RoleRequest;
import com.ugts.dto.response.RoleResponse;

public interface RoleService {
    RoleResponse create(RoleRequest request);

    List<RoleResponse> getAllRoles();
}
