package com.bdms.backend.controller;

import com.bdms.backend.dto.request.DonationHistoryCreateRequest;
import com.bdms.backend.dto.response.DonationHistoryDto;
import com.bdms.backend.service.DonationHistoryService;
import com.bdms.backend.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/donation-history")
@RequiredArgsConstructor
@Tag(name = "Donation History Management", description = "Recording and viewing completed donations")
public class DonationHistoryController {

    private final DonationHistoryService donationHistoryService;

    @PostMapping
    @Operation(summary = "Record a completed donation (hospital/admin)")
    public ResponseEntity<ApiResponse<DonationHistoryDto>> recordDonation(@Valid @RequestBody DonationHistoryCreateRequest request) {
        DonationHistoryDto dto = donationHistoryService.recordDonation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Donation recorded successfully", dto));
    }

    @GetMapping
    @Operation(summary = "Get all donation history records")
    public ResponseEntity<ApiResponse<List<DonationHistoryDto>>> getAllHistory() {
        return ResponseEntity.ok(ApiResponse.success("Donation history fetched successfully", donationHistoryService.getAllHistory()));
    }

    @GetMapping("/donor/{donorId}")
    @Operation(summary = "Get donation history for a specific donor")
    public ResponseEntity<ApiResponse<List<DonationHistoryDto>>> getByDonor(@PathVariable Long donorId) {
        return ResponseEntity.ok(ApiResponse.success("Donation history fetched successfully",
                donationHistoryService.getHistoryByDonor(donorId)));
    }

    @GetMapping("/hospital/{hospitalId}")
    @Operation(summary = "Get donation history for a specific hospital")
    public ResponseEntity<ApiResponse<List<DonationHistoryDto>>> getByHospital(@PathVariable Long hospitalId) {
        return ResponseEntity.ok(ApiResponse.success("Donation history fetched successfully",
                donationHistoryService.getHistoryByHospital(hospitalId)));
    }
}
