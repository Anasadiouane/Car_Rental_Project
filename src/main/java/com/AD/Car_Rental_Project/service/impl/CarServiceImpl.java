package com.AD.Car_Rental_Project.service.impl;

import com.AD.Car_Rental_Project.domain.entity.Car;
import com.AD.Car_Rental_Project.domain.enumeration.NotificationType;
import com.AD.Car_Rental_Project.domain.enumeration.RelatedEntityType;
import com.AD.Car_Rental_Project.domain.enumeration.RentalStatus;
import com.AD.Car_Rental_Project.domain.enumeration.TechnicalStatus;
import com.AD.Car_Rental_Project.repository.CarRepository;
import com.AD.Car_Rental_Project.service.CarService;
import com.AD.Car_Rental_Project.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final NotificationService notificationService;

    @Override
    public Car createCar(Car car) {
        return carRepository.save(car);
    }

    @Override
    public Car updateCar(Long carId, Car car) {
        return carRepository.findById(carId).map(existingCar -> {
            existingCar.setBrand(car.getBrand());
            existingCar.setModel(car.getModel());
            existingCar.setYear(car.getYear());
            existingCar.setPlateNumber(car.getPlateNumber());
            existingCar.setPricePerDay(car.getPricePerDay());
            existingCar.setMileage(car.getMileage());
            existingCar.setRentalStatus(car.getRentalStatus());
            existingCar.setTechnicalStatus(car.getTechnicalStatus());
            existingCar.setInsuranceExpiryDate(car.getInsuranceExpiryDate());
            existingCar.setVisitExpiryDate(car.getVisitExpiryDate());
            existingCar.setLastOilChangeDate(car.getLastOilChangeDate());
            existingCar.setLastOilChangeMileage(car.getLastOilChangeMileage());
            return carRepository.save(existingCar);
        }).orElseThrow(() -> new RuntimeException("Car not found"));
    }

    @Override
    public Car getCarById(Long carId) {
        return carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found"));
    }

    @Override
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    @Override
    public void checkCarInsuranceExpiry() {
        LocalDate today = LocalDate.now();
        List<Car> cars = carRepository.findByInsuranceExpiryDateBefore(today);

        for (Car car : cars) {
            notificationService.createNotification(
                    "Insurance expired",
                    "Car " + car.getPlateNumber() + " has expired insurance.",
                    NotificationType.INSURANCE_EXPIRED,
                    car.getId(),
                    RelatedEntityType.CAR,
                    null
            );
        }
    }

    @Override
    public void checkCarVisitExpiry() {
        LocalDate today = LocalDate.now();
        List<Car> cars = carRepository.findByVisitExpiryDateBefore(today);

        for (Car car : cars) {
            notificationService.createNotification(
                    "Technical visit expired",
                    "Car " + car.getPlateNumber() + " has expired technical visit.",
                    NotificationType.VISIT_EXPIRED,
                    car.getId(),
                    RelatedEntityType.CAR,
                    null
            );
        }
    }

    @Override
    public void checkCarOilChange() {
        LocalDate today = LocalDate.now();
        List<Car> cars = carRepository.findAll();

        for (Car car : cars) {
            boolean overdueByDate = car.getLastOilChangeDate() != null &&
                    car.getLastOilChangeDate().plusMonths(6).isBefore(today);

            boolean overdueByMileage = car.getLastOilChangeMileage() > 0 &&
                    (car.getMileage() - car.getLastOilChangeMileage()) > 10000;

            if (overdueByDate || overdueByMileage) {
                notificationService.createNotification(
                        "Oil change required",
                        "Car " + car.getPlateNumber() + " needs an oil change.",
                        NotificationType.OIL_CHANGE_EXPIRED,
                        car.getId(),
                        RelatedEntityType.CAR,
                        null
                );
            }
        }
    }
}
