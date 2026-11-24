package com.bdms.backend.mapper;

import com.bdms.backend.dto.response.HospitalApprovalDto;
import com.bdms.backend.entity.HospitalApproval;

public final class HospitalApprovalMapper {

    private HospitalApprovalMapper() {
    }

    public static HospitalApprovalDto toDto(HospitalApproval approval) {
        if (approval == null) {
            return null;
        }
        return HospitalApprovalDto.builder()
                .approvalId(approval.getApprovalId())
                .hospitalId(approval.getHospital() != null ? approval.getHospital().getHospitalId() : null)
                .hospitalName(approval.getHospital() != null ? approval.getHospital().getHospitalName() : null)
                .adminId(approval.getAdmin() != null ? approval.getAdmin().getAdminId() : null)
                .approvalStatus(approval.getApprovalStatus())
                .approvalDate(approval.getApprovalDate())
                .build();
    }
}
