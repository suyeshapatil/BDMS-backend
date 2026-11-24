package com.bdms.backend.service.impl;

import com.bdms.backend.dto.response.DashboardStatsDto;
import com.bdms.backend.enums.RequestStatus;
import com.bdms.backend.repository.BloodRequestRepository;
import com.bdms.backend.repository.DonationHistoryRepository;
import com.bdms.backend.repository.DonorRepository;
import com.bdms.backend.repository.HospitalRepository;
import com.bdms.backend.repository.UserRepository;
import com.bdms.backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final DonorRepository donorRepository;
    private final HospitalRepository hospitalRepository;
    private final BloodRequestRepository bloodRequestRepository;
    private final DonationHistoryRepository donationHistoryRepository;

    @Override
    public DashboardStatsDto getStats() {
        long totalUsers = userRepository.count();
        long totalDonors = donorRepository.count();
        long totalHospitals = hospitalRepository.count();
        long verifiedHospitals = hospitalRepository.findByVerified(true).size();
        long pendingHospitalApprovals = totalHospitals - verifiedHospitals;
        long totalBloodRequests = bloodRequestRepository.count();
        long openBloodRequests = bloodRequestRepository.findByRequestStatus(RequestStatus.OPEN).size();
        long completedBloodRequests = bloodRequestRepository.findByRequestStatus(RequestStatus.COMPLETED).size();
        long totalDonationsRecorded = donationHistoryRepository.count();

        return DashboardStatsDto.builder()
                .totalUsers(totalUsers)
                .totalDonors(totalDonors)
                .totalHospitals(totalHospitals)
                .verifiedHospitals(verifiedHospitals)
                .pendingHospitalApprovals(pendingHospitalApprovals)
                .totalBloodRequests(totalBloodRequests)
                .openBloodRequests(openBloodRequests)
                .completedBloodRequests(completedBloodRequests)
                .totalDonationsRecorded(totalDonationsRecorded)
                .build();
    }
}
