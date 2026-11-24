package com.bdms.backend.mapper;

import com.bdms.backend.dto.response.DonationHistoryDto;
import com.bdms.backend.entity.DonationHistory;

public final class DonationHistoryMapper {

    private DonationHistoryMapper() {
    }

    public static DonationHistoryDto toDto(DonationHistory history) {
        if (history == null) {
            return null;
        }
        return DonationHistoryDto.builder()
                .historyId(history.getHistoryId())
                .donorId(history.getDonor() != null ? history.getDonor().getDonorId() : null)
                .donorName(history.getDonor() != null && history.getDonor().getUser() != null
                        ? history.getDonor().getUser().getFullName() : null)
                .hospitalId(history.getHospital() != null ? history.getHospital().getHospitalId() : null)
                .hospitalName(history.getHospital() != null ? history.getHospital().getHospitalName() : null)
                .donationDate(history.getDonationDate())
                .unitsDonated(history.getUnitsDonated())
                .remarks(history.getRemarks())
                .build();
    }
}
