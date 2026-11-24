package com.bdms.backend.repository;

import com.bdms.backend.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {

    @Query("""
        SELECT h
        FROM Hospital h
        JOIN FETCH h.user
    """)
    List<Hospital> findAll();

    @Query("""
        SELECT h
        FROM Hospital h
        JOIN FETCH h.user
        WHERE h.hospitalId = :hospitalId
    """)
    Optional<Hospital> findByIdWithUser(@Param("hospitalId") Long hospitalId);

    @Query("""
        SELECT h
        FROM Hospital h
        JOIN FETCH h.user
        WHERE h.user.userId = :userId
    """)
    Optional<Hospital> findByUser_UserId(@Param("userId") Long userId);

    @Query("""
        SELECT h
        FROM Hospital h
        JOIN FETCH h.user
        WHERE h.verified = :verified
    """)
    List<Hospital> findByVerified(@Param("verified") Boolean verified);
}
