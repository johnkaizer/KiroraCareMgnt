package org.kcauniproject.kiroramanagementsystem.donationManagement;

import lombok.RequiredArgsConstructor;
import org.kcauniproject.kiroramanagementsystem.enums.DonationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/donations")
@RequiredArgsConstructor
public class DonationController {

    private final DonationService donationService;

    @PostMapping
    public ResponseEntity<Donation> createDonation(@RequestBody Donation donation) {
        return ResponseEntity.ok(donationService.saveDonation(donation));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Donation> getDonationById(@PathVariable Long id) {
        return donationService.getDonationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Donation>> getAllDonations() {
        return ResponseEntity.ok(donationService.getAllDonations());
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<Donation>> getAllDonationsPaged(Pageable pageable) {
        return ResponseEntity.ok(donationService.getAllDonations(pageable));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Donation>> getDonationsByType(@PathVariable DonationType type) {
        return ResponseEntity.ok(donationService.getDonationsByType(type));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Donation>> searchByDonorName(@RequestParam String name) {
        return ResponseEntity.ok(donationService.searchDonationsByDonorName(name));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Donation>> getDonationsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(donationService.getDonationsByDateRange(startDate, endDate));
    }

    @GetMapping("/total-monetary")
    public ResponseEntity<Double> getTotalMonetaryDonations(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(donationService.getTotalMonetaryDonations(startDate, endDate));
    }

    @GetMapping("/stats/type")
    public ResponseEntity<List<Object[]>> getDonationStatsByType() {
        return ResponseEntity.ok(donationService.getDonationStatsByType());
    }

    @GetMapping("/email")
    public ResponseEntity<List<Donation>> getDonationsByEmail(@RequestParam String email) {
        return ResponseEntity.ok(donationService.getDonationsByDonorEmail(email));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Donation> updateDonation(@PathVariable Long id, @RequestBody Donation donation) {
        return ResponseEntity.ok(donationService.updateDonation(id, donation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDonation(@PathVariable Long id) {
        donationService.deleteDonation(id);
        return ResponseEntity.noContent().build();
    }
}
