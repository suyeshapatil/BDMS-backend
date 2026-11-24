package com.bdms.backend.mapper;

import com.bdms.backend.dto.response.BloodRequestDto;
import com.bdms.backend.entity.BloodRequest;

public final class BloodRequestMapper {

    private BloodRequestMapper() {
    }

    public static BloodRequestDto toDto(BloodRequest request) {
        if (request == null) {
            return null;
        }
        return BloodRequestDto.builder()
                .requestId(request.getRequestId())
                .hospitalId(request.getHospital() != null ? request.getHospital().getHospitalId() : null)
                .hospitalName(request.getHospital() != null ? request.getHospital().getHospitalName() : null)
                .patientName(request.getPatientName())
                .bloodGroup(request.getBloodGroup())
                .unitsRequired(request.getUnitsRequired())
                .city(request.getCity())
                .urgencyLevel(request.getUrgencyLevel())
                .requestStatus(request.getRequestStatus())
                .createdAt(request.getCreatedAt())
                .build();
    }
}
