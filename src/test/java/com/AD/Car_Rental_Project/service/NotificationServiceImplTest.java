package com.AD.Car_Rental_Project.service;

import com.AD.Car_Rental_Project.domain.dto.response.NotificationResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.*;
import com.AD.Car_Rental_Project.domain.enumeration.NotificationType;
import com.AD.Car_Rental_Project.repository.NotificationRepository;
import com.AD.Car_Rental_Project.domain.mapper.NotificationMapper;
import com.AD.Car_Rental_Project.service.impl.NotificationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock private NotificationRepository notificationRepository;
    @Mock private NotificationMapper notificationMapper;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    // --- Booking notifications ---
    @Test
    void testSendBookingConfirmedNotification_shouldSaveNotification() {
        User user = new User(); user.setId(1L);
        Car car = new Car(); car.setPlateNumber("ABC-123");
        Booking booking = new Booking(); booking.setId(10L); booking.setCar(car);

        notificationService.sendBookingConfirmedNotification(user, booking);

        verify(notificationRepository).save(argThat(n ->
                n.getNotificationType() == NotificationType.BOOKING_CONFIRMED &&
                        n.getRelatedEntityId().equals(booking.getId()) &&
                        n.getUser().equals(user)
        ));
    }

    @Test
    void testSendBookingCreatedNotification_shouldSaveNotification() {
        User user = new User(); user.setId(1L);
        Car car = new Car(); car.setPlateNumber("XYZ-789");
        Booking booking = new Booking(); booking.setId(20L); booking.setCar(car);

        notificationService.sendBookingCreatedNotification(user, booking);

        verify(notificationRepository).save(argThat(n ->
                n.getNotificationType() == NotificationType.BOOKING_CREATED &&
                        n.getRelatedEntityId().equals(booking.getId())
        ));
    }

    @Test
    void testSendBookingEndSoonNotification_shouldSaveNotification() {
        User user = new User(); user.setId(1L);
        Car car = new Car(); car.setPlateNumber("END-123");
        Booking booking = new Booking(); booking.setId(30L); booking.setCar(car);
        booking.setEndDate(LocalDate.of(2026, 3, 15));

        notificationService.sendBookingEndSoonNotification(user, booking);

        verify(notificationRepository).save(argThat(n ->
                n.getNotificationType() == NotificationType.BOOKING_END_SOON &&
                        n.getRelatedEntityId().equals(booking.getId())
        ));
    }

    @Test
    void testSendBookingRejectedNotification_shouldSaveNotification() {
        User user = new User(); user.setId(1L);
        Car car = new Car(); car.setPlateNumber("REJ-123");
        Booking booking = new Booking(); booking.setId(40L); booking.setCar(car);

        notificationService.sendBookingRejectedNotification(user, booking, "Reason");

        verify(notificationRepository).save(argThat(n ->
                n.getTitle().equals("Booking Rejected") &&
                        n.getRelatedEntityId().equals(booking.getId())
        ));
    }

    @Test
    void testSendBookingCancelledNotification_shouldSaveNotification() {
        User user = new User(); user.setId(1L);
        Car car = new Car(); car.setPlateNumber("CAN-123");
        Booking booking = new Booking(); booking.setId(50L); booking.setCar(car);

        notificationService.sendBookingCancelledNotification(user, booking, "Reason");

        verify(notificationRepository).save(argThat(n ->
                n.getTitle().equals("Booking Cancelled") &&
                        n.getRelatedEntityId().equals(booking.getId())
        ));
    }

    // --- Payment notifications ---
    @Test
    void testSendPaymentNotification_shouldSaveNotification() {
        User user = new User(); user.setId(1L);
        Booking booking = new Booking(); booking.setId(60L);
        Payment payment = new Payment();
        payment.setBooking(booking); payment.setAmount(BigDecimal.valueOf(200));
        payment.setTransactionId("TRX-123");

        notificationService.sendPaymentNotification(user, payment);

        verify(notificationRepository).save(argThat(n ->
                n.getNotificationType() == NotificationType.PAYMENT &&
                        n.getRelatedEntityId().equals(booking.getId())
        ));
    }

    // --- Car notifications ---
    @Test
    void testSendVisitExpiredNotification_shouldSaveNotification() {
        User user = new User(); user.setId(1L);
        Car car = new Car(); car.setId(70L); car.setPlateNumber("VIS-123");

        notificationService.sendVisitExpiredNotification(car, user);

        verify(notificationRepository).save(argThat(n ->
                n.getNotificationType() == NotificationType.VISIT_EXPIRED &&
                        n.getRelatedEntityId().equals(car.getId())
        ));
    }

    @Test
    void testSendInsuranceExpiredNotification_shouldSaveNotification() {
        User user = new User(); user.setId(1L);
        Car car = new Car(); car.setId(80L); car.setPlateNumber("INS-123");

        notificationService.sendInsuranceExpiredNotification(car, user);

        verify(notificationRepository).save(argThat(n ->
                n.getNotificationType() == NotificationType.INSURANCE_EXPIRED &&
                        n.getRelatedEntityId().equals(car.getId())
        ));
    }

    @Test
    void testSendOilChangeExpiredNotification_shouldSaveNotification() {
        User user = new User(); user.setId(1L);
        Car car = new Car(); car.setId(90L); car.setPlateNumber("OIL-123");

        notificationService.sendOilChangeExpiredNotification(car, user);

        verify(notificationRepository).save(argThat(n ->
                n.getNotificationType() == NotificationType.OIL_CHANGE_EXPIRED &&
                        n.getRelatedEntityId().equals(car.getId())
        ));
    }

    @Test
    void testSendMaintenanceAlertNotification_shouldSaveNotification() {
        User user = new User(); user.setId(1L);
        Car car = new Car(); car.setId(100L); car.setPlateNumber("MAINT-123");

        notificationService.sendMaintenanceAlertNotification(car, user);

        verify(notificationRepository).save(argThat(n ->
                n.getNotificationType() == NotificationType.MAINTENANCE_ALERT &&
                        n.getRelatedEntityId().equals(car.getId())
        ));
    }

    // --- Retrieval methods ---
    @Test
    void testGetNotificationsByUser_shouldReturnMappedDtos() {
        Notification notification = new Notification();
        notification.setId(1L);
        notification.setTitle("Test");

        when(notificationRepository.findByUserId(1L)).thenReturn(List.of(notification));
        when(notificationMapper.toResponseDto(notification)).thenReturn(
                NotificationResponseDTO.builder().id(1L).title("Test").build()
        );

        List<NotificationResponseDTO> result = notificationService.getNotificationsByUser(1L);

        assertEquals(1, result.size());
        assertEquals("Test", result.get(0).getTitle());
    }

    @Test
    void testGetUnreadNotifications_shouldReturnMappedDtos() {
        Notification notification = new Notification();
        notification.setId(2L);
        notification.setTitle("Unread");
        notification.setSeen(false);

        when(notificationRepository.findByUserIdAndSeenFalse(1L)).thenReturn(List.of(notification));
        when(notificationMapper.toResponseDto(notification)).thenReturn(
                NotificationResponseDTO.builder().id(2L).title("Unread").seen(false).build()
        );

        List<NotificationResponseDTO> result = notificationService.getUnreadNotifications(1L);

        assertEquals(1, result.size());
        assertFalse(result.get(0).isSeen());
    }

    // --- Mark as seen ---
    @Test
    void testMarkNotificationAsSeen_shouldUpdateSeenFlag() {
        Notification notification = new Notification();
        notification.setId(99L); notification.setSeen(false);

        when(notificationRepository.findById(99L)).thenReturn(Optional.of(notification));

        notificationService.markNotificationAsSeen(99L);

        assertTrue(notification.isSeen());
        verify(notificationRepository).save(notification);
    }

    @Test
    void testMarkNotificationAsSeen_shouldThrowExceptionIfNotFound() {
        when(notificationRepository.findById(123L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> notificationService.markNotificationAsSeen(123L));
    }
}