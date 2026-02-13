package com.AD.Car_Rental_Project.service.impl;

import com.AD.Car_Rental_Project.domain.entity.*;
import com.AD.Car_Rental_Project.domain.enumeration.*;
import com.AD.Car_Rental_Project.repository.NotificationRepository;
import com.AD.Car_Rental_Project.repository.UserRepository;
import com.AD.Car_Rental_Project.repository.BookingRepository;
import com.AD.Car_Rental_Project.repository.CarRepository;
import com.AD.Car_Rental_Project.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CarRepository carRepository;

    // ====== Core Operations ======
    @Override
    public void createNotification(String title,
                                   String message,
                                   NotificationType type,
                                   Long relatedEntityId,
                                   RelatedEntityType relatedEntityType,
                                   User user) {
        Notification notification = Notification.builder()
                .title(title)
                .message(message)
                .notificationType(type)
                .relatedEntityId(relatedEntityId)
                .relatedEntityType(relatedEntityType)
                .user(user)
                .seen(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
    }

    @Override
    public void markAsSeen(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setSeen(true);
            notificationRepository.save(notification);
        });
    }

    @Override
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return notificationRepository.findById(id);
    }

    @Override
    public List<Notification> findAll() {
        return notificationRepository.findAll();
    }

    // ====== User-specific Methods ======
    @Override
    public List<Notification> getNotificationsForUser(User user) {
        return notificationRepository.findByUserOrderByCreatedAt(user);
    }

    @Override
    public List<Notification> getUnreadNotifications(User user) {
        return notificationRepository.findByUserAndSeenFalse(user, false);
    }

    @Override
    public long countUnreadNotifications(User user) {
        return notificationRepository.countByUserAndSeenFalse(user);
    }
    @Override
    public List<Notification> findByType(NotificationType type) {
        return notificationRepository.findByNotificationType(type);
    }

    @Override
    public List<Notification> findByCreatedAfter(LocalDateTime dateTime) {
        return notificationRepository.findByCreatedAtAfter(dateTime);
    }


    // ====== Business-specific Methods ======
    @Override
    public void notifyAdminsAndEmployeesAboutMaintenance(Maintenance maintenance) {
        List<User> staff = userRepository.findByRoleIn(List.of(Role.ADMIN, Role.EMPLOYEE));
        for (User user : staff) {
            createNotification(
                    "Maintenance Alert",
                    "Maintenance scheduled for car " + maintenance.getCar().getPlateNumber(),
                    NotificationType.MAINTENANCE_ALERT,
                    maintenance.getId(),
                    RelatedEntityType.CAR,
                    user
            );
        }
    }

    @Override
    public void notifyAdminsAndEmployeesAboutCarStatus(Car car) {
        List<User> staff = userRepository.findByRoleIn(List.of(Role.ADMIN, Role.EMPLOYEE));

        NotificationType type = null;
        String message = null;

        switch (car.getTechnicalStatus()) {
            case OIL_CHANGE_REQUIRED -> {
                type = NotificationType.OIL_CHANGE_EXPIRED;
                message = "Vidange requise pour la voiture " + car.getPlateNumber();
            }
            case VISIT_REQUIRED -> {
                type = NotificationType.VISIT_EXPIRED;
                message = "Visite technique expirée pour la voiture " + car.getPlateNumber();
            }
            case INSURANCE_EXPIRED -> {
                type = NotificationType.INSURANCE_EXPIRED;
                message = "Assurance expirée pour la voiture " + car.getPlateNumber();
            }
            case MAINTENANCE_EXPIRED -> {
                type = NotificationType.MAINTENANCE_ALERT;
                message = "Maintenance expirée pour la voiture " + car.getPlateNumber();
            }
        }

        if (type != null) {
            for (User user : staff) {
                createNotification("Car Status Alert", message, type, car.getId(), RelatedEntityType.CAR, user);
            }
        }
    }

    @Override
    public void notifyCustomerIfBookingEndingSoon(Booking booking) {
        LocalDate today = LocalDate.now();
        if (booking.getEndDate().minusDays(2).isEqual(today)) {
            sendWhatsAppNotification(
                    booking.getCustomerPhone(),
                    "Cher client " + booking.getCustomerName() +
                            ", votre location de la voiture " + booking.getCar().getPlateNumber() +
                            " se termine bientôt le " + booking.getEndDate() +
                            ". Merci de préparer le retour."
            );
        }
    }

    // ====== Scheduler quotidien ======
    @Scheduled(cron = "0 0 8 * * ?") // Tous les jours à 08h00
    public void checkSystemNotifications() {
        // Vérifier les voitures
        List<Car> cars = carRepository.findAll();
        for (Car car : cars) {
            notifyAdminsAndEmployeesAboutCarStatus(car);
        }

        // Vérifier les réservations
        List<Booking> bookings = bookingRepository.findAll();
        for (Booking booking : bookings) {
            notifyCustomerIfBookingEndingSoon(booking);
        }
    }

    // ====== External Integration ======
    @Override
    public void sendWhatsAppNotification(String phoneNumber, String message) {
        // ⚠️ Ici tu intègres Twilio ou WhatsApp Business API
        System.out.println("WhatsApp message sent to " + phoneNumber + " : " + message);
    }
}