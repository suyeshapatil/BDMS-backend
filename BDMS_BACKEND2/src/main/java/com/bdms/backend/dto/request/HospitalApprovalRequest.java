package com.bdms.backend.dto.request;

import com.bdms.backend.enums.ApprovalStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalApprovalRequest {

    @NotNull(message = "Hospital id is required")
    private Long hospitalId;

    @NotNull(message = "Approval status is required")
    private ApprovalStatus approvalStatus;
}
