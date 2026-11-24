package com.bdms.backend.service.impl;

import com.bdms.backend.dto.request.BloodRequestCreateRequest;
import com.bdms.backend.dto.request.BloodRequestStatusUpdateRequest;
import com.bdms.backend.dto.response.BloodRequestDto;
import com.bdms.backend.entity.BloodRequest;
import com.bdms.backend.entity.Donor;
import com.bdms.backend.entity.Hospital;
import com.bdms.backend.entity.Notification;
import com.bdms.backend.enums.RequestStatus;
import com.bdms.backend.exception.BadRequestException;
import com.bdms.backend.exception.ResourceNotFoundException;
import com.bdms.backend.mapper.BloodRequestMapper;
import com.bdms.backend.repository.BloodRequestRepository;
import com.bdms.backend.repository.DonorRepository;
import com.bdms.backend.repository.HospitalRepository;
import com.bdms.backend.repository.NotificationRepository;
import com.bdms.backend.service.BloodRequestService;
import com.bdms.backend.service.EmailService;
import com.bdms.backend.util.DonorEligibilityUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BloodRequestServiceImpl implements BloodRequestService {

    private static final Logger log = LoggerFactory.getLogger(BloodRequestServiceImpl.class);

    private final BloodRequestRepository bloodRequestRepository;
    private final HospitalRepository hospitalRepository;
    private final DonorRepository donorRepository;
    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    @Override
    @Transactional
    public BloodRequestDto createRequest(Long hospitalId, BloodRequestCreateRequest request) {

        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Hospital not found with id: " + hospitalId));

        if (Boolean.FALSE.equals(hospital.getVerified())) {
            throw new BadRequestException(
                    "Hospital is not yet verified by admin and cannot create blood requests");
        }

        BloodRequest bloodRequest = BloodRequest.builder()
                .hospital(hospital)
                .patientName(request.getPatientName())
                .bloodGroup(request.getBloodGroup())
                .unitsRequired(request.getUnitsRequired())
                .city(request.getCity())
                .urgencyLevel(request.getUrgencyLevel())
                .requestStatus(RequestStatus.OPEN)
                .build();

        bloodRequest = bloodRequestRepository.save(bloodRequest);

        notifyMatchingDonors(bloodRequest, hospital);

        return BloodRequestMapper.toDto(bloodRequest);
    }

    private void notifyMatchingDonors(BloodRequest bloodRequest, Hospital hospital) {

        List<Donor> matchingDonors =
                donorRepository.findByBloodGroupAndCityAndAvailabilityTrue(
                        bloodRequest.getBloodGroup(),
                        bloodRequest.getCity());

        int skippedIneligible = 0;

        for (Donor donor : matchingDonors) {

            if (donor.getUser() == null) {
                continue;
            }

            if (!DonorEligibilityUtil.isEligible(donor)) {
                skippedIneligible++;
                continue;
            }

            Notification notification = Notification.builder()
                    .user(donor.getUser())
                    .title("Blood Request: " + bloodRequest.getBloodGroup() + " needed")
                    .message(
                            hospital.getHospitalName()
                                    + " in "
                                    + bloodRequest.getCity()
                                    + " needs "
                                    + bloodRequest.getUnitsRequired()
                                    + " unit(s) of "
                                    + bloodRequest.getBloodGroup()
                                    + " blood. Urgency: "
                                    + bloodRequest.getUrgencyLevel())
                    .notificationType("BLOOD_REQUEST_ALERT")
                    .isRead(false)
                    .build();

            notificationRepository.save(notification);

            emailService.sendBloodRequestAlert(
                    donor.getUser().getEmail(),
                    donor.getUser().getFullName(),
                    bloodRequest.getBloodGroup(),
                    hospital.getHospitalName(),
                    bloodRequest.getCity(),
                    bloodRequest.getUrgencyLevel().name());
        }

        log.info(
                "Notified {} matching donor(s) ({} skipped as not yet eligible) for blood request id {}",
                matchingDonors.size() - skippedIneligible,
                skippedIneligible,
                bloodRequest.getRequestId());
    }

    @Override
    @Transactional(readOnly = true)
    public BloodRequestDto getRequestById(Long requestId) {

        BloodRequest request = bloodRequestRepository.findByIdWithHospital(requestId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Blood request not found with id: " + requestId));

        return BloodRequestMapper.toDto(request);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BloodRequestDto> getAllRequests() {
        return bloodRequestRepository.findAll()
                .stream()
                .map(BloodRequestMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BloodRequestDto> getRequestsByHospital(Long hospitalId) {
        return bloodRequestRepository.findByHospital_HospitalId(hospitalId)
                .stream()
                .map(BloodRequestMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BloodRequestDto> getOpenRequestsForDonor(Long donorId) {

        Donor donor = donorRepository.findById(donorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Donor not found with id: " + donorId));

        return bloodRequestRepository.findByRequestStatus(RequestStatus.OPEN)
                .stream()
                .filter(request ->
                        request.getBloodGroup().equalsIgnoreCase(donor.getBloodGroup()))
                .map(BloodRequestMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public BloodRequestDto updateStatus(Long requestId,
                                        BloodRequestStatusUpdateRequest request) {

        BloodRequest bloodRequest = findRequest(requestId);

        bloodRequest.setRequestStatus(request.getRequestStatus());

        return BloodRequestMapper.toDto(
                bloodRequestRepository.save(bloodRequest));
    }

    @Override
    @Transactional
    public void deleteRequest(Long requestId) {

        if (!bloodRequestRepository.existsById(requestId)) {
            throw new ResourceNotFoundException(
                    "Blood request not found with id: " + requestId);
        }

        bloodRequestRepository.deleteById(requestId);
    }

    private BloodRequest findRequest(Long requestId) {

        return bloodRequestRepository.findByIdWithHospital(requestId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Blood request not found with id: " + requestId));
    }
}