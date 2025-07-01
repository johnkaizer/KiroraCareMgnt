package org.kcauniproject.kiroramanagementsystem.educationManagement;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/education-records")
@RequiredArgsConstructor
public class EducationRecordController {

    private final EducationRecordService educationRecordService;

    @PostMapping
    public ResponseEntity<EducationRecord> createEducationRecord(@RequestBody EducationRecord educationRecord) {
        return ResponseEntity.ok(educationRecordService.saveEducationRecord(educationRecord));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EducationRecord> getEducationRecordById(@PathVariable Long id) {
        return educationRecordService.getEducationRecordById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/child/{childId}")
    public ResponseEntity<List<EducationRecord>> getByChildId(@PathVariable Long childId) {
        return ResponseEntity.ok(educationRecordService.getEducationRecordsByChildId(childId));
    }

    @GetMapping("/academic-year/{year}")
    public ResponseEntity<List<EducationRecord>> getByAcademicYear(@PathVariable String year) {
        return ResponseEntity.ok(educationRecordService.getEducationRecordsByAcademicYear(year));
    }

    @GetMapping("/grade/{grade}")
    public ResponseEntity<List<EducationRecord>> getByGrade(@PathVariable String grade) {
        return ResponseEntity.ok(educationRecordService.getEducationRecordsByGrade(grade));
    }

    @GetMapping("/search")
    public ResponseEntity<List<EducationRecord>> searchBySchoolName(@RequestParam String schoolName) {
        return ResponseEntity.ok(educationRecordService.searchBySchoolName(schoolName));
    }

    @GetMapping("/child/{childId}/year/{year}")
    public ResponseEntity<List<EducationRecord>> getByChildAndYear(@PathVariable Long childId, @PathVariable String year) {
        return ResponseEntity.ok(educationRecordService.getEducationRecordsByChildAndYear(childId, year));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EducationRecord> updateEducationRecord(@PathVariable Long id, @RequestBody EducationRecord educationRecordDetails) {
        try {
            return ResponseEntity.ok(educationRecordService.updateEducationRecord(id, educationRecordDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEducationRecord(@PathVariable Long id) {
        educationRecordService.deleteEducationRecord(id);
        return ResponseEntity.noContent().build();
    }
}
