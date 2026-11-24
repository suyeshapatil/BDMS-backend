package com.bdms.backend.service;

import com.bdms.backend.dto.request.HospitalApprovalRequest;
import com.bdms.backend.dto.response.HospitalApprovalDto;

import java.util.List;

public interface HospitalApprovalService {
    HospitalApprovalDto processApproval(Long adminId, HospitalApprovalRequest request);
    List<HospitalApprovalDto> getApprovalsByHospital(Long hospitalId);
    List<HospitalApprovalDto> getAllApprovals();
}
