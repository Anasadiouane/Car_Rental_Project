package com.AD.Car_Rental_Project.service.impl;

import com.AD.Car_Rental_Project.domain.entity.*;
import com.AD.Car_Rental_Project.domain.enumeration.BookingStatus;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentStatus;
import com.AD.Car_Rental_Project.domain.enumeration.PaymentType;
import com.AD.Car_Rental_Project.domain.enumeration.RentalStatus;
import com.AD.Car_Rental_Project.repository.*;
import com.AD.Car_Rental_Project.service.BookingService;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final CarRepository carRepository;
    private final ContractRepository contractRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              CarRepository carRepository,
                              ContractRepository contractRepository,
                              PaymentRepository paymentRepository,
                              UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.carRepository = carRepository;
        this.contractRepository = contractRepository;
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Booking createBooking(Long carId, String customerName, String customerCIN,
                                 String customerPhone, LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }

        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Car not found"));

        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        BigDecimal totalPrice = car.getPricePerDay().multiply(BigDecimal.valueOf(days));

        Booking booking = Booking.builder()
                .car(car)
                .customerName(customerName)
                .customerCIN(customerCIN)
                .customerPhone(customerPhone)
                .startDate(startDate)
                .endDate(endDate)
                .totalPrice(totalPrice)
                .bookingStatus(BookingStatus.PENDING)
                .build();

        return bookingRepository.save(booking);
    }

    @Override
    public Booking confirmBooking(Long bookingId, Long userId, PaymentType paymentType) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        booking.setBookingStatus(BookingStatus.CONFIRMED);
        booking.setConfirmedBy(user);

        Car car = booking.getCar();
        LocalDate today = LocalDate.now();

        if (!booking.getStartDate().isAfter(today)) {
            car.setRentalStatus(RentalStatus.RENTED);
        } else {
            car.setRentalStatus(RentalStatus.AVAILABLE); // réservée mais pas encore louée
        }
        carRepository.save(car);

        Contract contract = Contract.builder()
                .booking(booking)
                .contractNumber("CTR-" + booking.getId() + "-" + System.currentTimeMillis())
                .pdfPath("contracts/CTR-" + booking.getId() + ".pdf")
                .build();
        contractRepository.save(contract);
        booking.setContract(contract);

        Payment payment = Payment.builder()
                .booking(booking)
                .amount(booking.getTotalPrice())
                .paymentType(paymentType)
                .paymentStatus(PaymentStatus.PAID)
                .paymentDate(LocalDate.now())
                .transactionId("TX-" + booking.getId() + "-" + System.currentTimeMillis())
                .build();
        paymentRepository.save(payment);
        booking.setPayment(payment);

        return bookingRepository.save(booking);
    }

    @Override
    public Booking cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        booking.setBookingStatus(BookingStatus.CANCELLED);
        return bookingRepository.save(booking);
    }

    @Override
    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
    }

    @Override
    public List<Booking> findByStatus(BookingStatus status) {
        return bookingRepository.findByBookingStatus(status);
    }

    @Override
    public List<Booking> findByCar(Long carId) {
        return bookingRepository.findByCar_Id(carId);
    }

    @Override
    public List<Booking> findByCustomerCIN(String cin) {
        return bookingRepository.findByCustomerCIN(cin);
    }

    @Override
    public List<Booking> findByDateRange(LocalDate start, LocalDate end) {
        return bookingRepository.findByStartDateBetween(start, end);
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?") // chaque nuit à minuit
    public void updateCarRentalStatuses() {
        LocalDate today = LocalDate.now();
        List<Booking> bookings = bookingRepository.findAll();

        for (Booking booking : bookings) {
            Car car = booking.getCar();

            if (booking.getBookingStatus() == BookingStatus.CONFIRMED) {
                // Si la réservation a commencé
                if (!booking.getStartDate().isAfter(today) && !booking.getEndDate().isBefore(today)) {
                    car.setRentalStatus(RentalStatus.RENTED);
                }
                // Si la réservation est terminée
                else if (booking.getEndDate().isBefore(today)) {
                    booking.setBookingStatus(BookingStatus.FINISHED); // tu peux ajouter ce statut
                    car.setRentalStatus(RentalStatus.AVAILABLE);
                }
                carRepository.save(car);
                bookingRepository.save(booking);
            }
        }
    }

}