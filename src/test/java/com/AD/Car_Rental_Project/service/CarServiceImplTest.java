package com.AD.Car_Rental_Project.service;

import com.AD.Car_Rental_Project.domain.dto.request.CarRequestDTO;
import com.AD.Car_Rental_Project.domain.dto.response.CarResponseDTO;
import com.AD.Car_Rental_Project.domain.entity.Car;
import com.AD.Car_Rental_Project.domain.enumeration.RentalStatus;
import com.AD.Car_Rental_Project.domain.enumeration.TechnicalStatus;
import com.AD.Car_Rental_Project.domain.mapper.CarMapper;
import com.AD.Car_Rental_Project.repository.CarRepository;
import com.AD.Car_Rental_Project.service.impl.CarServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private CarMapper carMapper;

    @InjectMocks
    private CarServiceImpl carService;

    @Test
    void testAddCar_shouldSetDefaultStatuses() {
        CarRequestDTO dto = CarRequestDTO.builder()
                .brand("Toyota")
                .model("Corolla")
                .year(2022)
                .plateNumber("ABC-123")
                .pricePerDay(BigDecimal.valueOf(300))
                .mileage(10000)
                .rentalStatus(RentalStatus.AVAILABLE)
                .technicalStatus(TechnicalStatus.OK)
                .build();

        Car car = new Car();
        car.setBrand("Toyota");
        car.setModel("Corolla");
        car.setYear(2022);
        car.setPlateNumber("ABC-123");
        car.setPricePerDay(BigDecimal.valueOf(300));

        when(carMapper.toEntity(dto)).thenReturn(car);
        when(carMapper.toResponseDto(any(Car.class)))
                .thenReturn(CarResponseDTO.builder()
                        .brand("Toyota")
                        .model("Corolla")
                        .rentalStatus(RentalStatus.AVAILABLE)
                        .technicalStatus(TechnicalStatus.OK)
                        .build());

        CarResponseDTO response = carService.addCar(dto);

        assertEquals(RentalStatus.AVAILABLE, response.getRentalStatus());
        assertEquals(TechnicalStatus.OK, response.getTechnicalStatus());
        verify(carRepository).save(car);
    }

    @Test
    void testUpdateCar_shouldUpdateFields() {
        CarRequestDTO dto = CarRequestDTO.builder()
                .brand("Honda")
                .model("Civic")
                .year(2023)
                .plateNumber("XYZ-789")
                .pricePerDay(BigDecimal.valueOf(400))
                .mileage(5000)
                .rentalStatus(RentalStatus.AVAILABLE)
                .technicalStatus(TechnicalStatus.OK)
                .build();

        Car existingCar = new Car();
        existingCar.setId(1L);
        existingCar.setBrand("Toyota");

        when(carRepository.findById(1L)).thenReturn(Optional.of(existingCar));
        when(carMapper.toResponseDto(existingCar))
                .thenReturn(CarResponseDTO.builder()
                        .id(1L)
                        .brand("Honda")
                        .model("Civic")
                        .build());

        CarResponseDTO response = carService.updateCar(1L, dto);

        assertEquals("Honda", response.getBrand());
        assertEquals("Civic", response.getModel());
        verify(carRepository).save(existingCar);
    }

    @Test
    void testUpdateCar_shouldThrowExceptionIfNotFound() {
        when(carRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> carService.updateCar(99L, new CarRequestDTO()));
    }

    @Test
    void testDeleteCar_shouldCallRepository() {
        carService.deleteCar(1L);
        verify(carRepository).deleteById(1L);
    }

    @Test
    void testGetAvailableCars_shouldReturnMappedDtos() {
        Car car = new Car();
        car.setRentalStatus(RentalStatus.AVAILABLE);

        when(carRepository.findByRentalStatus(RentalStatus.AVAILABLE))
                .thenReturn(List.of(car));
        when(carMapper.toResponseDto(car))
                .thenReturn(CarResponseDTO.builder().rentalStatus(RentalStatus.AVAILABLE).build());

        List<CarResponseDTO> result = carService.getAvailableCars();

        assertEquals(1, result.size());
        assertEquals(RentalStatus.AVAILABLE, result.get(0).getRentalStatus());
    }
}