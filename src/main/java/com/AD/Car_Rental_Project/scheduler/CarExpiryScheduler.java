package com.AD.Car_Rental_Project.scheduler;

import com.AD.Car_Rental_Project.domain.entity.Car;
import com.AD.Car_Rental_Project.repository.CarRepository;
import com.AD.Car_Rental_Project.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CarExpiryScheduler {

    private final CarRepository carRepository;
    private final NotificationService notificationService;

    // كل نهار مع 07:30
    @Scheduled(cron = "0 30 7 * * *")
    public void checkInsuranceAndVisitExpiry() {

        LocalDate today = LocalDate.now();
        LocalDate alertDate = today.plusDays(7);

        // Insurance
        List<Car> insuranceCars =
                carRepository.findByInsuranceExpiryDateBefore(alertDate);

        for (Car car : insuranceCars) {
            notificationService.createSystemNotification(
                    "Insurance expiring soon for car " + car.getPlateNumber()
            );
        }

        // Visit technique
        List<Car> visitCars =
                carRepository.findByVisitExpiryDateBefore(alertDate);

        for (Car car : visitCars) {
            notificationService.createSystemNotification(
                    "Technical visit expiring soon for car " + car.getPlateNumber()
            );
        }
    }
}
