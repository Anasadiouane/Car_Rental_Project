package com.AD.Car_Rental_Project.service;

import com.AD.Car_Rental_Project.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User createUser(User user);

    User getById(Long id);

    Optional<User> getByEmail(String email);

    List<User> getAdmins();

    List<User> getActiveUsers();
}
