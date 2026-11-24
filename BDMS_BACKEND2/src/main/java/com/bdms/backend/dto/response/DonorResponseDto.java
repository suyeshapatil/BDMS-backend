package com.bdms.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonorResponseDto {
    private Long donorId;
    private Long userId;
    private String fullName;
    private String email;
    private Integer age;
    private String gender;
    private String bloodGroup;
    private String mobile;
    private String city;
    private String address;
    private Boolean availability;
    private LocalDate lastDonationDate;
    private Integer totalDonations;
}
