package org.kcauniproject.kiroramanagementsystem.volunteerManagement;

import lombok.RequiredArgsConstructor;
import org.kcauniproject.kiroramanagementsystem.enums.VolunteerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/volunteers")
@RequiredArgsConstructor
public class VolunteerController {

    private final VolunteerService volunteerService;

    @PostMapping
    public ResponseEntity<Volunteer> createVolunteer(@RequestBody @Validated Volunteer volunteer) {
        try {
            Volunteer savedVolunteer = volunteerService.saveVolunteer(volunteer);
            return ResponseEntity.ok(savedVolunteer);
        } catch (Exception e) {
            // Log the error for debugging
            System.err.println("Error creating volunteer: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Volunteer> getVolunteerById(@PathVariable Long id) {
        return volunteerService.getVolunteerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Volunteer>> getAllVolunteers() {
        return ResponseEntity.ok(volunteerService.getAllVolunteers());
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<Volunteer>> getAllVolunteersPaged(Pageable pageable) {
        return ResponseEntity.ok(volunteerService.getAllVolunteers(pageable));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Volunteer>> getVolunteersByStatus(@PathVariable VolunteerStatus status) {
        return ResponseEntity.ok(volunteerService.getVolunteersByStatus(status));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Volunteer>> searchVolunteers(@RequestParam String term) {
        return ResponseEntity.ok(volunteerService.searchVolunteers(term));
    }

    @GetMapping("/email")
    public ResponseEntity<Volunteer> getVolunteerByEmail(@RequestParam String email) {
        return volunteerService.getVolunteerByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/registration-range")
    public ResponseEntity<List<Volunteer>> getByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(volunteerService.getVolunteersByRegistrationDateRange(startDate, endDate));
    }

    @GetMapping("/skill")
    public ResponseEntity<List<Volunteer>> getBySkill(@RequestParam String skill) {
        return ResponseEntity.ok(volunteerService.getVolunteersBySkill(skill));
    }

    @GetMapping("/count/status/{status}")
    public ResponseEntity<Long> countByStatus(@PathVariable VolunteerStatus status) {
        return ResponseEntity.ok(volunteerService.getVolunteersCountByStatus(status));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Volunteer> updateVolunteer(@PathVariable Long id, @RequestBody Volunteer volunteer) {
        try {
            return ResponseEntity.ok(volunteerService.updateVolunteer(id, volunteer));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVolunteer(@PathVariable Long id) {
        volunteerService.deleteVolunteer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> volunteerExists(@PathVariable Long id) {
        return ResponseEntity.ok(volunteerService.volunteerExists(id));
    }
}