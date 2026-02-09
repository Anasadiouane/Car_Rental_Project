package com.AD.Car_Rental_Project.service;

import com.AD.Car_Rental_Project.domain.entity.User;
import com.AD.Car_Rental_Project.domain.enumeration.Role;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User createUser(User user);

    User getById(Long id);

    Optional<User> getByEmail(String email);

    List<User> getActiveUsers();

    List<User> getUsersByRole(Role role);
}
