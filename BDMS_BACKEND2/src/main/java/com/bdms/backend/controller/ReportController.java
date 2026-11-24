package com.bdms.backend.controller;

import com.bdms.backend.dto.request.ReportCreateRequest;
import com.bdms.backend.dto.response.ReportDto;
import com.bdms.backend.security.UserPrincipal;
import com.bdms.backend.service.AdminService;
import com.bdms.backend.service.ReportService;
import com.bdms.backend.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
@Tag(name = "Reporting", description = "Admin-generated system reports")
public class ReportController {

    private final ReportService reportService;
    private final AdminService adminService;

    @PostMapping
    @Operation(summary = "Generate a new report as the currently logged-in admin")
    public ResponseEntity<ApiResponse<ReportDto>> generateReport(@AuthenticationPrincipal UserPrincipal principal,
                                                                   @Valid @RequestBody ReportCreateRequest request) {
        Long adminId = adminService.getAdminIdByUserId(principal.getUserId());
        ReportDto dto = reportService.generateReport(adminId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Report generated successfully", dto));
    }

    @GetMapping
    @Operation(summary = "Get all generated reports")
    public ResponseEntity<ApiResponse<List<ReportDto>>> getAllReports() {
        return ResponseEntity.ok(ApiResponse.success("Reports fetched successfully", reportService.getAllReports()));
    }
}
