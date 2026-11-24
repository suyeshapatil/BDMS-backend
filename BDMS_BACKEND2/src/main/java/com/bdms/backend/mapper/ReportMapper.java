package com.bdms.backend.mapper;

import com.bdms.backend.dto.response.ReportDto;
import com.bdms.backend.entity.Report;

public final class ReportMapper {

    private ReportMapper() {
    }

    public static ReportDto toDto(Report report) {
        if (report == null) {
            return null;
        }
        return ReportDto.builder()
                .reportId(report.getReportId())
                .reportName(report.getReportName())
                .generatedByAdminId(report.getGeneratedBy() != null ? report.getGeneratedBy().getAdminId() : null)
                .generatedByAdminName(report.getGeneratedBy() != null && report.getGeneratedBy().getUser() != null
                        ? report.getGeneratedBy().getUser().getFullName() : null)
                .generatedOn(report.getGeneratedOn())
                .build();
    }
}
