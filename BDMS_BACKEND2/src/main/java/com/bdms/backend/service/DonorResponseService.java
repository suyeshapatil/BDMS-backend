package com.bdms.backend.service;

import com.bdms.backend.dto.request.DonorResponseCreateRequest;
import com.bdms.backend.dto.request.DonorResponseStatusUpdateRequest;
import com.bdms.backend.dto.response.DonorResponseRecordDto;
import com.bdms.backend.enums.ResponseStatus;

import java.util.List;

public interface DonorResponseService {
    DonorResponseRecordDto respondToRequest(Long donorId, DonorResponseCreateRequest request, ResponseStatus status);
    DonorResponseRecordDto updateResponseStatus(Long responseId, DonorResponseStatusUpdateRequest request);
    List<DonorResponseRecordDto> getResponsesByDonor(Long donorId);
    List<DonorResponseRecordDto> getResponsesByRequest(Long requestId);
}
