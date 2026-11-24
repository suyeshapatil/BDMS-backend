package com.bdms.backend.service;

import com.bdms.backend.dto.request.BloodRequestCreateRequest;
import com.bdms.backend.dto.request.BloodRequestStatusUpdateRequest;
import com.bdms.backend.dto.response.BloodRequestDto;

import java.util.List;

public interface BloodRequestService {
    BloodRequestDto createRequest(Long hospitalId, BloodRequestCreateRequest request);
    BloodRequestDto getRequestById(Long requestId);
    List<BloodRequestDto> getAllRequests();
    List<BloodRequestDto> getRequestsByHospital(Long hospitalId);
    List<BloodRequestDto> getOpenRequestsForDonor(Long donorId);
    BloodRequestDto updateStatus(Long requestId, BloodRequestStatusUpdateRequest request);
    void deleteRequest(Long requestId);
}
