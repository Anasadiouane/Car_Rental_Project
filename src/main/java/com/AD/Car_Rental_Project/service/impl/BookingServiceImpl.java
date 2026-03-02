package com.AD.Car_Rental_Project.service.impl;

import com.AD.Car_Rental_Project.domain.dto.request.BookingRequestDTO;
import com.AD.Car_Rental_Project.domain.dto.response.BookingResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.*;
import com.AD.Car_Rental_Project.domain.enumeration.BookingStatus;
import com.AD.Car_Rental_Project.domain.enumeration.RentalStatus;
import com.AD.Car_Rental_Project.domain.mapper.BookingMapper;
import com.AD.Car_Rental_Project.repository.*;
import com.AD.Car_Rental_Project.service.BookingService;
import com.AD.Car_Rental_Project.service.ContractService;
import com.AD.Car_Rental_Project.service.NotificationService;
import com.itextpdf.text.DocumentException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final BookingMapper bookingMapper;
    private final NotificationService notificationService;
    private final ContractRepository contractRepository;
    private final ContractService contractService;

    @Override
    public BookingResponseDTO createBooking(BookingRequestDTO dto) {
        User customer = userRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        Car car = carRepository.findById(dto.getCarId())
                .orElseThrow(() -> new IllegalArgumentException("Car not found"));

        // Vérifier les réservations CONFIRMED pour cette voiture
        List<Booking> existingBookings = bookingRepository.findByCarIdAndBookingStatus(
                car.getId(),
                BookingStatus.CONFIRMED
        );

        for (Booking existing : existingBookings) {
            boolean overlap = !(dto.getEndDate().isBefore(existing.getStartDate())
                    || dto.getStartDate().isAfter(existing.getEndDate()));

            if (overlap) {
                throw new IllegalStateException("Car is already booked for the selected dates");
            }
        }

        // Créer la réservation si pas de chevauchement
        Booking booking = bookingMapper.toEntity(dto);
        booking.setCustomer(customer);
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setTotalPrice(BigDecimal.ZERO);

        car.addBooking(booking);

        bookingRepository.save(booking);
        carRepository.save(car);

        // Notification plus logique : "Booking Created"
        notificationService.sendBookingCreatedNotification(customer, booking);

        return bookingMapper.toResponseDto(booking);
    }

    @Override
    public BookingResponseDTO confirmBooking(Long bookingId, Long employeeId) throws FileNotFoundException, DocumentException {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        if (booking.getBookingStatus() != BookingStatus.PENDING) {
            throw new IllegalStateException("Only pending bookings can be confirmed");
        }

        long days = ChronoUnit.DAYS.between(booking.getStartDate(), booking.getEndDate());
        if (days <= 0) {
            throw new IllegalArgumentException("End date must be after start date");
        }

        BigDecimal totalPrice = booking.getCar().getPricePerDay()
                .multiply(BigDecimal.valueOf(days));
        booking.setTotalPrice(totalPrice);
        booking.setBookingStatus(BookingStatus.CONFIRMED);
        booking.setConfirmedBy(employee);

        booking.getCar().setRentalStatus(RentalStatus.AVAILABLE);
        bookingRepository.save(booking);


        contractService.addContract(bookingId);

        notificationService.sendBookingConfirmedNotification(booking.getCustomer(), booking);

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
    public List<BookingResponseDTO> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(bookingMapper::toResponseDto)
                .toList();
    }

    @Override
    public BookingResponseDTO returnCar(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        booking.setBookingStatus(BookingStatus.FINISHED);
        booking.getCar().setRentalStatus(RentalStatus.AVAILABLE);

        bookingRepository.save(booking);

        return bookingMapper.toResponseDto(booking);
    }

    @Override
    public List<BookingResponseDTO> getBookingsByStatus(BookingStatus status) {
        return bookingRepository.findByBookingStatus(status)
                .stream()
                .map(bookingMapper::toResponseDto)
                .toList();
    }
}