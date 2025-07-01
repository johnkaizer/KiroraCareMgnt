package org.kcauniproject.kiroramanagementsystem.donationManagement;

import lombok.RequiredArgsConstructor;
import org.kcauniproject.kiroramanagementsystem.enums.DonationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DonationService {

    private final DonationRepository donationRepository;

    public Donation saveDonation(Donation donation) {
        return donationRepository.save(donation);
    }

    public Optional<Donation> getDonationById(Long id) {
        return donationRepository.findById(id);
    }

    public List<Donation> getAllDonations() {
        return donationRepository.findAll();
    }

    public Page<Donation> getAllDonations(Pageable pageable) {
        return donationRepository.findAll(pageable);
    }

    public List<Donation> getDonationsByType(DonationType type) {
        return donationRepository.findByType(type);
    }

    public List<Donation> searchDonationsByDonorName(String donorName) {
        return donationRepository.findByDonorNameContainingIgnoreCase(donorName);
    }

    public List<Donation> getDonationsByDateRange(LocalDate startDate, LocalDate endDate) {
        return donationRepository.findByDonationDateBetween(startDate, endDate);
    }

    public Double getTotalMonetaryDonations(LocalDate startDate, LocalDate endDate) {
        Double total = donationRepository.getTotalMonetaryDonations(startDate, endDate);
        return total != null ? total : 0.0;
    }

    public List<Object[]> getDonationStatsByType() {
        return donationRepository.getDonationStatsByType();
    }

    public List<Donation> getDonationsByDonorEmail(String email) {
        return donationRepository.findByDonorEmail(email);
    }

    public Donation updateDonation(Long id, Donation donationDetails) {
        return donationRepository.findById(id)
                .map(donation -> {
                    donation.setDonorName(donationDetails.getDonorName());
                    donation.setDonorEmail(donationDetails.getDonorEmail());
                    donation.setDonorPhone(donationDetails.getDonorPhone());
                    donation.setType(donationDetails.getType());
                    donation.setDescription(donationDetails.getDescription());
                    donation.setMonetaryAmount(donationDetails.getMonetaryAmount());
                    donation.setQuantity(donationDetails.getQuantity());
                    donation.setNotes(donationDetails.getNotes());
                    return donationRepository.save(donation);
                })
                .orElseThrow(() -> new RuntimeException("Donation not found with id: " + id));
    }

    public void deleteDonation(Long id) {
        donationRepository.deleteById(id);
    }
}

