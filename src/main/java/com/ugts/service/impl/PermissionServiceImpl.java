package com.ugts.service.impl;

import java.util.List;

import com.ugts.dto.request.PermissionRequest;
import com.ugts.dto.response.PermissionResponse;
import com.ugts.entity.Permission;
import com.ugts.mapper.PermissionMapper;
import com.ugts.repository.PermissionRepository;
import com.ugts.service.PermissionService;
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
