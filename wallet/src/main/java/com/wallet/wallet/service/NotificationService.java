package com.wallet.wallet.service;

import com.wallet.wallet.entity.Notification;
import com.wallet.wallet.exception.ResourceNotFoundException;
import com.wallet.wallet.repository.NotificationRepository;
import com.wallet.wallet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    // Method to create a notification
    public void createNotification(Long userId, String message) {
        Notification notification = new Notification();

        notification.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found")));

        notification.setMessage(message);
        notification.setTimestamp(LocalDateTime.now());

        notificationRepository.save(notification);
    }

    // Method to get notifications for a user
    public List<Notification> getNotificationsForUser(Long userId) {
        return notificationRepository.findByUserId(userId);
    }
}
