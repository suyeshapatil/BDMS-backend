package com.bdms.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalDto {
    private Long hospitalId;
    private Long userId;
    private String hospitalName;
    private String registrationNo;
    private String email;
    private String contactNo;
    private String city;
    private String address;
    private Boolean verified;
    private LocalDateTime createdAt;
}
