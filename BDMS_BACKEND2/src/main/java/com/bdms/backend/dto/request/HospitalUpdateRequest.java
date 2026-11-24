package com.bdms.backend.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalUpdateRequest {

    @Size(max = 200)
    private String hospitalName;

    @Size(max = 100)
    private String registrationNo;

    private String contactNo;
    private String city;
    private String address;
}
