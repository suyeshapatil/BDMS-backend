package com.bdms.backend.mapper;

import com.bdms.backend.dto.response.UserResponse;
import com.bdms.backend.entity.User;

public final class UserMapper {

    private UserMapper() {
    }

    public static UserResponse toDto(User user) {
        if (user == null) {
            return null;
        }
        return UserResponse.builder()
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
