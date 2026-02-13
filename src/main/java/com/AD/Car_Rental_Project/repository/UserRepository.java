package com.AD.Car_Rental_Project.repository;

import com.AD.Car_Rental_Project.domain.entity.User;
import com.AD.Car_Rental_Project.domain.enumeration.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role);
    List<User> findByActiveTrue();
    List<User> findByFullNameContainingIgnoreCase(String keyword);
    Optional<User> findByPhone(String phone);
    List<User> findByRoleIn(List<Role> roles);

}