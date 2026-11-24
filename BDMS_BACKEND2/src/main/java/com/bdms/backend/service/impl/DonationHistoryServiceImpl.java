package com.bdms.backend.service.impl;

import com.bdms.backend.dto.request.DonationHistoryCreateRequest;
import com.bdms.backend.dto.response.DonationHistoryDto;
import com.bdms.backend.entity.DonationHistory;
import com.bdms.backend.entity.Donor;
import com.bdms.backend.entity.Hospital;
import com.bdms.backend.entity.Notification;
import com.bdms.backend.exception.ResourceNotFoundException;
import com.bdms.backend.mapper.DonationHistoryMapper;
import com.bdms.backend.repository.DonationHistoryRepository;
import com.bdms.backend.repository.DonorRepository;
import com.bdms.backend.repository.HospitalRepository;
import com.bdms.backend.repository.NotificationRepository;
import com.bdms.backend.service.DonationHistoryService;
import com.bdms.backend.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DonationHistoryServiceImpl implements DonationHistoryService {

    private final DonationHistoryRepository donationHistoryRepository;
    private final DonorRepository donorRepository;
    private final HospitalRepository hospitalRepository;
    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    @Override
    @Transactional
    public DonationHistoryDto recordDonation(DonationHistoryCreateRequest request) {

        Donor donor = donorRepository.findById(request.getDonorId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Donor not found with id: " + request.getDonorId()));

        Hospital hospital = hospitalRepository.findByIdWithUser(request.getHospitalId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Hospital not found with id: " + request.getHospitalId()));

        DonationHistory history = DonationHistory.builder()
                .donor(donor)
                .hospital(hospital)
                .donationDate(request.getDonationDate())
                .unitsDonated(request.getUnitsDonated())
                .remarks(request.getRemarks())
                .build();

        history = donationHistoryRepository.save(history);

        donor.setLastDonationDate(request.getDonationDate());
        donor.setTotalDonations(
                (donor.getTotalDonations() == null ? 0 : donor.getTotalDonations()) + 1);

        donorRepository.save(donor);

        sendThankYou(donor, hospital, request.getUnitsDonated());

        return DonationHistoryMapper.toDto(history);
    }

    private void sendThankYou(Donor donor,
                              Hospital hospital,
                              int unitsDonated) {

        if (donor.getUser() == null) {
            return;
        }

        Notification notification = Notification.builder()
                .user(donor.getUser())
                .title("Thank You For Your Donation!")
                .message("Thank you for donating "
                        + unitsDonated
                        + " unit(s) of blood at "
                        + hospital.getHospitalName()
                        + ". Your generosity may help save a life.")
                .notificationType("THANK_YOU")
                .isRead(false)
                .build();

        notificationRepository.save(notification);

        emailService.sendThankYouForDonation(
                donor.getUser().getEmail(),
                donor.getUser().getFullName(),
                hospital.getHospitalName(),
                unitsDonated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DonationHistoryDto> getHistoryByDonor(Long donorId) {
        return donationHistoryRepository.findByDonor_DonorId(donorId)
                .stream()
                .map(DonationHistoryMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DonationHistoryDto> getHistoryByHospital(Long hospitalId) {
        return donationHistoryRepository.findByHospital_HospitalId(hospitalId)
                .stream()
                .map(DonationHistoryMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DonationHistoryDto> getAllHistory() {
        return donationHistoryRepository.findAll()
                .stream()
                .map(DonationHistoryMapper::toDto)
                .toList();
    }
}