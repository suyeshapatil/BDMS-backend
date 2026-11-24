package com.bdms.backend.dto.request;

import com.bdms.backend.enums.UrgencyLevel;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BloodRequestCreateRequest {

    @NotBlank(message = "Patient name is required")
    @Size(max = 100)
    private String patientName;

    @NotBlank(message = "Blood group is required")
    @Pattern(regexp = "^(A|B|AB|O)[+-]$", message = "Blood group must be a valid group e.g. A+, O-, AB+")
    private String bloodGroup;

    @NotNull(message = "Units required is mandatory")
    @Min(value = 1, message = "At least 1 unit must be requested")
    private Integer unitsRequired;

    @NotBlank(message = "City is required")
    private String city;

    @NotNull(message = "Urgency level is required")
    private UrgencyLevel urgencyLevel;
}
