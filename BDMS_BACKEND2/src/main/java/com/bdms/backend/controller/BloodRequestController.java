package com.bdms.backend.controller;

import com.bdms.backend.dto.request.BloodRequestCreateRequest;
import com.bdms.backend.dto.request.BloodRequestStatusUpdateRequest;
import com.bdms.backend.dto.response.BloodRequestDto;
import com.bdms.backend.security.UserPrincipal;
import com.bdms.backend.service.BloodRequestService;
import com.bdms.backend.service.DonorService;
import com.bdms.backend.service.HospitalService;
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
@RequestMapping("/blood-requests")
@RequiredArgsConstructor
@Tag(name = "Blood Request Management", description = "Hospital-created blood requests and their lifecycle")
public class BloodRequestController {

    private final BloodRequestService bloodRequestService;
    private final HospitalService hospitalService;
    private final DonorService donorService;

    @PostMapping
    @Operation(summary = "Create a blood request as the currently logged-in hospital")
    public ResponseEntity<ApiResponse<BloodRequestDto>> createRequest(@AuthenticationPrincipal UserPrincipal principal,
                                                                        @Valid @RequestBody BloodRequestCreateRequest request) {
        Long hospitalId = hospitalService.getHospitalByUserId(principal.getUserId()).getHospitalId();
        BloodRequestDto dto = bloodRequestService.createRequest(hospitalId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Blood request created successfully", dto));
    }

    @GetMapping
    @Operation(summary = "Get all blood requests")
    public ResponseEntity<ApiResponse<List<BloodRequestDto>>> getAllRequests() {
        return ResponseEntity.ok(ApiResponse.success("Blood requests fetched successfully", bloodRequestService.getAllRequests()));
    }

    @GetMapping("/{requestId}")
    @Operation(summary = "Get a blood request by id")
    public ResponseEntity<ApiResponse<BloodRequestDto>> getRequestById(@PathVariable Long requestId) {
        return ResponseEntity.ok(ApiResponse.success("Blood request fetched successfully", bloodRequestService.getRequestById(requestId)));
    }

    @GetMapping("/hospital/{hospitalId}")
    @Operation(summary = "Get all blood requests created by a specific hospital")
    public ResponseEntity<ApiResponse<List<BloodRequestDto>>> getRequestsByHospital(@PathVariable Long hospitalId) {
        return ResponseEntity.ok(ApiResponse.success("Blood requests fetched successfully",
                bloodRequestService.getRequestsByHospital(hospitalId)));
    }

    @GetMapping("/matching-me")
    @Operation(summary = "Get open blood requests matching the currently logged-in donor's blood group")
    public ResponseEntity<ApiResponse<List<BloodRequestDto>>> getMatchingRequests(@AuthenticationPrincipal UserPrincipal principal) {
        Long donorId = donorService.getDonorByUserId(principal.getUserId()).getDonorId();
        return ResponseEntity.ok(ApiResponse.success("Matching blood requests fetched successfully",
                bloodRequestService.getOpenRequestsForDonor(donorId)));
    }

    @PatchMapping("/{requestId}/status")
    @Operation(summary = "Update the status of a blood request")
    public ResponseEntity<ApiResponse<BloodRequestDto>> updateStatus(@PathVariable Long requestId,
                                                                       @Valid @RequestBody BloodRequestStatusUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Blood request status updated successfully",
                bloodRequestService.updateStatus(requestId, request)));
    }

    @DeleteMapping("/{requestId}")
    @Operation(summary = "Delete/cancel a blood request")
    public ResponseEntity<ApiResponse<Object>> deleteRequest(@PathVariable Long requestId) {
        bloodRequestService.deleteRequest(requestId);
        return ResponseEntity.ok(ApiResponse.success("Blood request deleted successfully"));
    }
}
