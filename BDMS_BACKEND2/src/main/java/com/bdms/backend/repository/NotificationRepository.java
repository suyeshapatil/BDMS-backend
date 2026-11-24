package com.bdms.backend.repository;

import com.bdms.backend.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("""
        SELECT n
        FROM Notification n
        JOIN FETCH n.user
        ORDER BY n.createdAt DESC
    """)
    List<Notification> findAll();

    @Query("""
        SELECT n
        FROM Notification n
        JOIN FETCH n.user
        WHERE n.notificationId = :notificationId
    """)
    Optional<Notification> findByIdWithUser(@Param("notificationId") Long notificationId);

    @Query("""
        SELECT n
        FROM Notification n
        JOIN FETCH n.user
        WHERE n.user.userId = :userId
        ORDER BY n.createdAt DESC
    """)
    List<Notification> findByUser_UserIdOrderByCreatedAtDesc(@Param("userId") Long userId);

    @Query("""
        SELECT n
        FROM Notification n
        JOIN FETCH n.user
        WHERE n.user.userId = :userId
        AND n.isRead = false
        ORDER BY n.createdAt DESC
    """)
    List<Notification> findByUser_UserIdAndIsReadFalse(@Param("userId") Long userId);
}