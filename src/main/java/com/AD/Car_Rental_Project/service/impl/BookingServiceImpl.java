package com.AD.Car_Rental_Project.service.impl;

import com.AD.Car_Rental_Project.domain.entity.Booking;
import com.AD.Car_Rental_Project.domain.entity.Car;
import com.AD.Car_Rental_Project.domain.entity.User;
import com.AD.Car_Rental_Project.domain.enumeration.BookingStatus;
import com.AD.Car_Rental_Project.domain.enumeration.NotificationType;
import com.AD.Car_Rental_Project.domain.enumeration.RelatedEntityType;
import com.AD.Car_Rental_Project.domain.enumeration.RentalStatus;
import com.AD.Car_Rental_Project.repository.BookingRepository;
import com.AD.Car_Rental_Project.service.BookingService;
import com.AD.Car_Rental_Project.service.CarService;
import com.AD.Car_Rental_Project.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final NotificationService notificationService;

    @Override
    public Booking createBooking(Booking booking) {
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setCreatedAt(LocalDate.now());
        return bookingRepository.save(booking);
    }

    @Override
    public Booking confirmBooking(Long bookingId, User confirmedBy) {
        return bookingRepository.findById(bookingId).map(booking -> {
            booking.setBookingStatus(BookingStatus.CONFIRMED);
            booking.setConfirmedBy(confirmedBy);

            // Internal notification for Admin/Employee
            notificationService.createNotification(
                    "Booking Confirmed",
                    "Booking #" + booking.getId() + " has been confirmed.",
                    NotificationType.BOOKING_END_SOON,
                    booking.getId(),
                    RelatedEntityType.BOOKING,
                    confirmedBy
            );

            return bookingRepository.save(booking);
        }).orElseThrow(() -> new RuntimeException("Booking not found"));
    }
    @Override
    public Booking cancelBooking(Long bookingId) {
        return bookingRepository.findById(bookingId).map(booking -> {
            booking.setBookingStatus(BookingStatus.CANCELLED);

            // Internal notification for Admin/Employee
            notificationService.createNotification(
                    "Booking Cancelled",
                    "Booking #" + booking.getId() + " has been cancelled.",
                    NotificationType.MAINTENANCE_ALERT, // could define a specific type later
                    booking.getId(),
                    RelatedEntityType.BOOKING,
                    null
            );

            return bookingRepository.save(booking);
        }).orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    @Override
    public List<Booking> getBookingsByCar(Car car) {
        return bookingRepository.findByCar(car);
    }

    @Override
    public List<Booking> getBookingsByCustomerCIN(String customerCIN) {
        return bookingRepository.findByCustomerCIN(customerCIN);
    }

    @Override
    public void checkBookingEndDates(LocalDate referenceDate) {
        // Find confirmed bookings ending before the reference date
        List<Booking> bookings = bookingRepository.findByEndDateBeforeAndBookingStatus(referenceDate, BookingStatus.CONFIRMED);

        for (Booking booking : bookings) {
            // External notification via WhatsApp to the customer
            notificationService.sendWhatsAppNotification(
                    booking.getCustomerPhone(),
                    "Dear " + booking.getCustomerName() +
                            ", your booking for car " + booking.getCar().getPlateNumber() +
                            " will end on " + booking.getEndDate() + ". Please prepare for return."
            );
        }
    }
}
