package com.bdms.backend.service;

public interface EmailService {
    void sendDonorRegistrationConfirmation(String toEmail, String fullName);
    void sendPasswordResetEmail(String toEmail, String resetToken);
    void sendBloodRequestAlert(String toEmail, String donorName, String bloodGroup, String hospitalName, String city, String urgency);
    void sendHospitalApprovalStatus(String toEmail, String hospitalName, boolean approved);
    void sendThankYouForDonation(String toEmail, String donorName, String hospitalName, int unitsDonated);
}
