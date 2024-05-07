package com.ugts.service.impl;

import java.util.HashSet;
import java.util.List;

import com.ugts.constant.PredefinedRole;
import com.ugts.dto.request.RegisterRequest;
import com.ugts.dto.response.UserResponse;
import com.ugts.entity.Role;
import com.ugts.entity.User;
import com.ugts.exception.AppException;
import com.ugts.exception.ErrorCode;
import com.ugts.mapper.UserMapper;
import com.ugts.repository.RoleRepository;
import com.ugts.repository.UserRepository;
import com.ugts.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    RoleRepository roleRepository;

    UserMapper userMapper;

    PasswordEncoder passwordEncoder;

    @Override
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        if (Boolean.TRUE.equals(userRepository.existsByPhoneNumber(request.getPhoneNumber()))) {
            throw new AppException(ErrorCode.PHONE_NUMBER_EXISTED);
        }
        if (Boolean.TRUE.equals(userRepository.existsByEmail(request.getEmail()))) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        User user = userMapper.register(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);

        user.setRoles(roles);

        return userMapper.userToUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')") // verify that the user is ADMIN before getAllUsers() is called
    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::userToUserResponse)
                .toList();
    }

    @PostAuthorize("returnObject.username == authentication.name")
    @Override
    public UserResponse getUserById(String userId) {
        return userMapper.userToUserResponse(
                userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }
}
