package com.bdms.backend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonorUpdateRequest {

    @Min(value = 18, message = "Donor must be at least 18 years old")
    private Integer age;

    private String gender;

    @Pattern(regexp = "^(A|B|AB|O)[+-]$", message = "Blood group must be a valid group e.g. A+, O-, AB+")
    private String bloodGroup;

    private String mobile;
    private String city;
    private String address;
}
