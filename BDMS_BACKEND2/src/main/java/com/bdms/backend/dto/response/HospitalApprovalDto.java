package com.bdms.backend.dto.response;

import com.bdms.backend.enums.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalApprovalDto {
    private Long approvalId;
    private Long hospitalId;
    private String hospitalName;
    private Long adminId;
    private ApprovalStatus approvalStatus;
    private LocalDateTime approvalDate;
}
