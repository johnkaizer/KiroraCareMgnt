package org.kcauniproject.kiroramanagementsystem.healthManagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HealthRecordRepository extends JpaRepository<HealthRecord, Long> {

    List<HealthRecord> findByChildId(Long childId);

    List<HealthRecord> findByChildIdOrderByVisitDateDesc(Long childId);

    List<HealthRecord> findByVisitDateBetween(LocalDate startDate, LocalDate endDate);

    List<HealthRecord> findByNextAppointmentBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT hr FROM HealthRecord hr WHERE hr.nextAppointment <= :date AND hr.nextAppointment IS NOT NULL")
    List<HealthRecord> findUpcomingAppointments(@Param("date") LocalDate date);

    @Query("SELECT hr FROM HealthRecord hr WHERE hr.child.id = :childId ORDER BY hr.visitDate DESC")
    List<HealthRecord> findLatestHealthRecordsByChild(@Param("childId") Long childId);
}
