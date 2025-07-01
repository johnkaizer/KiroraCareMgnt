package org.kcauniproject.kiroramanagementsystem.childrenManagement;

import org.kcauniproject.kiroramanagementsystem.enums.ChildStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChildRepository extends JpaRepository<Child, Long> {

    List<Child> findByStatus(ChildStatus status);

    List<Child> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName, String lastName);

    List<Child> findByAdmissionDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT c FROM Child c WHERE c.dateOfBirth BETWEEN :startDate AND :endDate")
    List<Child> findByDateOfBirthBetween(@Param("startDate") LocalDate startDate,
                                         @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(c) FROM Child c WHERE c.status = :status")
    long countByStatus(@Param("status") ChildStatus status);

    Optional<Child> findByFirstNameAndLastNameAndDateOfBirth(
            String firstName, String lastName, LocalDate dateOfBirth);
}
