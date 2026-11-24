package com.bdms.backend.repository;

import com.bdms.backend.entity.BloodRequest;
import com.bdms.backend.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BloodRequestRepository extends JpaRepository<BloodRequest, Long> {

    @Query("""
        SELECT br
        FROM BloodRequest br
        JOIN FETCH br.hospital
    """)
    List<BloodRequest> findAll();

    @Query("""
        SELECT br
        FROM BloodRequest br
        JOIN FETCH br.hospital
        WHERE br.requestId = :requestId
    """)
    Optional<BloodRequest> findByIdWithHospital(@Param("requestId") Long requestId);

    @Query("""
        SELECT br
        FROM BloodRequest br
        JOIN FETCH br.hospital
        WHERE br.hospital.hospitalId = :hospitalId
    """)
    List<BloodRequest> findByHospital_HospitalId(@Param("hospitalId") Long hospitalId);

    @Query("""
        SELECT br
        FROM BloodRequest br
        JOIN FETCH br.hospital
        WHERE br.requestStatus = :status
    """)
    List<BloodRequest> findByRequestStatus(@Param("status") RequestStatus status);

    @Query("""
        SELECT br
        FROM BloodRequest br
        JOIN FETCH br.hospital
        WHERE br.bloodGroup = :bloodGroup
        AND br.city = :city
    """)
    List<BloodRequest> findByBloodGroupAndCity(
            @Param("bloodGroup") String bloodGroup,
            @Param("city") String city
    );
}