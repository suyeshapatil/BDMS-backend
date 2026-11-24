package com.bdms.backend.service.impl;

import com.bdms.backend.dto.request.NotificationCreateRequest;
import com.bdms.backend.dto.response.NotificationDto;
import com.bdms.backend.entity.Notification;
import com.bdms.backend.entity.User;
import com.bdms.backend.exception.ResourceNotFoundException;
import com.bdms.backend.mapper.NotificationMapper;
import com.bdms.backend.repository.NotificationRepository;
import com.bdms.backend.repository.UserRepository;
import com.bdms.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public NotificationDto createNotification(NotificationCreateRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        Notification notification = Notification.builder()
                .user(user)
                .title(request.getTitle())
                .message(request.getMessage())
                .notificationType(request.getNotificationType())
                .isRead(false)
                .build();

        return NotificationMapper.toDto(notificationRepository.save(notification));
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDto> getNotificationsForUser(Long userId) {

        return notificationRepository.findByUser_UserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(NotificationMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDto> getUnreadNotificationsForUser(Long userId) {

        return notificationRepository.findByUser_UserIdAndIsReadFalse(userId)
                .stream()
                .map(NotificationMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public NotificationDto markAsRead(Long notificationId) {

        Notification notification = notificationRepository.findByIdWithUser(notificationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Notification not found with id: " + notificationId));

        notification.setIsRead(true);

        return NotificationMapper.toDto(notificationRepository.save(notification));
    }

    @Override
    @Transactional
    public void deleteNotification(Long notificationId) {

        if (!notificationRepository.existsById(notificationId)) {
            throw new ResourceNotFoundException(
                    "Notification not found with id: " + notificationId);
        }

        notificationRepository.deleteById(notificationId);
    }
}