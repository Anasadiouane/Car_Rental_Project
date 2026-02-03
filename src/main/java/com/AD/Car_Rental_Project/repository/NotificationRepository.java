package com.AD.Car_Rental_Project.repository;

import com.AD.Car_Rental_Project.domain.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification,Long> {

}
