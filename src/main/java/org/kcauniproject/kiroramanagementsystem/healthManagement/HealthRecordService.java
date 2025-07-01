package org.kcauniproject.kiroramanagementsystem.healthManagement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class HealthRecordService {

    private final HealthRecordRepository healthRecordRepository;

    public HealthRecord saveHealthRecord(HealthRecord healthRecord) {
        return healthRecordRepository.save(healthRecord);
    }

    public Optional<HealthRecord> getHealthRecordById(Long id) {
        return healthRecordRepository.findById(id);
    }

    public List<HealthRecord> getHealthRecordsByChildId(Long childId) {
        return healthRecordRepository.findByChildIdOrderByVisitDateDesc(childId);
    }

    public List<HealthRecord> getHealthRecordsByDateRange(LocalDate startDate, LocalDate endDate) {
        return healthRecordRepository.findByVisitDateBetween(startDate, endDate);
    }

    public List<HealthRecord> getUpcomingAppointments(LocalDate endDate) {
        return healthRecordRepository.findUpcomingAppointments(endDate);
    }

    public List<HealthRecord> getAppointmentsByDateRange(LocalDate startDate, LocalDate endDate) {
        return healthRecordRepository.findByNextAppointmentBetween(startDate, endDate);
    }

    public HealthRecord updateHealthRecord(Long id, HealthRecord healthRecordDetails) {
        return healthRecordRepository.findById(id)
                .map(record -> {
                    record.setVisitDate(healthRecordDetails.getVisitDate());
                    record.setDiagnosis(healthRecordDetails.getDiagnosis());
                    record.setTreatment(healthRecordDetails.getTreatment());
                    record.setNotes(healthRecordDetails.getNotes());
                    record.setNextAppointment(healthRecordDetails.getNextAppointment());
                    return healthRecordRepository.save(record);
                })
                .orElseThrow(() -> new RuntimeException("Health record not found with id: " + id));
    }

    public void deleteHealthRecord(Long id) {
        healthRecordRepository.deleteById(id);
    }
}
