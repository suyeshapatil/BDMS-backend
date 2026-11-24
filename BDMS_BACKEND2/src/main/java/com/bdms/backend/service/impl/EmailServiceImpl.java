package com.bdms.backend.service.impl;

import com.bdms.backend.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String fromAddress;

    @Override
    public void sendDonorRegistrationConfirmation(String toEmail, String fullName) {
        String subject = "Welcome to Blood Donation Management System";
        String body = "Dear " + fullName + ",\n\n"
                + "Thank you for registering as a blood donor with BDMS. "
                + "Your account has been created successfully.\n\n"
                + "Regards,\nBDMS Team";
        send(toEmail, subject, body);
    }

    @Override
    public void sendPasswordResetEmail(String toEmail, String resetToken) {
        String subject = "BDMS Password Reset Request";
        String body = "You requested a password reset.\n\n"
                + "Use the following token to reset your password (valid for 15 minutes):\n\n"
                + resetToken + "\n\n"
                + "If you did not request this, please ignore this email.\n\nRegards,\nBDMS Team";
        send(toEmail, subject, body);
    }

    @Override
    public void sendBloodRequestAlert(String toEmail, String donorName, String bloodGroup, String hospitalName, String city, String urgency) {
        String subject = "Urgent Blood Request - " + bloodGroup + " Needed";
        String body = "Dear " + donorName + ",\n\n"
                + hospitalName + " in " + city + " urgently requires " + bloodGroup + " blood.\n"
                + "Urgency Level: " + urgency + "\n\n"
                + "Please log in to BDMS to respond if you are available to donate.\n\nRegards,\nBDMS Team";
        send(toEmail, subject, body);
    }

    @Override
    public void sendHospitalApprovalStatus(String toEmail, String hospitalName, boolean approved) {
        String subject = "BDMS Hospital Registration " + (approved ? "Approved" : "Rejected");
        String body = "Dear " + hospitalName + ",\n\n"
                + "Your hospital registration on BDMS has been "
                + (approved ? "approved. You can now create blood requests."
                            : "rejected. Please contact the administrator for more details.")
                + "\n\nRegards,\nBDMS Team";
        send(toEmail, subject, body);
    }

    @Override
    public void sendThankYouForDonation(String toEmail, String donorName, String hospitalName, int unitsDonated) {
        String subject = "Thank You For Your Donation!";
        String body = "Dear " + donorName + ",\n\n"
                + "Thank you for donating " + unitsDonated + " unit(s) of blood at " + hospitalName + ". "
                + "Your generosity may help save a life.\n\n"
                + "We would love to hear about your experience - feel free to share feedback next time you log in to BDMS.\n\n"
                + "Regards,\nBDMS Team";
        send(toEmail, subject, body);
    }

    private void send(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromAddress);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        } catch (Exception e) {
            // Never let a mail failure break the calling business transaction.
            log.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }
}
