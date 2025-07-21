package org.kcauniproject.kiroramanagementsystem.volunteer_records;

import org.kcauniproject.kiroramanagementsystem.enums.VolunteerRecordStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface VolunteerRecordRepository extends JpaRepository<VolunteerRecord, Long> {

    // Find records by user ID
    List<VolunteerRecord> findByUserIdOrderByActivityDateDesc(Long userId);

    // Find records by user ID with pagination
    Page<VolunteerRecord> findByUserIdOrderByActivityDateDesc(Long userId, Pageable pageable);

    // Find records by status
    List<VolunteerRecord> findByStatus(VolunteerRecordStatus status);

    // Find records by user and status
    List<VolunteerRecord> findByUserIdAndStatus(Long userId, VolunteerRecordStatus status);

    // Find records by date range
    List<VolunteerRecord> findByActivityDateBetween(LocalDate startDate, LocalDate endDate);

    // Find records by user and date range
    List<VolunteerRecord> findByUserIdAndActivityDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    // Search records by text (activity title, description, location, supervisor)
    @Query("SELECT vr FROM VolunteerRecord vr WHERE " +
            "LOWER(vr.activityTitle) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(vr.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(vr.location) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(vr.supervisor) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(vr.skills) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<VolunteerRecord> searchRecords(@Param("searchTerm") String searchTerm);

    // Search records by user and text
    @Query("SELECT vr FROM VolunteerRecord vr WHERE vr.userId = :userId AND (" +
            "LOWER(vr.activityTitle) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(vr.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(vr.location) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(vr.supervisor) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(vr.skills) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<VolunteerRecord> searchRecordsByUser(@Param("userId") Long userId, @Param("searchTerm") String searchTerm);

    // Statistics queries
    @Query("SELECT COUNT(vr) FROM VolunteerRecord vr WHERE vr.userId = :userId")
    Long countByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(vr) FROM VolunteerRecord vr WHERE vr.userId = :userId AND vr.status = :status")
    Long countByUserIdAndStatus(@Param("userId") Long userId, @Param("status") VolunteerRecordStatus status);

    @Query("SELECT COALESCE(SUM(vr.hoursWorked), 0) FROM VolunteerRecord vr WHERE vr.userId = :userId")
    BigDecimal sumHoursByUserId(@Param("userId") Long userId);

    @Query("SELECT COALESCE(SUM(vr.hoursWorked), 0) FROM VolunteerRecord vr WHERE vr.userId = :userId AND vr.status = :status")
    BigDecimal sumHoursByUserIdAndStatus(@Param("userId") Long userId, @Param("status") VolunteerRecordStatus status);

    // Find records by supervisor
    List<VolunteerRecord> findBySupervisorContainingIgnoreCase(String supervisor);

    // Find records by location
    List<VolunteerRecord> findByLocationContainingIgnoreCase(String location);

    // Find records with minimum hours
    @Query("SELECT vr FROM VolunteerRecord vr WHERE vr.hoursWorked >= :minHours")
    List<VolunteerRecord> findRecordsWithMinimumHours(@Param("minHours") BigDecimal minHours);

    // Find recent records (last 30 days)
    @Query("SELECT vr FROM VolunteerRecord vr WHERE vr.activityDate >= :sinceDate ORDER BY vr.activityDate DESC")
    List<VolunteerRecord> findRecentRecords(@Param("sinceDate") LocalDate sinceDate);

    // Find user's recent records
    @Query("SELECT vr FROM VolunteerRecord vr WHERE vr.userId = :userId AND vr.activityDate >= :sinceDate ORDER BY vr.activityDate DESC")
    List<VolunteerRecord> findUserRecentRecords(@Param("userId") Long userId, @Param("sinceDate") LocalDate sinceDate);
}
