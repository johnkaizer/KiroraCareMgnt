package org.kcauniproject.kiroramanagementsystem.educationManagement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class EducationRecordService {

    private final EducationRecordRepository educationRecordRepository;

    public EducationRecord saveEducationRecord(EducationRecord educationRecord) {
        return educationRecordRepository.save(educationRecord);
    }

    public Optional<EducationRecord> getEducationRecordById(Long id) {
        return educationRecordRepository.findById(id);
    }

    public List<EducationRecord> getEducationRecordsByChildId(Long childId) {
        return educationRecordRepository.findByChildIdOrderByCreatedAtDesc(childId);
    }

    public List<EducationRecord> getEducationRecordsByAcademicYear(String academicYear) {
        return educationRecordRepository.findByAcademicYear(academicYear);
    }

    public List<EducationRecord> getEducationRecordsByGrade(String grade) {
        return educationRecordRepository.findByGrade(grade);
    }

    public List<EducationRecord> searchBySchoolName(String schoolName) {
        return educationRecordRepository.findBySchoolNameContainingIgnoreCase(schoolName);
    }

    public List<EducationRecord> getEducationRecordsByChildAndYear(Long childId, String academicYear) {
        return educationRecordRepository.findByChildAndAcademicYear(childId, academicYear);
    }

    public EducationRecord updateEducationRecord(Long id, EducationRecord educationRecordDetails) {
        return educationRecordRepository.findById(id)
                .map(record -> {
                    record.setSchoolName(educationRecordDetails.getSchoolName());
                    record.setGrade(educationRecordDetails.getGrade());
                    record.setAcademicYear(educationRecordDetails.getAcademicYear());
                    record.setPerformance(educationRecordDetails.getPerformance());
                    record.setNotes(educationRecordDetails.getNotes());
                    return educationRecordRepository.save(record);
                })
                .orElseThrow(() -> new RuntimeException("Education record not found with id: " + id));
    }

    public void deleteEducationRecord(Long id) {
        educationRecordRepository.deleteById(id);
    }
}

