package com.AD.Car_Rental_Project.service.impl;

import com.AD.Car_Rental_Project.domain.dto.request.BookingRequestDTO;
import com.AD.Car_Rental_Project.domain.dto.response.BookingResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.*;
import com.AD.Car_Rental_Project.domain.enumeration.BookingStatus;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentStatus;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentType;
import com.AD.Car_Rental_Project.domain.enumeration.RentalStatus;
import com.AD.Car_Rental_Project.domain.mapper.BookingMapper;
import com.AD.Car_Rental_Project.repository.*;
import com.AD.Car_Rental_Project.service.BookingService;
import com.AD.Car_Rental_Project.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final BookingMapper bookingMapper;
    private final NotificationService notificationService;

    @Override
    public BookingResponseDTO createBooking(BookingRequestDTO dto) {
        User customer = userRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        Car car = carRepository.findById(dto.getCarId())
                .orElseThrow(() -> new IllegalArgumentException("Car not found"));

        Booking booking = bookingMapper.toEntity(dto);
        booking.setCustomer(customer);
        booking.setCar(car);
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setTotalPrice(BigDecimal.ZERO);

        bookingRepository.save(booking);

        notificationService.sendBookingEndSoonNotification(customer, booking);

        return bookingMapper.toResponseDto(booking);
    }

    @Override
    public BookingResponseDTO confirmBooking(Long bookingId, Long employeeId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        booking.setBookingStatus(BookingStatus.CONFIRMED);
        BigDecimal totalPrice = booking.getCar().getPricePerDay()
                .multiply(BigDecimal.valueOf(
                        ChronoUnit.DAYS.between(booking.getStartDate(), booking.getEndDate())));
        booking.setTotalPrice(totalPrice);

        booking.getCar().setRentalStatus(RentalStatus.RENTED);

        bookingRepository.save(booking);

        notificationService.sendBookingEndSoonNotification(booking.getCustomer(), booking);

        return bookingMapper.toResponseDto(booking);
    }

    @Override
    public BookingResponseDTO rejectBooking(Long bookingId, String reason, Long employeeId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        booking.setBookingStatus(BookingStatus.REJECTED);
        bookingRepository.save(booking);

        notificationService.sendBookingRejectedNotification(booking.getCustomer(), booking, reason);

        return bookingMapper.toResponseDto(booking);
    }

    @Override
    public BookingResponseDTO cancelBooking(Long bookingId, String reason, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        booking.setBookingStatus(BookingStatus.CANCELLED);
        booking.getCar().setRentalStatus(RentalStatus.AVAILABLE);

        bookingRepository.save(booking);

        notificationService.sendBookingCancelledNotification(booking.getCustomer(), booking, reason);

        return bookingMapper.toResponseDto(booking);
    }

    @Override
    public BookingResponseDTO finishBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        booking.setBookingStatus(BookingStatus.FINISHED);
        booking.getCar().setRentalStatus(RentalStatus.AVAILABLE);

        bookingRepository.save(booking);

        return bookingMapper.toResponseDto(booking);
    }

    @Override
    public List<BookingResponseDTO> getBookingsByCustomer(Long customerId) {
        return bookingRepository.findByCustomerId(customerId)
                .stream()
                .map(bookingMapper::toResponseDto)
                .toList();
    }

    @Override
    public List<BookingResponseDTO> getBookingsByStatus(BookingStatus status) {
        return bookingRepository.findByBookingStatus(status)
                .stream()
                .map(bookingMapper::toResponseDto)
                .toList();
    }
}