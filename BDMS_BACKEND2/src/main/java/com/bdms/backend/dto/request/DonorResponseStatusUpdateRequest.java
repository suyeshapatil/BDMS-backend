package com.bdms.backend.dto.request;

import com.bdms.backend.enums.ResponseStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonorResponseStatusUpdateRequest {

    @NotNull(message = "Response status is required")
    private ResponseStatus responseStatus;
}
