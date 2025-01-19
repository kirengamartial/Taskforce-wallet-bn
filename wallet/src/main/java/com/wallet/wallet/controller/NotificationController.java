package com.wallet.wallet.controller;

import com.wallet.wallet.dto.CategoryDTO;
import com.wallet.wallet.dto.NotificationDTO;
import com.wallet.wallet.entity.Notification;
import com.wallet.wallet.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationDTO>> getNotifications(@PathVariable Long userId) {
        List<Notification> notifications = notificationService.getNotificationsForUser(userId);
        List<NotificationDTO> notificationDTOs = notifications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(notificationDTOs);
    }

    private NotificationDTO convertToDTO(Notification notification) {
        return new NotificationDTO(
                notification.getId(),
                notification.getMessage(),
                notification.getTimestamp(),
                notification.getUser().getId()
        );
    }

    @PostMapping
    public ResponseEntity<String> createNotification(@RequestBody NotificationDTO notificationDTO) {
        notificationService.createNotification(notificationDTO.getUserId(), notificationDTO.getMessage());
        return new  ResponseEntity<>("New Notification",HttpStatus.CREATED);
    }
}
