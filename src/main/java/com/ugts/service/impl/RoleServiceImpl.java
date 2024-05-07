package com.ugts.service.impl;

import java.util.HashSet;
import java.util.List;

import com.ugts.dto.request.RoleRequest;
import com.ugts.dto.response.RoleResponse;
import com.ugts.mapper.RoleMapper;
import com.ugts.repository.PermissionRepository;
import com.ugts.repository.RoleRepository;
import com.ugts.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements RoleService {

    RoleRepository roleRepository;

    PermissionRepository permissionRepository;

    RoleMapper roleMapper;

    @Override
    public RoleResponse create(RoleRequest request) {
        var role = roleMapper.toRole(request);

        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }
}
