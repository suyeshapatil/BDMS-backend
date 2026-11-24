package com.bdms.backend.controller;

import com.bdms.backend.dto.request.DonorAvailabilityRequest;
import com.bdms.backend.dto.request.DonorUpdateRequest;
import com.bdms.backend.dto.response.DonorResponseDto;
import com.bdms.backend.security.UserPrincipal;
import com.bdms.backend.service.DonorService;
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
@RequestMapping("/donors")
@RequiredArgsConstructor
@Tag(name = "Donor Management", description = "Donor profile, availability, and search endpoints")
public class DonorController {

    private final DonorService donorService;

    @GetMapping
    @Operation(summary = "Get all donors, or search by bloodGroup and/or city")
    public ResponseEntity<ApiResponse<List<DonorResponseDto>>> getDonors(
            @RequestParam(required = false) String bloodGroup,
            @RequestParam(required = false) String city) {
        List<DonorResponseDto> donors = (bloodGroup != null || city != null)
                ? donorService.searchDonors(bloodGroup, city)
                : donorService.getAllDonors();
        return ResponseEntity.ok(ApiResponse.success("Donors fetched successfully", donors));
    }

    @GetMapping("/{donorId}")
    @Operation(summary = "Get a donor profile by donor id")
    public ResponseEntity<ApiResponse<DonorResponseDto>> getDonorById(@PathVariable Long donorId) {
        return ResponseEntity.ok(ApiResponse.success("Donor fetched successfully", donorService.getDonorById(donorId)));
    }

    @GetMapping("/me")
    @Operation(summary = "Get the currently logged-in donor's own profile")
    public ResponseEntity<ApiResponse<DonorResponseDto>> getMyProfile(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.success("Profile fetched successfully",
                donorService.getDonorByUserId(principal.getUserId())));
    }

    @PutMapping("/{donorId}")
    @Operation(summary = "Update a donor profile")
    public ResponseEntity<ApiResponse<DonorResponseDto>> updateProfile(@PathVariable Long donorId,
                                                                         @Valid @RequestBody DonorUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully",
                donorService.updateDonorProfile(donorId, request)));
    }

    @PatchMapping("/{donorId}/availability")
    @Operation(summary = "Update a donor's availability status")
    public ResponseEntity<ApiResponse<DonorResponseDto>> updateAvailability(@PathVariable Long donorId,
                                                                              @Valid @RequestBody DonorAvailabilityRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Availability updated successfully",
                donorService.updateAvailability(donorId, request)));
    }

    @DeleteMapping("/{donorId}")
    @Operation(summary = "Delete a donor profile (admin only)")
    public ResponseEntity<ApiResponse<Object>> deleteDonor(@PathVariable Long donorId) {
        donorService.deleteDonor(donorId);
        return ResponseEntity.ok(ApiResponse.success("Donor deleted successfully"));
    }
}
