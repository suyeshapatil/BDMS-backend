package com.bdms.backend.mapper;

import com.bdms.backend.dto.response.HospitalDto;
import com.bdms.backend.entity.Hospital;

public final class HospitalMapper {

    private HospitalMapper() {
    }

    public static HospitalDto toDto(Hospital hospital) {
        if (hospital == null) {
            return null;
        }
        return HospitalDto.builder()
                .hospitalId(hospital.getHospitalId())
                .userId(hospital.getUser() != null ? hospital.getUser().getUserId() : null)
                .hospitalName(hospital.getHospitalName())
                .registrationNo(hospital.getRegistrationNo())
                .email(hospital.getEmail())
                .contactNo(hospital.getContactNo())
                .city(hospital.getCity())
                .address(hospital.getAddress())
                .verified(hospital.getVerified())
                .createdAt(hospital.getCreatedAt())
                .build();
    }
}
