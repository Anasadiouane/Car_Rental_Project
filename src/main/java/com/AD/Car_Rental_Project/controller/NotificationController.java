package com.AD.Car_Rental_Project.controller;

import com.AD.Car_Rental_Project.domain.entity.Notification;
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
    public List<Notification> getUserNotifications(@PathVariable Long userId) {
        return notificationService.getUserNotifications(userId);
    }

    @GetMapping("/user/{userId}/unseen")
    public List<Notification> getUnseen(@PathVariable Long userId) {
        return notificationService.getUnseenNotifications(userId);
    }

    @PutMapping("/{id}/seen")
    public void markAsSeen(@PathVariable Long id) {
        notificationService.markAsSeen(id);
    }
}

