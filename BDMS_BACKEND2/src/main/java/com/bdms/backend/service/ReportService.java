package com.bdms.backend.service;

import com.bdms.backend.dto.request.ReportCreateRequest;
import com.bdms.backend.dto.response.ReportDto;

import java.util.List;

public interface ReportService {
    ReportDto generateReport(Long adminId, ReportCreateRequest request);
    List<ReportDto> getAllReports();
}
