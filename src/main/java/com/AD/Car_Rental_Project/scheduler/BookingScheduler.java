package com.AD.Car_Rental_Project.scheduler;

import com.AD.Car_Rental_Project.domain.entity.Booking;
import com.AD.Car_Rental_Project.domain.enumeration.BookingStatus;
import com.AD.Car_Rental_Project.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingScheduler {

    private final BookingRepository bookingRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void closeExpiredBookings() {

        List<Booking> bookings =
                bookingRepository.findByEndDateBeforeAndBookingStatus(
                        LocalDate.now(),
                        BookingStatus.CONFIRMED
                );

        for (Booking booking : bookings) {
            booking.setBookingStatus(BookingStatus.CONFIRMED);
            bookingRepository.save(booking);
        }
    }
}
