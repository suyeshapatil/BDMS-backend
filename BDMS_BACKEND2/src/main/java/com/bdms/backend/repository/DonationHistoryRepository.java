package com.bdms.backend.repository;

import com.bdms.backend.entity.DonationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DonationHistoryRepository extends JpaRepository<DonationHistory, Long> {

    @Query("""
        SELECT dh
        FROM DonationHistory dh
        JOIN FETCH dh.donor d
        JOIN FETCH d.user
        JOIN FETCH dh.hospital
    """)
    List<DonationHistory> findAll();

    @Query("""
        SELECT dh
        FROM DonationHistory dh
        JOIN FETCH dh.donor d
        JOIN FETCH d.user
        JOIN FETCH dh.hospital
        WHERE dh.historyId = :historyId
    """)
    Optional<DonationHistory> findByIdWithRelations(@Param("historyId") Long historyId);

    @Query("""
        SELECT dh
        FROM DonationHistory dh
        JOIN FETCH dh.donor d
        JOIN FETCH d.user
        JOIN FETCH dh.hospital
        WHERE d.donorId = :donorId
    """)
    List<DonationHistory> findByDonor_DonorId(@Param("donorId") Long donorId);

    @Query("""
        SELECT dh
        FROM DonationHistory dh
        JOIN FETCH dh.donor d
        JOIN FETCH d.user
        JOIN FETCH dh.hospital
        WHERE dh.hospital.hospitalId = :hospitalId
    """)
    List<DonationHistory> findByHospital_HospitalId(@Param("hospitalId") Long hospitalId);
}