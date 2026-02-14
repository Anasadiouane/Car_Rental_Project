package com.AD.Car_Rental_Project.controller;

import com.AD.Car_Rental_Project.domain.dto.response.NotificationResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.Notification;
import com.AD.Car_Rental_Project.domain.entity.User;
import com.AD.Car_Rental_Project.domain.enumeration.NotificationType;

import com.AD.Car_Rental_Project.service.NotificationService;
import com.AD.Car_Rental_Project.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public NotificationController(NotificationService notificationService, UserRepository userRepository) {
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }

    // ================= Get Notification by ID =================
    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponseDTO> getNotificationById(@PathVariable Long id) {
        return notificationService.findById(id)
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= Get All Notifications =================
    @GetMapping
    public ResponseEntity<List<NotificationResponseDTO>> getAllNotifications() {
        return ResponseEntity.ok(
                notificationService.findAll().stream()
                        .map(this::toDto)
                        .collect(Collectors.toList())
        );
    }

    // ================= Get Notifications for User =================
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponseDTO>> getNotificationsForUser(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return ResponseEntity.ok(
                notificationService.getNotificationsForUser(user).stream()
                        .map(this::toDto)
                        .collect(Collectors.toList())
        );
    }

    // ================= Get Unread Notifications for User =================
    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<NotificationResponseDTO>> getUnreadNotifications(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return ResponseEntity.ok(
                notificationService.getUnreadNotifications(user).stream()
                        .map(this::toDto)
                        .collect(Collectors.toList())
        );
    }

    // ================= Count Unread Notifications for User =================
    @GetMapping("/user/{userId}/unread/count")
    public ResponseEntity<Long> countUnreadNotifications(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return ResponseEntity.ok(notificationService.countUnreadNotifications(user));
    }

    // ================= Mark Notification as Seen =================
    @PutMapping("/{id}/seen")
    public ResponseEntity<Void> markAsSeen(@PathVariable Long id) {
        notificationService.markAsSeen(id);
        return ResponseEntity.noContent().build();
    }

    // ================= Delete Notification =================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

    // ================= Search by Type =================
    @GetMapping("/type/{type}")
    public ResponseEntity<List<NotificationResponseDTO>> getNotificationsByType(@PathVariable NotificationType type) {
        return ResponseEntity.ok(
                notificationService.findByType(type).stream()
                        .map(this::toDto)
                        .collect(Collectors.toList())
        );
    }

    // ================= Search by Date =================
    @GetMapping("/created-after")
    public ResponseEntity<List<NotificationResponseDTO>> getNotificationsByCreatedAfter(@RequestParam LocalDateTime dateTime) {
        return ResponseEntity.ok(
                notificationService.findByCreatedAfter(dateTime).stream()
                        .map(this::toDto)
                        .collect(Collectors.toList())
        );
    }

    // ================= Test WhatsApp Notification =================
    @PostMapping("/whatsapp")
    public ResponseEntity<Void> sendWhatsAppNotification(@RequestParam String phoneNumber,
                                                         @RequestParam String message) {
        notificationService.sendWhatsAppNotification(phoneNumber, message);
        return ResponseEntity.ok().build();
    }

    // ================= Mapping utilitaire =================
    private NotificationResponseDTO toDto(Notification notification) {
        NotificationResponseDTO dto = new NotificationResponseDTO();
        dto.setId(notification.getId());
        dto.setTitle(notification.getTitle());
        dto.setMessage(notification.getMessage());
        dto.setNotificationType(notification.getNotificationType());
        dto.setRelatedEntityType(notification.getRelatedEntityType());
        dto.setRelatedEntityId(notification.getRelatedEntityId());
        dto.setSeen(notification.isSeen());
        dto.setCreatedAt(notification.getCreatedAt());

        if (notification.getUser() != null) {
            dto.setUserFullName(notification.getUser().getFullName());
            dto.setUserEmail(notification.getUser().getEmail());
        }

        return dto;
    }
}