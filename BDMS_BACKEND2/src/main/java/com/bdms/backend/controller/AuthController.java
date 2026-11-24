package com.bdms.backend.controller;

import com.bdms.backend.dto.request.RegisterRequest;
import com.bdms.backend.dto.request.LoginRequest;
import com.bdms.backend.dto.request.RefreshTokenRequest;
import com.bdms.backend.dto.request.ForgotPasswordRequest;
import com.bdms.backend.dto.request.ResetPasswordRequest;
import com.bdms.backend.dto.request.ChangePasswordRequest;
import com.bdms.backend.dto.response.AuthResponse;
import com.bdms.backend.security.UserPrincipal;
import com.bdms.backend.service.AuthService;
import com.bdms.backend.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Registration, login, JWT refresh, and password management")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/registr")
    @Operation(summary = "Register a new Donor, Hospital, or Admin account")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Registration successful", response));
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate a user and issue JWT access/refresh tokens")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Exchange a valid refresh token for a new access/refresh token pair")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        AuthResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(ApiResponse.success("Token refreshed successfully", response));
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Request a password reset token via email")
    public ResponseEntity<ApiResponse<Object>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return ResponseEntity.ok(ApiResponse.success("If the email exists, a password reset link has been sent"));
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password using the token emailed to the user")
    public ResponseEntity<ApiResponse<Object>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.success("Password reset successful. Please log in with your new password."));
    }

    @PostMapping("/change-password")
    @Operation(summary = "Change the currently authenticated user's password")
    public ResponseEntity<ApiResponse<Object>> changePassword(@AuthenticationPrincipal UserPrincipal principal,
                                                                @Valid @RequestBody ChangePasswordRequest request) {
        authService.changePassword(principal.getUserId(), request);
        return ResponseEntity.ok(ApiResponse.success("Password changed successfully"));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout (stateless JWT - client discards tokens)")
    public ResponseEntity<ApiResponse<Object>> logout() {
        return ResponseEntity.ok(ApiResponse.success("Logged out successfully. Please discard your tokens."));
    }
}
