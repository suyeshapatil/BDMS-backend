package com.bdms.backend.service.impl;

import com.bdms.backend.dto.request.ReportCreateRequest;
import com.bdms.backend.dto.response.ReportDto;
import com.bdms.backend.entity.Admin;
import com.bdms.backend.entity.Report;
import com.bdms.backend.exception.ResourceNotFoundException;
import com.bdms.backend.mapper.ReportMapper;
import com.bdms.backend.repository.AdminRepository;
import com.bdms.backend.repository.ReportRepository;
import com.bdms.backend.service.ReportService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final AdminRepository adminRepository;

    @Override
    @Transactional
    public ReportDto generateReport(Long adminId, ReportCreateRequest request) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with id: " + adminId));

        Report report = Report.builder()
                .reportName(request.getReportName())
                .generatedBy(admin)
                .build();

        return ReportMapper.toDto(reportRepository.save(report));
    }

    @Override
    public List<ReportDto> getAllReports() {
        return reportRepository.findAll().stream().map(ReportMapper::toDto).toList();
    }
}
