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
public class ReportDto {
    private Long reportId;
    private String reportName;
    private Long generatedByAdminId;
    private String generatedByAdminName;
    private LocalDateTime generatedOn;
}
