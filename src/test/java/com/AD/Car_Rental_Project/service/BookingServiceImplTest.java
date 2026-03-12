package com.AD.Car_Rental_Project.service;

import com.AD.Car_Rental_Project.domain.dto.request.BookingRequestDTO;
import com.AD.Car_Rental_Project.domain.dto.response.BookingResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.Booking;
import com.AD.Car_Rental_Project.domain.entity.Car;
import com.AD.Car_Rental_Project.domain.entity.User;
import com.AD.Car_Rental_Project.domain.enumeration.BookingStatus;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentStatus;
import com.AD.Car_Rental_Project.domain.mapper.BookingMapper;
import com.AD.Car_Rental_Project.repository.BookingRepository;
import com.AD.Car_Rental_Project.repository.CarRepository;
import com.AD.Car_Rental_Project.repository.UserRepository;
import com.AD.Car_Rental_Project.service.impl.BookingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock private UserRepository userRepository;
    @Mock private CarRepository carRepository;
    @Mock private BookingMapper bookingMapper;
    @Mock private NotificationService notificationService;
    @Mock private ContractService contractService;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void testCreateBooking_shouldCreatePendingBooking() {
        BookingRequestDTO dto = BookingRequestDTO.builder()
                .customerId(1L)
                .carId(2L)
                .startDate(LocalDate.of(2026, 3, 12))
                .endDate(LocalDate.of(2026, 3, 15))
                .build();

        User customer = new User(); customer.setId(1L);
        Car car = new Car(); car.setId(2L); car.setPricePerDay(BigDecimal.valueOf(100));

        when(userRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(carRepository.findById(2L)).thenReturn(Optional.of(car));
        when(bookingRepository.findByCarIdAndBookingStatus(2L, BookingStatus.CONFIRMED))
                .thenReturn(List.of());
        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setCar(car);
        booking.setStartDate(dto.getStartDate());
        booking.setEndDate(dto.getEndDate());
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setPaymentStatus(PaymentStatus.UNPAID);
        booking.setTotalPrice(BigDecimal.valueOf(300));

        when(bookingMapper.toEntity(dto)).thenReturn(booking);
        when(bookingMapper.toResponseDto(booking)).thenReturn(
                BookingResponseDTO.builder()
                        .bookingStatus(BookingStatus.PENDING)
                        .paymentStatus(PaymentStatus.UNPAID)
                        .totalPrice(BigDecimal.valueOf(300))
                        .build()
        );

        BookingResponseDTO response = bookingService.createBooking(dto);

        assertEquals(BookingStatus.PENDING, response.getBookingStatus());
        assertEquals(PaymentStatus.UNPAID, response.getPaymentStatus());
        assertEquals(BigDecimal.valueOf(300), response.getTotalPrice());
        verify(notificationService).sendBookingCreatedNotification(customer, booking);
    }

    @Test
    void testCreateBooking_shouldThrowExceptionIfDatesInvalid() {
        BookingRequestDTO dto = BookingRequestDTO.builder()
                .customerId(1L)
                .carId(2L)
                .startDate(LocalDate.of(2026, 3, 15))
                .endDate(LocalDate.of(2026, 3, 12))
                .build();

        User customer = new User(); customer.setId(1L);
        Car car = new Car(); car.setId(2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(carRepository.findById(2L)).thenReturn(Optional.of(car));

        assertThrows(IllegalArgumentException.class,
                () -> bookingService.createBooking(dto));
    }

    @Test
    void testConfirmBooking_shouldConfirmPendingBooking() throws Exception {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setCustomer(new User());
        booking.setCar(new Car());

        User employee = new User(); employee.setId(99L);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(userRepository.findById(99L)).thenReturn(Optional.of(employee));
        when(bookingMapper.toResponseDto(booking)).thenReturn(
                BookingResponseDTO.builder().bookingStatus(BookingStatus.CONFIRMED).build()
        );

        BookingResponseDTO response = bookingService.confirmBooking(1L, 99L);

        assertEquals(BookingStatus.CONFIRMED, response.getBookingStatus());
        verify(contractService).addContract(1L);
        verify(notificationService).sendBookingConfirmedNotification(booking.getCustomer(), booking);
    }
}