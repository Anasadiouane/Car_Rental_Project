package com.AD.Car_Rental_Project.controller;

import com.AD.Car_Rental_Project.domain.entity.Notification;
import com.AD.Car_Rental_Project.domain.entity.User;
import com.AD.Car_Rental_Project.domain.enumeration.NotificationType;
import com.AD.Car_Rental_Project.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // ================= Get Notification by ID =================
    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable Long id) {
        return notificationService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= Get All Notifications =================
    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        return ResponseEntity.ok(notificationService.findAll());
    }

    // ================= Get Notifications for User =================
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getNotificationsForUser(@PathVariable Long userId) {
        User user = new User();
        user.setId(userId);
        return ResponseEntity.ok(notificationService.getNotificationsForUser(user));
    }

    // ================= Get Unread Notifications for User =================
    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@PathVariable Long userId) {
        User user = new User();
        user.setId(userId);
        return ResponseEntity.ok(notificationService.getUnreadNotifications(user));
    }

    // ================= Count Unread Notifications for User =================
    @GetMapping("/user/{userId}/unread/count")
    public ResponseEntity<Long> countUnreadNotifications(@PathVariable Long userId) {
        User user = new User();
        user.setId(userId);
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
    public ResponseEntity<List<Notification>> getNotificationsByType(@PathVariable NotificationType type) {
        return ResponseEntity.ok(notificationService.findByType(type));
    }

    // ================= Search by Date =================
    @GetMapping("/created-after")
    public ResponseEntity<List<Notification>> getNotificationsByCreatedAfter(@RequestParam LocalDateTime dateTime) {
        return ResponseEntity.ok(notificationService.findByCreatedAfter(dateTime));
    }

    // ================= Test WhatsApp Notification =================
    @PostMapping("/whatsapp")
    public ResponseEntity<Void> sendWhatsAppNotification(@RequestParam String phoneNumber,
                                                         @RequestParam String message) {
        notificationService.sendWhatsAppNotification(phoneNumber, message);
        return ResponseEntity.ok().build();
    }
}