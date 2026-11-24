package com.bdms.backend.repository;

import com.bdms.backend.entity.DonorResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DonorResponseRepository extends JpaRepository<DonorResponse, Long> {

    @Query("""
        SELECT dr
        FROM DonorResponse dr
        JOIN FETCH dr.donor d
        JOIN FETCH d.user
        JOIN FETCH dr.bloodRequest
    """)
    List<DonorResponse> findAll();

    @Query("""
        SELECT dr
        FROM DonorResponse dr
        JOIN FETCH dr.donor d
        JOIN FETCH d.user
        JOIN FETCH dr.bloodRequest
        WHERE dr.responseId = :responseId
    """)
    Optional<DonorResponse> findByIdWithRelations(@Param("responseId") Long responseId);

    @Query("""
        SELECT dr
        FROM DonorResponse dr
        JOIN FETCH dr.donor d
        JOIN FETCH d.user
        JOIN FETCH dr.bloodRequest
        WHERE d.donorId = :donorId
    """)
    List<DonorResponse> findByDonor_DonorId(@Param("donorId") Long donorId);

    @Query("""
        SELECT dr
        FROM DonorResponse dr
        JOIN FETCH dr.donor d
        JOIN FETCH d.user
        JOIN FETCH dr.bloodRequest
        WHERE dr.bloodRequest.requestId = :requestId
    """)
    List<DonorResponse> findByBloodRequest_RequestId(@Param("requestId") Long requestId);
}