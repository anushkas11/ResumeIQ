package com.resumewise.resumeiq.service;

import com.resumewise.resumeiq.dto.request.UserCreateRequest;
import com.resumewise.resumeiq.dto.response.UserResponse;

public interface UserService {

    UserResponse createUser(UserCreateRequest request);

    UserResponse getUserById(Long id);
}