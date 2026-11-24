package com.bdms.backend.mapper;

import com.bdms.backend.dto.response.DonorResponseRecordDto;
import com.bdms.backend.entity.DonorResponse;

public final class DonorResponseRecordMapper {

    private DonorResponseRecordMapper() {
    }

    public static DonorResponseRecordDto toDto(DonorResponse response) {
        if (response == null) {
            return null;
        }
        return DonorResponseRecordDto.builder()
                .responseId(response.getResponseId())
                .requestId(response.getBloodRequest() != null ? response.getBloodRequest().getRequestId() : null)
                .donorId(response.getDonor() != null ? response.getDonor().getDonorId() : null)
                .donorName(response.getDonor() != null && response.getDonor().getUser() != null
                        ? response.getDonor().getUser().getFullName() : null)
                .responseStatus(response.getResponseStatus())
                .responseDate(response.getResponseDate())
                .build();
    }
}
