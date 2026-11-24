package com.bdms.backend.dto.request;

import com.bdms.backend.enums.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.bdms.backend.validation.StrongPassword;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    @Size(max = 100)
    private String email;
    

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
//    private String password;
    @StrongPassword
    private String password;

    @NotNull(message = "Role is required")
    private Role role;

    // ---- optional donor-specific fields (used when role = DONOR) ----
    private Integer age;
    private String gender;
    private String bloodGroup;
    private String mobile;
    private String city;
    private String address;

    // ---- optional hospital-specific fields (used when role = HOSPITAL) ----
    private String hospitalName;
    private String registrationNo;
    private String contactNo;

    // ---- optional admin-specific field (used when role = ADMIN) ----
    private String designation;
}
