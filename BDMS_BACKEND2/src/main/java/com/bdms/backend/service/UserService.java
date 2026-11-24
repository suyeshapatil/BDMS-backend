package com.bdms.backend.service;

import com.bdms.backend.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse getUserById(Long userId);
    List<UserResponse> getAllUsers();
    void deactivateUser(Long userId);
    void activateUser(Long userId);
    void deleteUser(Long userId);
}
