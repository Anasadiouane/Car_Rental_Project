package com.AD.Car_Rental_Project.repository;

import com.AD.Car_Rental_Project.domain.entity.Booking;
import com.AD.Car_Rental_Project.domain.entity.Car;
import com.AD.Car_Rental_Project.domain.entity.User;
import com.AD.Car_Rental_Project.domain.enumeration.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {

    List<Booking> findByBookingStatus(BookingStatus bookingStatus);

    List<Booking> findByCar(Car car);

    List<Booking> findByConfirmedBy(User user);

    List<Booking> findByCustomerCIN(String customerCIN);

    List<Booking> findByStartDateBetween(LocalDate start, LocalDate end);

    List<Booking> findByEndDateBeforeAndBookingStatus(LocalDate date, BookingStatus status);

}
