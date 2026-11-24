package com.bdms.backend.service.impl;

import com.bdms.backend.entity.Admin;
import com.bdms.backend.exception.ResourceNotFoundException;
import com.bdms.backend.repository.AdminRepository;
import com.bdms.backend.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    @Override
    public Long getAdminIdByUserId(Long userId) {
        Admin admin = adminRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin profile not found for user id: " + userId));
        return admin.getAdminId();
    }
}
