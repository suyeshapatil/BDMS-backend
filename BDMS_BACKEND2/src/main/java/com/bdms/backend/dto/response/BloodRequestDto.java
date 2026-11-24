package com.bdms.backend.dto.response;

import com.bdms.backend.enums.RequestStatus;
import com.bdms.backend.enums.UrgencyLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BloodRequestDto {
    private Long requestId;
    private Long hospitalId;
    private String hospitalName;
    private String patientName;
    private String bloodGroup;
    private Integer unitsRequired;
    private String city;
    private UrgencyLevel urgencyLevel;
    private RequestStatus requestStatus;
    private LocalDateTime createdAt;
}
