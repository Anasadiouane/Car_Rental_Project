package com.AD.Car_Rental_Project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CarRentalProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarRentalProjectApplication.class, args);
    }

}
