package com.bdms.backend.controller;

import com.bdms.backend.dto.request.HospitalUpdateRequest;
import com.bdms.backend.dto.response.HospitalDto;
import com.bdms.backend.security.UserPrincipal;
import com.bdms.backend.service.HospitalService;
import com.bdms.backend.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hospitals")
@RequiredArgsConstructor
@Tag(name = "Hospital Management", description = "Hospital profile and verification-status endpoints")
public class HospitalController {

    private final HospitalService hospitalService;

    @GetMapping
    @Operation(summary = "Get all hospitals (admin/hospital)")
    public ResponseEntity<ApiResponse<List<HospitalDto>>> getAllHospitals() {
        return ResponseEntity.ok(ApiResponse.success("Hospitals fetched successfully", hospitalService.getAllHospitals()));
    }

    @GetMapping("/verified")
    @Operation(summary = "Get all verified hospitals (public)")
    public ResponseEntity<ApiResponse<List<HospitalDto>>> getVerifiedHospitals() {
        return ResponseEntity.ok(ApiResponse.success("Verified hospitals fetched successfully", hospitalService.getVerifiedHospitals()));
    }

    @GetMapping("/{hospitalId}")
    @Operation(summary = "Get a hospital by id")
    public ResponseEntity<ApiResponse<HospitalDto>> getHospitalById(@PathVariable Long hospitalId) {
        return ResponseEntity.ok(ApiResponse.success("Hospital fetched successfully", hospitalService.getHospitalById(hospitalId)));
    }

    @GetMapping("/me")
    @Operation(summary = "Get the currently logged-in hospital's own profile")
    public ResponseEntity<ApiResponse<HospitalDto>> getMyProfile(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.success("Profile fetched successfully",
                hospitalService.getHospitalByUserId(principal.getUserId())));
    }

    @PutMapping("/{hospitalId}")
    @Operation(summary = "Update a hospital profile")
    public ResponseEntity<ApiResponse<HospitalDto>> updateHospital(@PathVariable Long hospitalId,
                                                                     @Valid @RequestBody HospitalUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Hospital updated successfully",
                hospitalService.updateHospital(hospitalId, request)));
    }

    @DeleteMapping("/{hospitalId}")
    @Operation(summary = "Delete a hospital (admin only)")
    public ResponseEntity<ApiResponse<Object>> deleteHospital(@PathVariable Long hospitalId) {
        hospitalService.deleteHospital(hospitalId);
        return ResponseEntity.ok(ApiResponse.success("Hospital deleted successfully"));
    }
}
