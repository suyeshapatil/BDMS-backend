package com.bdms.backend.dto.response;

import com.bdms.backend.enums.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO representing a row in the donor_responses table
 * (i.e. a donor's Accept/Reject response to a specific blood request).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonorResponseRecordDto {
    private Long responseId;
    private Long requestId;
    private Long donorId;
    private String donorName;
    private ResponseStatus responseStatus;
    private LocalDateTime responseDate;
}
