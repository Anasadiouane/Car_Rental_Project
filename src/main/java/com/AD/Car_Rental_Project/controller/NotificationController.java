package com.AD.Car_Rental_Project.controller;

import com.AD.Car_Rental_Project.domain.entity.Notification;
import com.AD.Car_Rental_Project.domain.entity.User;
import com.AD.Car_Rental_Project.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/user/{userId}")
    public List<Notification> getNotificationsForUser(@PathVariable User userId) {
        return notificationService.getNotificationsForUser(userId);
    }

    @GetMapping("/user/{userId}/unread")
    public List<Notification> getUnreadNotifications(@PathVariable User userId) {
        return notificationService.getUnreadNotifications(userId);
    }

    @GetMapping("/user/{userId}/count-unread")
    public long countUnreadNotifications(@PathVariable User userId) {
        return notificationService.countUnreadNotifications(userId);
    }

    @PutMapping("/{id}/seen")
    public void markAsSeen(@PathVariable Long id) {
        notificationService.markAsSeen(id);
    }
}
