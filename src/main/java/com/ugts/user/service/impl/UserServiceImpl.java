package com.ugts.user.service.impl;

import java.util.HashSet;
import java.util.List;

import com.ugts.authentication.dto.request.RegisterRequest;
import com.ugts.constant.PredefinedRole;
import com.ugts.user.dto.response.UserResponse;
import com.ugts.user.entity.Role;
import com.ugts.user.entity.User;
import com.ugts.user.exception.UserErrorCode;
import com.ugts.user.exception.UserException;
import com.ugts.user.mapper.UserMapper;
import com.ugts.user.repository.RoleRepository;
import com.ugts.user.repository.UserRepository;
import com.ugts.user.service.UserService;
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
            throw new UserException(UserErrorCode.USER_EXISTED);
        }
        if (Boolean.TRUE.equals(userRepository.existsByPhoneNumber(request.getPhoneNumber()))) {
            throw new UserException(UserErrorCode.PHONE_NUMBER_EXISTED);
        }
        if (Boolean.TRUE.equals(userRepository.existsByEmail(request.getEmail()))) {
            throw new UserException(UserErrorCode.EMAIL_EXISTED);
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
                userRepository.findById(userId).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_EXISTED)));
    }
}
