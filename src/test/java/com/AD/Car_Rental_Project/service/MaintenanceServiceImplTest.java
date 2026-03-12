package com.AD.Car_Rental_Project.service;

import com.AD.Car_Rental_Project.domain.dto.request.MaintenanceRequestDTO;
import com.AD.Car_Rental_Project.domain.dto.response.MaintenanceResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.Car;
import com.AD.Car_Rental_Project.domain.entity.Maintenance;
import com.AD.Car_Rental_Project.domain.entity.User;
import com.AD.Car_Rental_Project.domain.enumeration.MaintenanceType;
import com.AD.Car_Rental_Project.domain.enumeration.RentalStatus;
import com.AD.Car_Rental_Project.domain.enumeration.TechnicalStatus;
import com.AD.Car_Rental_Project.repository.CarRepository;
import com.AD.Car_Rental_Project.repository.MaintenanceRepository;
import com.AD.Car_Rental_Project.repository.UserRepository;
import com.AD.Car_Rental_Project.domain.mapper.MaintenanceMapper;
import com.AD.Car_Rental_Project.service.impl.MaintenanceServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MaintenanceServiceImplTest {

    @Mock private MaintenanceRepository maintenanceRepository;
    @Mock private CarRepository carRepository;
    @Mock private UserRepository userRepository;
    @Mock private MaintenanceMapper maintenanceMapper;
    @Mock private NotificationService notificationService;

    @InjectMocks
    private MaintenanceServiceImpl maintenanceService;

    @Test
    void testCreateMaintenance_shouldSaveAndNotifyOilChange() {
        Car car = new Car(); car.setId(1L);
        User employee = new User(); employee.setId(2L);

        MaintenanceRequestDTO dto = MaintenanceRequestDTO.builder()
                .carId(1L)
                .userId(2L)
                .maintenanceType(MaintenanceType.OIL_CHANGE)
                .maintenanceDate(LocalDate.now())
                .nextDueDate(LocalDate.now().plusMonths(6))
                .build();

        Maintenance maintenance = new Maintenance();
        maintenance.setMaintenanceType(MaintenanceType.OIL_CHANGE);

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(userRepository.findById(2L)).thenReturn(Optional.of(employee));
        when(maintenanceMapper.toEntity(dto)).thenReturn(maintenance);
        when(maintenanceMapper.toResponseDto(maintenance)).thenReturn(
                MaintenanceResponseDTO.builder().maintenanceType(MaintenanceType.OIL_CHANGE).build()
        );

        MaintenanceResponseDTO response = maintenanceService.createMaintenance(dto, 1L, 2L);

        assertEquals(MaintenanceType.OIL_CHANGE, response.getMaintenanceType());
        assertEquals(RentalStatus.MAINTENANCE, car.getRentalStatus());
        assertEquals(TechnicalStatus.OIL_CHANGE_REQUIRED, car.getTechnicalStatus());
        verify(maintenanceRepository).save(maintenance);
        verify(carRepository).save(car);
        verify(notificationService).sendOilChangeExpiredNotification(car, employee);
    }

    @Test
    void testCreateMaintenance_shouldThrowExceptionIfCarNotFound() {
        MaintenanceRequestDTO dto = MaintenanceRequestDTO.builder()
                .carId(99L)
                .userId(2L)
                .maintenanceType(MaintenanceType.REPAIR)
                .maintenanceDate(LocalDate.now())
                .build();

        when(carRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> maintenanceService.createMaintenance(dto, 99L, 2L));
    }

    @Test
    void testCreateMaintenance_shouldThrowExceptionIfEmployeeNotFound() {
        Car car = new Car(); car.setId(1L);
        MaintenanceRequestDTO dto = MaintenanceRequestDTO.builder()
                .carId(1L)
                .userId(99L)
                .maintenanceType(MaintenanceType.REPAIR)
                .maintenanceDate(LocalDate.now())
                .build();

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> maintenanceService.createMaintenance(dto, 1L, 99L));
    }

    @Test
    void testGetMaintenancesByCar_shouldReturnDtos() {
        Maintenance maintenance = new Maintenance();
        maintenance.setId(1L);

        when(maintenanceRepository.findByCarId(1L)).thenReturn(List.of(maintenance));
        when(maintenanceMapper.toResponseDto(maintenance)).thenReturn(
                MaintenanceResponseDTO.builder().id(1L).build()
        );

        List<MaintenanceResponseDTO> result = maintenanceService.getMaintenancesByCar(1L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void testGetMaintenancesByType_shouldReturnDtos() {
        Maintenance maintenance = new Maintenance();
        maintenance.setId(2L);

        when(maintenanceRepository.findByMaintenanceType(MaintenanceType.REPAIR)).thenReturn(List.of(maintenance));
        when(maintenanceMapper.toResponseDto(maintenance)).thenReturn(
                MaintenanceResponseDTO.builder().id(2L).maintenanceType(MaintenanceType.REPAIR).build()
        );

        List<MaintenanceResponseDTO> result = maintenanceService.getMaintenancesByType(MaintenanceType.REPAIR);

        assertEquals(1, result.size());
        assertEquals(MaintenanceType.REPAIR, result.get(0).getMaintenanceType());
    }

    @Test
    void testGetExpiredMaintenances_shouldReturnDtos() {
        Maintenance maintenance = new Maintenance();
        maintenance.setId(3L);

        when(maintenanceRepository.findExpiredMaintenances()).thenReturn(List.of(maintenance));
        when(maintenanceMapper.toResponseDto(maintenance)).thenReturn(
                MaintenanceResponseDTO.builder().id(3L).build()
        );

        List<MaintenanceResponseDTO> result = maintenanceService.getExpiredMaintenances();

        assertEquals(1, result.size());
        assertEquals(3L, result.get(0).getId());
    }
}