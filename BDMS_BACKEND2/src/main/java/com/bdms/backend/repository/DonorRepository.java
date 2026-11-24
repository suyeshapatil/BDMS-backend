package com.bdms.backend.repository;

import com.bdms.backend.entity.Donor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DonorRepository extends JpaRepository<Donor, Long> {

    @Query("""
        SELECT d
        FROM Donor d
        JOIN FETCH d.user
    """)
    List<Donor> findAll();

    @Query("""
        SELECT d
        FROM Donor d
        JOIN FETCH d.user
        WHERE d.user.userId = :userId
    """)
    Optional<Donor> findByUser_UserId(@Param("userId") Long userId);

    @Query("""
        SELECT d
        FROM Donor d
        JOIN FETCH d.user
        WHERE d.donorId = :donorId
    """)
    Optional<Donor> findByIdWithUser(@Param("donorId") Long donorId);

    @Query("""
    		SELECT d
    		FROM Donor d
    		JOIN FETCH d.user
    		WHERE d.city = :city
    		""")
    		List<Donor> findByCity(@Param("city") String city);
    
    @Query("""
    		SELECT d
    		FROM Donor d
    		JOIN FETCH d.user
    		WHERE d.bloodGroup = :bloodGroup
    		AND d.availability = true
    		""")
    		List<Donor> findByBloodGroupAndAvailabilityTrue(
    		        @Param("bloodGroup") String bloodGroup
    		);
    
    @Query("""
    		SELECT d
    		FROM Donor d
    		JOIN FETCH d.user
    		WHERE d.bloodGroup = :bloodGroup
    		AND d.city = :city
    		AND d.availability = true
    		""")
    		List<Donor> findByBloodGroupAndCityAndAvailabilityTrue(
    		        @Param("bloodGroup") String bloodGroup,
    		        @Param("city") String city
    		);
}