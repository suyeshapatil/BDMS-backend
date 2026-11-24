package com.bdms.backend.dto.request;

import com.bdms.backend.enums.RequestStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BloodRequestStatusUpdateRequest {

    @NotNull(message = "Request status is required")
    private RequestStatus requestStatus;
}
