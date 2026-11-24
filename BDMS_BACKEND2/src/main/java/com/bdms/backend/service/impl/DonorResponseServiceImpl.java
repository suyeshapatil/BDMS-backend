package com.bdms.backend.service.impl;

import com.bdms.backend.dto.request.DonorResponseCreateRequest;
import com.bdms.backend.dto.request.DonorResponseStatusUpdateRequest;
import com.bdms.backend.dto.response.DonorResponseRecordDto;
import com.bdms.backend.entity.BloodRequest;
import com.bdms.backend.entity.Donor;
import com.bdms.backend.entity.DonorResponse;
import com.bdms.backend.entity.Notification;
import com.bdms.backend.enums.RequestStatus;
import com.bdms.backend.enums.ResponseStatus;
import com.bdms.backend.exception.DuplicateResourceException;
import com.bdms.backend.exception.ResourceNotFoundException;
import com.bdms.backend.mapper.DonorResponseRecordMapper;
import com.bdms.backend.repository.BloodRequestRepository;
import com.bdms.backend.repository.DonorRepository;
import com.bdms.backend.repository.DonorResponseRepository;
import com.bdms.backend.repository.NotificationRepository;
import com.bdms.backend.service.DonorResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DonorResponseServiceImpl implements DonorResponseService {

    private final DonorResponseRepository donorResponseRepository;
    private final DonorRepository donorRepository;
    private final BloodRequestRepository bloodRequestRepository;
    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public DonorResponseRecordDto respondToRequest(
            Long donorId,
            DonorResponseCreateRequest request,
            ResponseStatus status) {

        Donor donor = donorRepository.findById(donorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Donor not found with id: " + donorId));

        BloodRequest bloodRequest = bloodRequestRepository.findByIdWithHospital(request.getRequestId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Blood request not found with id: " + request.getRequestId()));

        boolean alreadyResponded = donorResponseRepository
                .findByBloodRequest_RequestId(bloodRequest.getRequestId())
                .stream()
                .anyMatch(response ->
                        response.getDonor().getDonorId().equals(donorId));

        if (alreadyResponded) {
            throw new DuplicateResourceException(
                    "You have already responded to this blood request");
        }

        DonorResponse response = DonorResponse.builder()
                .bloodRequest(bloodRequest)
                .donor(donor)
                .responseStatus(status)
                .build();

        response = donorResponseRepository.save(response);

        if (status == ResponseStatus.ACCEPTED) {

            bloodRequest.setRequestStatus(RequestStatus.PENDING);
            bloodRequestRepository.save(bloodRequest);

            if (bloodRequest.getHospital() != null &&
                    bloodRequest.getHospital().getUser() != null) {

                Notification notification = Notification.builder()
                        .user(bloodRequest.getHospital().getUser())
                        .title("Donor Accepted Your Blood Request")
                        .message(
                                donor.getUser().getFullName()
                                        + " has accepted your request for "
                                        + bloodRequest.getBloodGroup()
                                        + " blood.")
                        .notificationType("DONOR_RESPONSE")
                        .isRead(false)
                        .build();

                notificationRepository.save(notification);
            }
        }

        return DonorResponseRecordMapper.toDto(response);
    }

    @Override
    @Transactional
    public DonorResponseRecordDto updateResponseStatus(
            Long responseId,
            DonorResponseStatusUpdateRequest request) {

        DonorResponse response = donorResponseRepository.findByIdWithRelations(responseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Donor response not found with id: " + responseId));

        response.setResponseStatus(request.getResponseStatus());

        return DonorResponseRecordMapper.toDto(
                donorResponseRepository.save(response));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DonorResponseRecordDto> getResponsesByDonor(Long donorId) {

        return donorResponseRepository.findByDonor_DonorId(donorId)
                .stream()
                .map(DonorResponseRecordMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DonorResponseRecordDto> getResponsesByRequest(Long requestId) {

        return donorResponseRepository.findByBloodRequest_RequestId(requestId)
                .stream()
                .map(DonorResponseRecordMapper::toDto)
                .toList();
    }
}