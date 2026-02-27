package com.AD.Car_Rental_Project.repository;

import com.AD.Car_Rental_Project.domain.entity.User;
import com.AD.Car_Rental_Project.domain.enumeration.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Trouver un utilisateur par email (utilisé pour login/authentification)
    Optional<User> findByEmail(String email);

    // Vérifier si un email existe déjà (éviter doublons à l'inscription)
    boolean existsByEmail(String email);

    // Vérifier si un CIN existe déjà (éviter doublons)
    boolean existsByCin(String cin);

    // Récupérer tous les utilisateurs par rôle
    List<User> findByRole(Role role);

    // Récupérer les utilisateurs actifs/inactifs
    List<User> findByActive(boolean active);

    // Statistiques : nombre d’utilisateurs par rôle
    @Query("SELECT u.role, COUNT(u) FROM User u GROUP BY u.role")
    List<Object[]> countUsersByRole();
}