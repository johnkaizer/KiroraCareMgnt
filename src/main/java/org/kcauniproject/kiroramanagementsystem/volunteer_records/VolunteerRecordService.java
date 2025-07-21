package org.kcauniproject.kiroramanagementsystem.volunteer_records;

import lombok.RequiredArgsConstructor;
import org.kcauniproject.kiroramanagementsystem.enums.VolunteerRecordStatus;
import org.kcauniproject.kiroramanagementsystem.usersmanagement.User;
import org.kcauniproject.kiroramanagementsystem.usersmanagement.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class VolunteerRecordService {

    private final VolunteerRecordRepository volunteerRecordRepository;
    private final UserRepository userRepository; // Assuming you have this

    public VolunteerRecord saveVolunteerRecord(VolunteerRecord record) {
        // Validate user exists
        if (record.getUserId() != null) {
            User user = userRepository.findById(record.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + record.getUserId()));
            record.setUser(user);
        }

        // Set default values if not provided
        if (record.getStatus() == null) {
            record.setStatus(VolunteerRecordStatus.COMPLETED);
        }

        return volunteerRecordRepository.save(record);
    }

    public Optional<VolunteerRecord> getVolunteerRecordById(Long id) {
        return volunteerRecordRepository.findById(id);
    }

    public List<VolunteerRecord> getAllVolunteerRecords() {
        return volunteerRecordRepository.findAll();
    }

    public Page<VolunteerRecord> getAllVolunteerRecords(Pageable pageable) {
        return volunteerRecordRepository.findAll(pageable);
    }

    public List<VolunteerRecord> getVolunteerRecordsByUserId(Long userId) {
        return volunteerRecordRepository.findByUserIdOrderByActivityDateDesc(userId);
    }

    public Page<VolunteerRecord> getVolunteerRecordsByUserId(Long userId, Pageable pageable) {
        return volunteerRecordRepository.findByUserIdOrderByActivityDateDesc(userId, pageable);
    }

    public List<VolunteerRecord> getVolunteerRecordsByStatus(VolunteerRecordStatus status) {
        return volunteerRecordRepository.findByStatus(status);
    }

    public List<VolunteerRecord> getVolunteerRecordsByUserAndStatus(Long userId, VolunteerRecordStatus status) {
        return volunteerRecordRepository.findByUserIdAndStatus(userId, status);
    }

    public List<VolunteerRecord> getVolunteerRecordsByDateRange(LocalDate startDate, LocalDate endDate) {
        return volunteerRecordRepository.findByActivityDateBetween(startDate, endDate);
    }

    public List<VolunteerRecord> getVolunteerRecordsByUserAndDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        return volunteerRecordRepository.findByUserIdAndActivityDateBetween(userId, startDate, endDate);
    }

    public List<VolunteerRecord> searchVolunteerRecords(String searchTerm) {
        return volunteerRecordRepository.searchRecords(searchTerm);
    }

    public List<VolunteerRecord> searchVolunteerRecordsByUser(Long userId, String searchTerm) {
        return volunteerRecordRepository.searchRecordsByUser(userId, searchTerm);
    }

    public VolunteerRecord updateVolunteerRecord(Long id, VolunteerRecord updatedRecord) {
        return volunteerRecordRepository.findById(id)
                .map(existing -> {
                    // Update fields
                    existing.setActivityTitle(updatedRecord.getActivityTitle());
                    existing.setDescription(updatedRecord.getDescription());
                    existing.setActivityDate(updatedRecord.getActivityDate());
                    existing.setHoursWorked(updatedRecord.getHoursWorked().doubleValue());
                    existing.setStatus(updatedRecord.getStatus());
                    existing.setLocation(updatedRecord.getLocation());
                    existing.setSupervisor(updatedRecord.getSupervisor());
                    existing.setBeneficiaries(updatedRecord.getBeneficiaries());
                    existing.setSkills(updatedRecord.getSkills());
                    existing.setNotes(updatedRecord.getNotes());

                    return volunteerRecordRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Volunteer record not found with id: " + id));
    }

    public void deleteVolunteerRecord(Long id) {
        if (!volunteerRecordRepository.existsById(id)) {
            throw new RuntimeException("Volunteer record not found with id: " + id);
        }
        volunteerRecordRepository.deleteById(id);
    }

    public boolean volunteerRecordExists(Long id) {
        return volunteerRecordRepository.existsById(id);
    }

    // Statistics methods
    public Map<String, Object> getUserStatistics(Long userId) {
        Map<String, Object> stats = new HashMap<>();

        Long totalRecords = volunteerRecordRepository.countByUserId(userId);
        Long completedRecords = volunteerRecordRepository.countByUserIdAndStatus(userId, VolunteerRecordStatus.COMPLETED);
        Long pendingRecords = volunteerRecordRepository.countByUserIdAndStatus(userId, VolunteerRecordStatus.PENDING);
        Long inProgressRecords = volunteerRecordRepository.countByUserIdAndStatus(userId, VolunteerRecordStatus.IN_PROGRESS);

        BigDecimal totalHours = volunteerRecordRepository.sumHoursByUserId(userId);

        stats.put("totalRecords", totalRecords);
        stats.put("completedRecords", completedRecords);
        stats.put("pendingRecords", pendingRecords);
        stats.put("inProgressRecords", inProgressRecords);
        stats.put("totalHours", totalHours != null ? totalHours.doubleValue() : 0.0);

        return stats;
    }

    public List<VolunteerRecord> getRecentRecords(int days) {
        LocalDate sinceDate = LocalDate.now().minusDays(days);
        return volunteerRecordRepository.findRecentRecords(sinceDate);
    }

    public List<VolunteerRecord> getUserRecentRecords(Long userId, int days) {
        LocalDate sinceDate = LocalDate.now().minusDays(days);
        return volunteerRecordRepository.findUserRecentRecords(userId, sinceDate);
    }

    public List<VolunteerRecord> getRecordsBySupervisor(String supervisor) {
        return volunteerRecordRepository.findBySupervisorContainingIgnoreCase(supervisor);
    }

    public List<VolunteerRecord> getRecordsByLocation(String location) {
        return volunteerRecordRepository.findByLocationContainingIgnoreCase(location);
    }

    public List<VolunteerRecord> getRecordsWithMinimumHours(double minHours) {
        return volunteerRecordRepository.findRecordsWithMinimumHours(BigDecimal.valueOf(minHours));
    }
}