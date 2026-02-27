package com.AD.Car_Rental_Project.repository;

import com.AD.Car_Rental_Project.domain.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

    // Trouver un contrat par son numéro unique
    Optional<Contract> findByContractNumber(String contractNumber);

    // Trouver un contrat lié à une réservation
    Optional<Contract> findByBookingId(Long bookingId);

    // Statistiques : nombre de contrats générés par mois
    @Query("SELECT MONTH(c.createdAt), COUNT(c) FROM Contract c GROUP BY MONTH(c.createdAt)")
    List<Object[]> countContractsPerMonth();
}