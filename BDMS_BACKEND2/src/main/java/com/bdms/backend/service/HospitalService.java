package com.bdms.backend.service;

import com.bdms.backend.dto.request.HospitalUpdateRequest;
import com.bdms.backend.dto.response.HospitalDto;

import java.util.List;

public interface HospitalService {
    HospitalDto getHospitalById(Long hospitalId);
    HospitalDto getHospitalByUserId(Long userId);
    List<HospitalDto> getAllHospitals();
    List<HospitalDto> getVerifiedHospitals();
    HospitalDto updateHospital(Long hospitalId, HospitalUpdateRequest request);
    void deleteHospital(Long hospitalId);
}
