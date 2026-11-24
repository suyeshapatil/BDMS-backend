package com.bdms.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatsDto {
    private long totalUsers;
    private long totalDonors;
    private long totalHospitals;
    private long verifiedHospitals;
    private long pendingHospitalApprovals;
    private long totalBloodRequests;
    private long openBloodRequests;
    private long completedBloodRequests;
    private long totalDonationsRecorded;
}
