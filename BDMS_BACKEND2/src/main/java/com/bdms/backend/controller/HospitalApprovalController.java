package com.bdms.backend.controller;

import com.bdms.backend.dto.request.HospitalApprovalRequest;
import com.bdms.backend.dto.response.HospitalApprovalDto;
import com.bdms.backend.security.UserPrincipal;
import com.bdms.backend.service.AdminService;
import com.bdms.backend.service.HospitalApprovalService;
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
@RequestMapping("/hospital-approvals")
@RequiredArgsConstructor
@Tag(name = "Hospital Approval Management", description = "Admin verification/rejection of hospital registrations")
public class HospitalApprovalController {

    private final HospitalApprovalService hospitalApprovalService;
    private final AdminService adminService;

    @PostMapping
    @Operation(summary = "Approve or reject a hospital as the currently logged-in admin")
    public ResponseEntity<ApiResponse<HospitalApprovalDto>> processApproval(@AuthenticationPrincipal UserPrincipal principal,
                                                                              @Valid @RequestBody HospitalApprovalRequest request) {
        Long adminId = adminService.getAdminIdByUserId(principal.getUserId());
        HospitalApprovalDto dto = hospitalApprovalService.processApproval(adminId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Hospital approval processed successfully", dto));
    }

    @GetMapping
    @Operation(summary = "Get all hospital approval records")
    public ResponseEntity<ApiResponse<List<HospitalApprovalDto>>> getAllApprovals() {
        return ResponseEntity.ok(ApiResponse.success("Approvals fetched successfully", hospitalApprovalService.getAllApprovals()));
    }

    @GetMapping("/hospital/{hospitalId}")
    @Operation(summary = "Get approval history for a specific hospital")
    public ResponseEntity<ApiResponse<List<HospitalApprovalDto>>> getByHospital(@PathVariable Long hospitalId) {
        return ResponseEntity.ok(ApiResponse.success("Approvals fetched successfully",
                hospitalApprovalService.getApprovalsByHospital(hospitalId)));
    }
}
