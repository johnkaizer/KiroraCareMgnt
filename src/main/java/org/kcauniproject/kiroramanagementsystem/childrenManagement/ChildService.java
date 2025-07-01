package org.kcauniproject.kiroramanagementsystem.childrenManagement;

import lombok.RequiredArgsConstructor;
import org.kcauniproject.kiroramanagementsystem.enums.ChildStatus;
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
public class ChildService {

    private final ChildRepository childRepository;

    public Child saveChild(Child child) {
        return childRepository.save(child);
    }

    public Optional<Child> getChildById(Long id) {
        return childRepository.findById(id);
    }

    public List<Child> getAllChildren() {
        return childRepository.findAll();
    }

    public Page<Child> getAllChildren(Pageable pageable) {
        return childRepository.findAll(pageable);
    }

    public List<Child> getChildrenByStatus(ChildStatus status) {
        return childRepository.findByStatus(status);
    }

    public List<Child> searchChildren(String searchTerm) {
        return childRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                searchTerm, searchTerm);
    }

    public List<Child> getChildrenByAdmissionDateRange(LocalDate startDate, LocalDate endDate) {
        return childRepository.findByAdmissionDateBetween(startDate, endDate);
    }

    public List<Child> getChildrenByAgeRange(LocalDate startDate, LocalDate endDate) {
        return childRepository.findByDateOfBirthBetween(startDate, endDate);
    }

    public long getChildrenCountByStatus(ChildStatus status) {
        return childRepository.countByStatus(status);
    }

    public Child updateChild(Long id, Child childDetails) {
        return childRepository.findById(id)
                .map(child -> {
                    child.setFirstName(childDetails.getFirstName());
                    child.setLastName(childDetails.getLastName());
                    child.setDateOfBirth(childDetails.getDateOfBirth());
                    child.setGender(childDetails.getGender());
                    child.setBackgroundStory(childDetails.getBackgroundStory());
                    child.setStatus(childDetails.getStatus());
                    return childRepository.save(child);
                })
                .orElseThrow(() -> new RuntimeException("Child not found with id: " + id));
    }

    public void deleteChild(Long id) {
        childRepository.deleteById(id);
    }

    public boolean childExists(Long id) {
        return childRepository.existsById(id);
    }
}