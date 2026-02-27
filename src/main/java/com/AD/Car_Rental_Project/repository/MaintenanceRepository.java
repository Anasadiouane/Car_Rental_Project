package com.AD.Car_Rental_Project.repository;

import com.AD.Car_Rental_Project.domain.entity.Car;
import com.AD.Car_Rental_Project.domain.entity.Maintenance;
import com.AD.Car_Rental_Project.domain.entity.User;
import com.AD.Car_Rental_Project.domain.enumeration.MaintenanceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {

    // Récupérer toutes les maintenances d’une voiture
    List<Maintenance> findByCarId(Long carId);

    // Récupérer toutes les maintenances créées par un utilisateur (EMPLOYEE/ADMIN)
    List<Maintenance> findByCreatedById(Long userId);

    // Filtrer par type de maintenance
    List<Maintenance> findByMaintenanceType(MaintenanceType type);

    // Détecter les maintenances urgentes (ex: assurance expirée, visite technique dépassée)
    @Query("SELECT m FROM Maintenance m WHERE m.nextDueDate < CURRENT_DATE")
    List<Maintenance> findExpiredMaintenances();

    // Statistiques : nombre de maintenances par type
    @Query("SELECT m.maintenanceType, COUNT(m) FROM Maintenance m GROUP BY m.maintenanceType")
    List<Object[]> countMaintenancesByType();
}