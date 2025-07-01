package org.kcauniproject.kiroramanagementsystem.healthManagement;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/health-records")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HealthRecordController {

    private final HealthRecordService healthRecordService;

    @PostMapping
    public ResponseEntity<HealthRecord> createHealthRecord(@Valid @RequestBody HealthRecord healthRecord) {
        HealthRecord savedRecord = healthRecordService.saveHealthRecord(healthRecord);
        return new ResponseEntity<>(savedRecord, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HealthRecord> getHealthRecordById(@PathVariable Long id) {
        return healthRecordService.getHealthRecordById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/child/{childId}")
    public ResponseEntity<List<HealthRecord>> getHealthRecordsByChildId(@PathVariable Long childId) {
        List<HealthRecord> records = healthRecordService.getHealthRecordsByChildId(childId);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<HealthRecord>> getHealthRecordsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<HealthRecord> records = healthRecordService.getHealthRecordsByDateRange(startDate, endDate);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/upcoming-appointments")
    public ResponseEntity<List<HealthRecord>> getUpcomingAppointments(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        LocalDate queryDate = endDate != null ? endDate : LocalDate.now().plusDays(30);
        List<HealthRecord> records = healthRecordService.getUpcomingAppointments(queryDate);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/appointments-range")
    public ResponseEntity<List<HealthRecord>> getAppointmentsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<HealthRecord> records = healthRecordService.getAppointmentsByDateRange(startDate, endDate);
        return ResponseEntity.ok(records);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HealthRecord> updateHealthRecord(@PathVariable Long id,
                                                           @Valid @RequestBody HealthRecord healthRecordDetails) {
        try {
            HealthRecord updatedRecord = healthRecordService.updateHealthRecord(id, healthRecordDetails);
            return ResponseEntity.ok(updatedRecord);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHealthRecord(@PathVariable Long id) {
        if (healthRecordService.getHealthRecordById(id).isPresent()) {
            healthRecordService.deleteHealthRecord(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
