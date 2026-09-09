package com.resumewise.resumeiq.service.impl;

import com.resumewise.resumeiq.dto.request.UserCreateRequest;
import com.resumewise.resumeiq.dto.response.UserResponse;
import com.resumewise.resumeiq.entity.User;
import com.resumewise.resumeiq.exception.DuplicateResourceException;
import com.resumewise.resumeiq.exception.ResourceNotFoundException;
import com.resumewise.resumeiq.repository.UserRepository;
import com.resumewise.resumeiq.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse createUser(UserCreateRequest request) {

        // Check if email is already registered
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                    "User with email " + request.getEmail() + " already exists"
            );
        }

        // Create User entity
        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        // Hash password before storing it
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        // Save user to database
        User savedUser = userRepository.save(user);

        // Return response DTO
        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail()
        );
    }

    @Override
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User with id " + id + " not found"
                        )
                );

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}