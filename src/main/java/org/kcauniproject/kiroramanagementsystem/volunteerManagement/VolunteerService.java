package org.kcauniproject.kiroramanagementsystem.volunteerManagement;

import lombok.RequiredArgsConstructor;
import org.kcauniproject.kiroramanagementsystem.enums.VolunteerStatus;
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
public class VolunteerService {

    private final VolunteerRepository volunteerRepository;

    public Volunteer saveVolunteer(Volunteer volunteer) {
        return volunteerRepository.save(volunteer);
    }

    public Optional<Volunteer> getVolunteerById(Long id) {
        return volunteerRepository.findById(id);
    }

    public List<Volunteer> getAllVolunteers() {
        return volunteerRepository.findAll();
    }

    public Page<Volunteer> getAllVolunteers(Pageable pageable) {
        return volunteerRepository.findAll(pageable);
    }

    public List<Volunteer> getVolunteersByStatus(VolunteerStatus status) {
        return volunteerRepository.findByStatus(status);
    }

    public List<Volunteer> searchVolunteers(String searchTerm) {
        return volunteerRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                searchTerm, searchTerm);
    }

    public Optional<Volunteer> getVolunteerByEmail(String email) {
        return volunteerRepository.findByEmail(email);
    }

    public List<Volunteer> getVolunteersByRegistrationDateRange(LocalDate startDate, LocalDate endDate) {
        return volunteerRepository.findByRegistrationDateBetween(startDate, endDate);
    }

    public List<Volunteer> getVolunteersBySkill(String skill) {
        return volunteerRepository.findBySkillsContaining(skill);
    }

    public long getVolunteersCountByStatus(VolunteerStatus status) {
        return volunteerRepository.countByStatus(status);
    }

    public Volunteer updateVolunteer(Long id, Volunteer volunteerDetails) {
        return volunteerRepository.findById(id)
                .map(volunteer -> {
                    volunteer.setFirstName(volunteerDetails.getFirstName());
                    volunteer.setLastName(volunteerDetails.getLastName());
                    volunteer.setEmail(volunteerDetails.getEmail());
                    volunteer.setPhone(volunteerDetails.getPhone());
                    volunteer.setSkills(volunteerDetails.getSkills());
                    volunteer.setAvailability(volunteerDetails.getAvailability());
                    volunteer.setStatus(volunteerDetails.getStatus());
                    volunteer.setNotes(volunteerDetails.getNotes());
                    return volunteerRepository.save(volunteer);
                })
                .orElseThrow(() -> new RuntimeException("Volunteer not found with id: " + id));
    }

    public void deleteVolunteer(Long id) {
        volunteerRepository.deleteById(id);
    }

    public boolean volunteerExists(Long id) {
        return volunteerRepository.existsById(id);
    }
}
