package com.bdms.backend.service;

import com.bdms.backend.dto.request.DonationHistoryCreateRequest;
import com.bdms.backend.dto.response.DonationHistoryDto;

import java.util.List;

public interface DonationHistoryService {
    DonationHistoryDto recordDonation(DonationHistoryCreateRequest request);
    List<DonationHistoryDto> getHistoryByDonor(Long donorId);
    List<DonationHistoryDto> getHistoryByHospital(Long hospitalId);
    List<DonationHistoryDto> getAllHistory();
}
