package com.bdms.backend.controller;

import com.bdms.backend.dto.request.DonorResponseCreateRequest;
import com.bdms.backend.dto.request.DonorResponseStatusUpdateRequest;
import com.bdms.backend.dto.response.DonorResponseRecordDto;
import com.bdms.backend.enums.ResponseStatus;
import com.bdms.backend.security.UserPrincipal;
import com.bdms.backend.service.DonorResponseService;
import com.bdms.backend.service.DonorService;
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
@RequestMapping("/donor-responses")
@RequiredArgsConstructor
@Tag(name = "Donor Response Management", description = "Donors accepting/rejecting blood requests")
public class DonorResponseController {

    private final DonorResponseService donorResponseService;
    private final DonorService donorService;

    @PostMapping("/accept")
    @Operation(summary = "Accept a blood request as the currently logged-in donor")
    public ResponseEntity<ApiResponse<DonorResponseRecordDto>> accept(@AuthenticationPrincipal UserPrincipal principal,
                                                                        @Valid @RequestBody DonorResponseCreateRequest request) {
        Long donorId = donorService.getDonorByUserId(principal.getUserId()).getDonorId();
        DonorResponseRecordDto dto = donorResponseService.respondToRequest(donorId, request, ResponseStatus.ACCEPTED);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Blood request accepted", dto));
    }

    @PostMapping("/reject")
    @Operation(summary = "Reject a blood request as the currently logged-in donor")
    public ResponseEntity<ApiResponse<DonorResponseRecordDto>> reject(@AuthenticationPrincipal UserPrincipal principal,
                                                                        @Valid @RequestBody DonorResponseCreateRequest request) {
        Long donorId = donorService.getDonorByUserId(principal.getUserId()).getDonorId();
        DonorResponseRecordDto dto = donorResponseService.respondToRequest(donorId, request, ResponseStatus.REJECTED);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Blood request rejected", dto));
    }

    @PatchMapping("/{responseId}/status")
    @Operation(summary = "Update a donor response's status (admin/hospital)")
    public ResponseEntity<ApiResponse<DonorResponseRecordDto>> updateStatus(@PathVariable Long responseId,
                                                                              @Valid @RequestBody DonorResponseStatusUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Response status updated successfully",
                donorResponseService.updateResponseStatus(responseId, request)));
    }

    @GetMapping("/donor/{donorId}")
    @Operation(summary = "Get all responses made by a specific donor")
    public ResponseEntity<ApiResponse<List<DonorResponseRecordDto>>> getByDonor(@PathVariable Long donorId) {
        return ResponseEntity.ok(ApiResponse.success("Responses fetched successfully",
                donorResponseService.getResponsesByDonor(donorId)));
    }

    @GetMapping("/request/{requestId}")
    @Operation(summary = "Get all donor responses to a specific blood request")
    public ResponseEntity<ApiResponse<List<DonorResponseRecordDto>>> getByRequest(@PathVariable Long requestId) {
        return ResponseEntity.ok(ApiResponse.success("Responses fetched successfully",
                donorResponseService.getResponsesByRequest(requestId)));
    }
}
