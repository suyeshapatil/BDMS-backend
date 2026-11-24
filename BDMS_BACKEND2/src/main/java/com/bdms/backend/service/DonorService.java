package com.bdms.backend.service;

import com.bdms.backend.dto.request.DonorAvailabilityRequest;
import com.bdms.backend.dto.request.DonorUpdateRequest;
import com.bdms.backend.dto.response.DonorResponseDto;

import java.util.List;

public interface DonorService {
    DonorResponseDto getDonorById(Long donorId);
    DonorResponseDto getDonorByUserId(Long userId);
    List<DonorResponseDto> getAllDonors();
    List<DonorResponseDto> searchDonors(String bloodGroup, String city);
    DonorResponseDto updateDonorProfile(Long donorId, DonorUpdateRequest request);
    DonorResponseDto updateAvailability(Long donorId, DonorAvailabilityRequest request);
    void deleteDonor(Long donorId);
}
