package com.AD.Car_Rental_Project.service;

import com.AD.Car_Rental_Project.domain.entity.User;
import com.AD.Car_Rental_Project.domain.enumeration.Role;

import java.util.List;
import java.util.Optional;

public interface UserService {

    // ====== Core Operations ======
    User createUser(User user);

    User updateUser(Long id, User user);

    void deleteUser(Long id);

    Optional<User> findById(Long id);

    List<User> findAll();

    // ====== Search Methods ======
    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);

    List<User> findByRole(Role role);

    List<User> findByRoleIn(List<Role> roles);
}