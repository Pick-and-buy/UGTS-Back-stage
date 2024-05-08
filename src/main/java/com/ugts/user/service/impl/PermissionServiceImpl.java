package com.ugts.user.service.impl;

import java.util.List;

import com.ugts.user.dto.request.PermissionRequest;
import com.ugts.user.dto.response.PermissionResponse;
import com.ugts.user.entity.Permission;
import com.ugts.user.mapper.PermissionMapper;
import com.ugts.user.repository.PermissionRepository;
import com.ugts.user.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionServiceImpl implements PermissionService {

    PermissionRepository permissionRepository;

    PermissionMapper permissionMapper;

    @Override
    public PermissionResponse create(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);

        return permissionMapper.toPermissionResponse(permission);
    }

    @Override
    public List<PermissionResponse> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(permissionMapper::toPermissionResponse)
                .toList();
    }

    @Override
    public void deletePermission(String permissionId) {
        permissionRepository.deleteById(permissionId);
    }
}
