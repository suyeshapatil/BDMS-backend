package com.bdms.backend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonationHistoryCreateRequest {

    @NotNull(message = "Donor id is required")
    private Long donorId;

    @NotNull(message = "Hospital id is required")
    private Long hospitalId;

    @NotNull(message = "Donation date is required")
    private LocalDate donationDate;

    @NotNull(message = "Units donated is required")
    @Min(value = 1, message = "At least 1 unit must be recorded")
    private Integer unitsDonated;

    private String remarks;
}
