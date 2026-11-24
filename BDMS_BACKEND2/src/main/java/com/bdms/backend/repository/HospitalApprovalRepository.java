package com.bdms.backend.repository;

import com.bdms.backend.entity.HospitalApproval;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HospitalApprovalRepository extends JpaRepository<HospitalApproval, Long> {
    List<HospitalApproval> findByHospital_HospitalId(Long hospitalId);
    Optional<HospitalApproval> findTopByHospital_HospitalIdOrderByApprovalIdDesc(Long hospitalId);
}
