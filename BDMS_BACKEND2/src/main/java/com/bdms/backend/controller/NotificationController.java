package com.bdms.backend.controller;

import com.bdms.backend.dto.request.NotificationCreateRequest;
import com.bdms.backend.dto.response.NotificationDto;
import com.bdms.backend.security.UserPrincipal;
import com.bdms.backend.service.NotificationService;
import com.bdms.backend.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification Management", description = "In-app notifications for donors, hospitals, and admins")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    @Operation(summary = "Create a notification for a specific user (admin/system use)")
    public ResponseEntity<ApiResponse<NotificationDto>> create(@Valid @RequestBody NotificationCreateRequest request) {
        NotificationDto dto = notificationService.createNotification(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Notification created successfully", dto));
    }

    @GetMapping("/me")
    @Operation(summary = "Get all notifications for the currently logged-in user")
    public ResponseEntity<ApiResponse<List<NotificationDto>>> getMyNotifications(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.success("Notifications fetched successfully",
                notificationService.getNotificationsForUser(principal.getUserId())));
    }

    @GetMapping("/me/unread")
    @Operation(summary = "Get unread notifications for the currently logged-in user")
    public ResponseEntity<ApiResponse<List<NotificationDto>>> getMyUnreadNotifications(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.success("Unread notifications fetched successfully",
                notificationService.getUnreadNotificationsForUser(principal.getUserId())));
    }

    @PatchMapping("/{notificationId}/read")
    @Operation(summary = "Mark a notification as read")
    public ResponseEntity<ApiResponse<NotificationDto>> markAsRead(@PathVariable Long notificationId) {
        return ResponseEntity.ok(ApiResponse.success("Notification marked as read", notificationService.markAsRead(notificationId)));
    }

    @DeleteMapping("/{notificationId}")
    @Operation(summary = "Delete a notification")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok(ApiResponse.success("Notification deleted successfully"));
    }
}
