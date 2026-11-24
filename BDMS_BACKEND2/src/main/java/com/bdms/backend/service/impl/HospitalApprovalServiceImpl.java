package com.bdms.backend.service.impl;

import com.bdms.backend.dto.request.HospitalApprovalRequest;
import com.bdms.backend.dto.response.HospitalApprovalDto;
import com.bdms.backend.entity.Admin;
import com.bdms.backend.entity.Hospital;
import com.bdms.backend.entity.HospitalApproval;
import com.bdms.backend.enums.ApprovalStatus;
import com.bdms.backend.exception.ResourceNotFoundException;
import com.bdms.backend.mapper.HospitalApprovalMapper;
import com.bdms.backend.repository.AdminRepository;
import com.bdms.backend.repository.HospitalApprovalRepository;
import com.bdms.backend.repository.HospitalRepository;
import com.bdms.backend.service.EmailService;
import com.bdms.backend.service.HospitalApprovalService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HospitalApprovalServiceImpl implements HospitalApprovalService {

    private final HospitalApprovalRepository hospitalApprovalRepository;
    private final HospitalRepository hospitalRepository;
    private final AdminRepository adminRepository;
    private final EmailService emailService;

    @Override
    @Transactional
    public HospitalApprovalDto processApproval(Long adminId, HospitalApprovalRequest request) {
        Hospital hospital = hospitalRepository.findById(request.getHospitalId())
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with id: " + request.getHospitalId()));
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with id: " + adminId));

        HospitalApproval approval = HospitalApproval.builder()
                .hospital(hospital)
                .admin(admin)
                .approvalStatus(request.getApprovalStatus())
                .approvalDate(LocalDateTime.now())
                .build();
        approval = hospitalApprovalRepository.save(approval);

        hospital.setVerified(request.getApprovalStatus() == ApprovalStatus.APPROVED);
        hospitalRepository.save(hospital);

        if (hospital.getEmail() != null) {
            emailService.sendHospitalApprovalStatus(hospital.getEmail(), hospital.getHospitalName(),
                    request.getApprovalStatus() == ApprovalStatus.APPROVED);
        }

        return HospitalApprovalMapper.toDto(approval);
    }

    @Override
    public List<HospitalApprovalDto> getApprovalsByHospital(Long hospitalId) {
        return hospitalApprovalRepository.findByHospital_HospitalId(hospitalId).stream()
                .map(HospitalApprovalMapper::toDto).toList();
    }

    @Override
    public List<HospitalApprovalDto> getAllApprovals() {
        return hospitalApprovalRepository.findAll().stream().map(HospitalApprovalMapper::toDto).toList();
    }
}
