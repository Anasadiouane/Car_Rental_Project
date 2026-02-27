package com.AD.Car_Rental_Project.scheduler;

import com.AD.Car_Rental_Project.domain.entity.Booking;
import com.AD.Car_Rental_Project.domain.entity.Car;
import com.AD.Car_Rental_Project.domain.entity.User;
import com.AD.Car_Rental_Project.domain.enumeration.BookingStatus;
import com.AD.Car_Rental_Project.domain.enumeration.TechnicalStatus;
import com.AD.Car_Rental_Project.repository.BookingRepository;
import com.AD.Car_Rental_Project.repository.CarRepository;
import com.AD.Car_Rental_Project.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private final BookingRepository bookingRepository;
    private final CarRepository carRepository;
    private final NotificationService notificationService;

    // Vérifier les réservations qui finissent demain
    @Scheduled(cron = "0 0 0 * * ?")
    public void checkExpiringBookings() {
        LocalDate today = LocalDate.now();
        List<Booking> bookings = bookingRepository.findByBookingStatus(BookingStatus.CONFIRMED);

        for (Booking booking : bookings) {
            if (booking.getEndDate().minusDays(1).isEqual(today)) {
                notificationService.sendBookingEndSoonNotification(booking.getCustomer(), booking);
            }
        }
    }

    // Vérifier les maintenances liées aux voitures
    @Scheduled(cron = "0 0 0 * * ?")
    public void checkCarMaintenance() {
        LocalDate today = LocalDate.now();
        List<Car> cars = carRepository.findAll();

        for (Car car : cars) {
            // On récupère le dernier booking actif (si existant)
            Booking activeBooking = car.getBookings().stream()
                    .filter(b -> b.getBookingStatus() == BookingStatus.CONFIRMED) // ou ton statut actif
                    .findFirst()
                    .orElse(null);

            User targetUser = (activeBooking != null) ? activeBooking.getCustomer() : null;

            if (car.getVisitExpiryDate() != null && car.getVisitExpiryDate().isBefore(today)) {
                notificationService.sendVisitExpiredNotification(car, targetUser);
            }
            if (car.getInsuranceExpiryDate() != null && car.getInsuranceExpiryDate().isBefore(today)) {
                notificationService.sendInsuranceExpiredNotification(car, targetUser);
            }
            if (car.getLastOilChangeDate() != null && car.getLastOilChangeDate().plusMonths(6).isBefore(today)) {
                notificationService.sendOilChangeExpiredNotification(car, targetUser);
            }
            if (car.getTechnicalStatus() == TechnicalStatus.MAINTENANCE_EXPIRED) {
                notificationService.sendMaintenanceAlertNotification(car, targetUser);
            }
        }
    }
}