package com.bdms.backend.service;

import com.bdms.backend.dto.response.DashboardStatsDto;

public interface DashboardService {
    DashboardStatsDto getStats();
}
