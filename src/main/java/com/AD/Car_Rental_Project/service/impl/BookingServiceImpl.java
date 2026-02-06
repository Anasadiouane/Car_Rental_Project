package com.AD.Car_Rental_Project.service.impl;

import com.AD.Car_Rental_Project.domain.entity.Booking;
import com.AD.Car_Rental_Project.domain.entity.Car;
import com.AD.Car_Rental_Project.domain.entity.User;
import com.AD.Car_Rental_Project.domain.enumeration.BookingStatus;
import com.AD.Car_Rental_Project.domain.enumeration.RentalStatus;
import com.AD.Car_Rental_Project.repository.BookingRepository;
import com.AD.Car_Rental_Project.service.BookingService;
import com.AD.Car_Rental_Project.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final CarService carService;

    @Override
    public Booking createBooking(Booking booking) {
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setCreatedAt(LocalDate.now());

        Car car = carService.getCarById(booking.getCar().getId());
        car.setRentalStatus(RentalStatus.RENTED);

        return bookingRepository.save(booking);
    }

    @Override
    public Booking confirmBooking(Long bookingId, User user) {
        Booking booking = getBookingById(bookingId);
        booking.setBookingStatus(BookingStatus.CONFIRMED);
        booking.setConfirmedBy(user);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking finishBooking(Long bookingId) {
        Booking booking = getBookingById(bookingId);
        booking.setBookingStatus(BookingStatus.CONFIRMED);

        Car car = booking.getCar();
        car.setRentalStatus(RentalStatus.AVAILABLE);

        return bookingRepository.save(booking);
    }

    @Override
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public List<Booking> getBookingsByStatus(BookingStatus status) {
        return List.of();
    }

    @Override
    public Booking updateBookingStatus(Long id, BookingStatus status) {
        return null;
    }
}
