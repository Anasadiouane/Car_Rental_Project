package com.AD.Car_Rental_Project.service;

import com.AD.Car_Rental_Project.domain.dto.response.ContractResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.Booking;
import com.AD.Car_Rental_Project.domain.entity.Car;
import com.AD.Car_Rental_Project.domain.entity.Contract;
import com.AD.Car_Rental_Project.domain.entity.User;
import com.AD.Car_Rental_Project.repository.BookingRepository;
import com.AD.Car_Rental_Project.repository.ContractRepository;
import com.AD.Car_Rental_Project.repository.UserRepository;
import com.AD.Car_Rental_Project.domain.mapper.ContractMapper;
import com.AD.Car_Rental_Project.service.impl.ContractServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractServiceImplTest {

    @Mock private ContractRepository contractRepository;
    @Mock private BookingRepository bookingRepository;
    @Mock private UserRepository userRepository;
    @Mock private ContractMapper contractMapper;

    @InjectMocks
    private ContractServiceImpl contractService;

    @Test
    void testAddContract_shouldSaveContractAndGeneratePdf() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setCustomer(new User());
        booking.setCar(new Car());
        booking.setStartDate(LocalDate.of(2026, 3, 12));
        booking.setEndDate(LocalDate.of(2026, 3, 15));
        booking.setTotalPrice(BigDecimal.valueOf(500));

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        contractService.addContract(1L);

        verify(contractRepository).save(argThat(contract ->
                contract.getContractNumber().equals("CTR-1") &&
                        contract.getPdfPath().equals("contracts/CTR-1.pdf") &&
                        contract.getBooking().equals(booking)
        ));
    }

    @Test
    void testAddContract_shouldThrowExceptionIfBookingNotFound() {
        when(bookingRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> contractService.addContract(99L));
    }

    @Test
    void testGetContractByBooking_shouldReturnDto() {
        Contract contract = new Contract();
        contract.setId(1L);
        contract.setContractNumber("CTR-1");

        when(contractRepository.findByBookingId(1L)).thenReturn(Optional.of(contract));
        when(contractMapper.toResponseDto(contract)).thenReturn(
                ContractResponseDTO.builder().contractNumber("CTR-1").build()
        );

        ContractResponseDTO response = contractService.getContractByBooking(1L);

        assertEquals("CTR-1", response.getContractNumber());
    }

    @Test
    void testGetContractByBooking_shouldThrowExceptionIfNotFound() {
        when(contractRepository.findByBookingId(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> contractService.getContractByBooking(1L));
    }

    @Test
    void testGetContractByNumber_shouldReturnDto() {
        Contract contract = new Contract();
        contract.setId(2L);
        contract.setContractNumber("CTR-2");

        when(contractRepository.findByContractNumber("CTR-2")).thenReturn(Optional.of(contract));
        when(contractMapper.toResponseDto(contract)).thenReturn(
                ContractResponseDTO.builder().contractNumber("CTR-2").build()
        );

        ContractResponseDTO response = contractService.getContractByNumber("CTR-2");

        assertEquals("CTR-2", response.getContractNumber());
    }

    @Test
    void testGetContractByNumber_shouldThrowExceptionIfNotFound() {
        when(contractRepository.findByContractNumber("CTR-99")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> contractService.getContractByNumber("CTR-99"));
    }

    @Test
    void testGetAllContracts_shouldReturnMappedDtos() {
        Contract contract = new Contract();
        contract.setId(3L);
        contract.setContractNumber("CTR-3");

        when(contractRepository.findAll()).thenReturn(List.of(contract));
        when(contractMapper.toResponseDto(contract)).thenReturn(
                ContractResponseDTO.builder().contractNumber("CTR-3").build()
        );

        List<ContractResponseDTO> result = contractService.getAllContracts();

        assertEquals(1, result.size());
        assertEquals("CTR-3", result.get(0).getContractNumber());
    }

    @Test
    void testGenerateContractPdf_shouldNotThrowException() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setCustomer(new User());
        booking.setCar(new Car());
        booking.setStartDate(LocalDate.of(2026, 3, 12));
        booking.setEndDate(LocalDate.of(2026, 3, 15));
        booking.setTotalPrice(BigDecimal.valueOf(500));

        Contract contract = Contract.builder()
                .id(1L)
                .contractNumber("CTR-1")
                .booking(booking)
                .pdfPath("contracts/test.pdf")
                .createdAt(LocalDateTime.now())
                .build();

        assertDoesNotThrow(() -> contractService.generateContractPdf(contract));
    }
}