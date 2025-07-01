package org.kcauniproject.kiroramanagementsystem.donationManagement;

import org.kcauniproject.kiroramanagementsystem.enums.DonationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {

    List<Donation> findByType(DonationType type);

    List<Donation> findByDonorNameContainingIgnoreCase(String donorName);

    List<Donation> findByDonationDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT SUM(d.monetaryAmount) FROM Donation d WHERE d.type = 'MONETARY' AND d.donationDate BETWEEN :startDate AND :endDate")
    Double getTotalMonetaryDonations(@Param("startDate") LocalDate startDate,
                                     @Param("endDate") LocalDate endDate);

    @Query("SELECT d.type, COUNT(d) FROM Donation d GROUP BY d.type")
    List<Object[]> getDonationStatsByType();

    @Query("SELECT d FROM Donation d WHERE d.donorEmail = :email ORDER BY d.donationDate DESC")
    List<Donation> findByDonorEmail(@Param("email") String email);
}