package com.bdms.backend.service;

import com.bdms.backend.dto.request.NotificationCreateRequest;
import com.bdms.backend.dto.response.NotificationDto;

import java.util.List;

public interface NotificationService {
    NotificationDto createNotification(NotificationCreateRequest request);
    List<NotificationDto> getNotificationsForUser(Long userId);
    List<NotificationDto> getUnreadNotificationsForUser(Long userId);
    NotificationDto markAsRead(Long notificationId);
    void deleteNotification(Long notificationId);
}
