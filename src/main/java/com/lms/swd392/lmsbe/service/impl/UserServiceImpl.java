package com.lms.swd392.lmsbe.service.impl;

import com.lms.swd392.lmsbe.constant.UserRole;
import com.lms.swd392.lmsbe.constant.UserStatus;
import com.lms.swd392.lmsbe.entity.User;
import com.lms.swd392.lmsbe.exception.ConflictException;
import com.lms.swd392.lmsbe.exception.ResourceNotFoundException;
import com.lms.swd392.lmsbe.mapper.UserMapper;
import com.lms.swd392.lmsbe.model.request.RegisterRequest;
import com.lms.swd392.lmsbe.repository.UserRepository;
import com.lms.swd392.lmsbe.service.R2Service;
import com.lms.swd392.lmsbe.service.UserService;
import com.lms.swd392.lmsbe.validation.FileValidator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    R2Service r2Service;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;
    FileValidator fileValidator;

    @Override
    @Transactional
    public User register(RegisterRequest registerRequest, MultipartFile avatar) {
        // Business logic validation
        System.out.println("Filename: " + avatar.getOriginalFilename());
        System.out.println("Content-Type: " + avatar.getContentType());
        System.out.println("Size: " + avatar.getSize());

        validateUserUniqueness(registerRequest);

        // File validation
        fileValidator.validateAvatar(avatar);

        // Upload avatar
        String avatarUrl = r2Service.uploadFile(avatar, "avatar/" + registerRequest.getEmail());

        // Map and prepare user entity
        User user = userMapper.toUser(registerRequest);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(UserRole.BORROWER.getValue());
        user.setStatus(UserStatus.ACTIVE.getValue());
        user.setAvatarUrl(avatarUrl);



        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with username: " + username);
        }
        return user;
    }

    @Override
    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void updateStatus(Integer id, String status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        
        // Validate status
        boolean validStatus = false;
        for (UserStatus s : UserStatus.values()) {
            if (s.getValue().equals(status)) {
                validStatus = true;
                break;
            }
        }
        
        if (!validStatus) {
            throw new com.lms.swd392.lmsbe.exception.BadRequestException("Invalid user status: " + status);
        }

        user.setStatus(status);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateRole(Integer id, String role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Validate role
        boolean validRole = false;
        for (UserRole r : UserRole.values()) {
            if (r.getValue().equals(role)) {
                validRole = true;
                break;
            }
        }

        if (!validRole) {
            throw new com.lms.swd392.lmsbe.exception.BadRequestException("Invalid user role: " + role);
        }

        user.setRole(role);
        userRepository.save(user);
    }

    private void validateUserUniqueness(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email is already in use.");
        }
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new ConflictException("Phone number is already in use.");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ConflictException("Username is already in use.");
        }
    }
}
