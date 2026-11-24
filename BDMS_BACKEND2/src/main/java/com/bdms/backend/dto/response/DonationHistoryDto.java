package com.bdms.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonationHistoryDto {
    private Long historyId;
    private Long donorId;
    private String donorName;
    private Long hospitalId;
    private String hospitalName;
    private LocalDate donationDate;
    private Integer unitsDonated;
    private String remarks;
}
