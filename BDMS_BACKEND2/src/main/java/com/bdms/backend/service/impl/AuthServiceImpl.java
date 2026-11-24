package com.bdms.backend.service.impl;

import com.bdms.backend.dto.request.*;
import com.bdms.backend.dto.response.AuthResponse;
import com.bdms.backend.entity.Admin;
import com.bdms.backend.entity.Donor;
import com.bdms.backend.entity.Hospital;
import com.bdms.backend.entity.Notification;
import com.bdms.backend.entity.User;
import com.bdms.backend.exception.BadRequestException;
import com.bdms.backend.exception.DuplicateResourceException;
import com.bdms.backend.exception.InvalidCredentialsException;
import com.bdms.backend.exception.InvalidTokenException;
import com.bdms.backend.exception.ResourceNotFoundException;
import com.bdms.backend.repository.AdminRepository;
import com.bdms.backend.repository.DonorRepository;
import com.bdms.backend.repository.HospitalRepository;
import com.bdms.backend.repository.NotificationRepository;
import com.bdms.backend.repository.UserRepository;
import com.bdms.backend.security.JwtUtil;
import com.bdms.backend.security.UserPrincipal;
import com.bdms.backend.service.AuthService;
import com.bdms.backend.service.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;
    private final DonorRepository donorRepository;
    private final HospitalRepository hospitalRepository;
    private final AdminRepository adminRepository;
    private final NotificationRepository notificationRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("An account with this email already exists");
        }
        if (request.getRole() == null) {
            throw new BadRequestException("Role is required");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .isActive(true)
                .build();
        user = userRepository.save(user);

        switch (request.getRole()) {
            case DONOR -> {
                Donor donor = Donor.builder()
                        .user(user)
                        .age(request.getAge())
                        .gender(request.getGender())
                        .bloodGroup(request.getBloodGroup())
                        .mobile(request.getMobile())
                        .city(request.getCity())
                        .address(request.getAddress())
                        .availability(true)
                        .totalDonations(0)
                        .build();
                donorRepository.save(donor);
                emailService.sendDonorRegistrationConfirmation(user.getEmail(), user.getFullName());
                notifyAdminsOfNewDonor(user);
            }
            case HOSPITAL -> {
                Hospital hospital = Hospital.builder()
                        .user(user)
                        .hospitalName(request.getHospitalName())
                        .registrationNo(request.getRegistrationNo())
                        .email(user.getEmail())
                        .contactNo(request.getContactNo())
                        .city(request.getCity())
                        .address(request.getAddress())
                        .verified(false)
                        .build();
                hospitalRepository.save(hospital);
            }
            case ADMIN -> {
                Admin admin = Admin.builder()
                        .user(user)
                        .designation(request.getDesignation())
                        .build();
                adminRepository.save(admin);
            }
            default -> throw new BadRequestException("Unsupported role: " + request.getRole());
        }

        log.info("New {} account registered: {}", request.getRole(), user.getEmail());
        return buildAuthResponse(user);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (Exception e) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (Boolean.FALSE.equals(user.getIsActive())) {
            throw new InvalidCredentialsException("This account has been deactivated. Contact administrator.");
        }

        log.info("User logged in: {}", user.getEmail());
        return buildAuthResponse(user);
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String token = request.getRefreshToken();

        if (jwtUtil.isTokenExpired(token) || !jwtUtil.isRefreshToken(token)) {
            throw new InvalidTokenException("Refresh token is invalid or expired. Please log in again.");
        }

        String email = jwtUtil.extractEmail(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return buildAuthResponse(user);
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("No account found with this email"));

        String resetToken = jwtUtil.generatePasswordResetToken(user.getEmail());

        log.info("RESET TOKEN: {}", resetToken);

        emailService.sendPasswordResetEmail(user.getEmail(), resetToken);
        log.info("Password reset token issued for: {}", user.getEmail());
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
//        String token = request.getToken();
    	String token = request.getToken().replaceAll("\\s+", "");

        if (jwtUtil.isTokenExpired(token) || !jwtUtil.isResetToken(token)) {
            throw new InvalidTokenException("Reset token is invalid or expired. Please request a new one.");
        }

//        String email = jwtUtil.extractEmail(token);
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//
//        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
//        userRepository.save(user);
//        log.info("Password reset successful for: {}", email);
        
        String email = jwtUtil.extractEmail(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Prevent using the current password again
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new BadRequestException("New password cannot be the same as the current password.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        log.info("Password reset successful for: {}", email);
    }

    @Override
    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

//        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
//            throw new InvalidCredentialsException("Current password is incorrect");
//        }
//
//        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
//        userRepository.save(user);
//        log.info("Password changed for user id: {}", userId);
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Current password is incorrect");
        }

        // Prevent using the same password again
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new BadRequestException("New password cannot be the same as the current password.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        log.info("Password changed for user id: {}", userId);
        
    }

    private void notifyAdminsOfNewDonor(User newDonorUser) {
        // SRS 6.5.3: "The system shall notify administrators about newly registered donors."
        for (Admin admin : adminRepository.findAll()) {
            if (admin.getUser() == null) {
                continue;
            }
            Notification notification = Notification.builder()
                    .user(admin.getUser())
                    .title("New Donor Registered")
                    .message(newDonorUser.getFullName() + " (" + newDonorUser.getEmail() + ") has registered as a donor.")
                    .notificationType("NEW_DONOR_REGISTRATION")
                    .isRead(false)
                    .build();
            notificationRepository.save(notification);
        }
    }

    private AuthResponse buildAuthResponse(User user) {
        UserPrincipal principal = UserPrincipal.create(user);
        String accessToken = jwtUtil.generateAccessToken(principal);
        String refreshToken = jwtUtil.generateRefreshToken(principal);

        return AuthResponse.builder()
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .build();
    }
}
