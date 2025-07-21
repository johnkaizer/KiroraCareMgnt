package org.kcauniproject.kiroramanagementsystem.volunteer_records;

import lombok.RequiredArgsConstructor;
import org.kcauniproject.kiroramanagementsystem.enums.VolunteerRecordStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/volunteer-records")
@RequiredArgsConstructor
public class VolunteerRecordController {

    private final VolunteerRecordService volunteerRecordService;

    @PostMapping
    public ResponseEntity<VolunteerRecord> createVolunteerRecord(@RequestBody @Validated VolunteerRecord record) {
        try {
            VolunteerRecord savedRecord = volunteerRecordService.saveVolunteerRecord(record);
            return ResponseEntity.ok(savedRecord);
        } catch (Exception e) {
            System.err.println("Error creating volunteer record: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<VolunteerRecord> getVolunteerRecordById(@PathVariable Long id) {
        return volunteerRecordService.getVolunteerRecordById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<VolunteerRecord>> getAllVolunteerRecords() {
        return ResponseEntity.ok(volunteerRecordService.getAllVolunteerRecords());
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<VolunteerRecord>> getAllVolunteerRecordsPaged(Pageable pageable) {
        return ResponseEntity.ok(volunteerRecordService.getAllVolunteerRecords(pageable));
    }

    // Main endpoint for frontend: get records by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<VolunteerRecord>> getVolunteerRecordsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(volunteerRecordService.getVolunteerRecordsByUserId(userId));
    }

    @GetMapping("/user/{userId}/paged")
    public ResponseEntity<Page<VolunteerRecord>> getVolunteerRecordsByUserIdPaged(
            @PathVariable Long userId, Pageable pageable) {
        return ResponseEntity.ok(volunteerRecordService.getVolunteerRecordsByUserId(userId, pageable));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<VolunteerRecord>> getVolunteerRecordsByStatus(@PathVariable VolunteerRecordStatus status) {
        return ResponseEntity.ok(volunteerRecordService.getVolunteerRecordsByStatus(status));
    }

    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<VolunteerRecord>> getVolunteerRecordsByUserAndStatus(
            @PathVariable Long userId, @PathVariable VolunteerRecordStatus status) {
        return ResponseEntity.ok(volunteerRecordService.getVolunteerRecordsByUserAndStatus(userId, status));
    }

    @GetMapping("/search")
    public ResponseEntity<List<VolunteerRecord>> searchVolunteerRecords(@RequestParam String term) {
        return ResponseEntity.ok(volunteerRecordService.searchVolunteerRecords(term));
    }

    @GetMapping("/user/{userId}/search")
    public ResponseEntity<List<VolunteerRecord>> searchVolunteerRecordsByUser(
            @PathVariable Long userId, @RequestParam String term) {
        return ResponseEntity.ok(volunteerRecordService.searchVolunteerRecordsByUser(userId, term));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<VolunteerRecord>> getVolunteerRecordsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(volunteerRecordService.getVolunteerRecordsByDateRange(startDate, endDate));
    }

    @GetMapping("/user/{userId}/date-range")
    public ResponseEntity<List<VolunteerRecord>> getVolunteerRecordsByUserAndDateRange(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(volunteerRecordService.getVolunteerRecordsByUserAndDateRange(userId, startDate, endDate));
    }

    // Statistics endpoint for frontend
    @GetMapping("/user/{userId}/statistics")
    public ResponseEntity<Map<String, Object>> getUserStatistics(@PathVariable Long userId) {
        return ResponseEntity.ok(volunteerRecordService.getUserStatistics(userId));
    }

    @GetMapping("/recent")
    public ResponseEntity<List<VolunteerRecord>> getRecentRecords(@RequestParam(defaultValue = "30") int days) {
        return ResponseEntity.ok(volunteerRecordService.getRecentRecords(days));
    }

    @GetMapping("/user/{userId}/recent")
    public ResponseEntity<List<VolunteerRecord>> getUserRecentRecords(
            @PathVariable Long userId, @RequestParam(defaultValue = "30") int days) {
        return ResponseEntity.ok(volunteerRecordService.getUserRecentRecords(userId, days));
    }

    @GetMapping("/supervisor")
    public ResponseEntity<List<VolunteerRecord>> getRecordsBySupervisor(@RequestParam String supervisor) {
        return ResponseEntity.ok(volunteerRecordService.getRecordsBySupervisor(supervisor));
    }

    @GetMapping("/location")
    public ResponseEntity<List<VolunteerRecord>> getRecordsByLocation(@RequestParam String location) {
        return ResponseEntity.ok(volunteerRecordService.getRecordsByLocation(location));
    }

    @GetMapping("/min-hours")
    public ResponseEntity<List<VolunteerRecord>> getRecordsWithMinimumHours(@RequestParam double minHours) {
        return ResponseEntity.ok(volunteerRecordService.getRecordsWithMinimumHours(minHours));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VolunteerRecord> updateVolunteerRecord(
            @PathVariable Long id, @RequestBody VolunteerRecord record) {
        try {
            return ResponseEntity.ok(volunteerRecordService.updateVolunteerRecord(id, record));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVolunteerRecord(@PathVariable Long id) {
        try {
            volunteerRecordService.deleteVolunteerRecord(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> volunteerRecordExists(@PathVariable Long id) {
        return ResponseEntity.ok(volunteerRecordService.volunteerRecordExists(id));
    }
}
