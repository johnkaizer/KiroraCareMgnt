package org.kcauniproject.kiroramanagementsystem.volunteerManagement;

import org.kcauniproject.kiroramanagementsystem.enums.VolunteerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {

    List<Volunteer> findByStatus(VolunteerStatus status);

    List<Volunteer> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName, String lastName);

    Optional<Volunteer> findByEmail(String email);

    List<Volunteer> findByRegistrationDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT v FROM Volunteer v WHERE v.skills LIKE %:skill%")
    List<Volunteer> findBySkillsContaining(@Param("skill") String skill);

    @Query("SELECT COUNT(v) FROM Volunteer v WHERE v.status = :status")
    long countByStatus(@Param("status") VolunteerStatus status);
}
