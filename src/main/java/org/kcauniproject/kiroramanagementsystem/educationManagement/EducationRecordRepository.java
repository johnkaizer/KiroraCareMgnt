package org.kcauniproject.kiroramanagementsystem.educationManagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EducationRecordRepository extends JpaRepository<EducationRecord, Long> {

    List<EducationRecord> findByChildId(Long childId);

    List<EducationRecord> findByChildIdOrderByCreatedAtDesc(Long childId);

    List<EducationRecord> findByAcademicYear(String academicYear);

    List<EducationRecord> findByGrade(String grade);

    List<EducationRecord> findBySchoolNameContainingIgnoreCase(String schoolName);

    @Query("SELECT er FROM EducationRecord er WHERE er.child.id = :childId AND er.academicYear = :year")
    List<EducationRecord> findByChildAndAcademicYear(@Param("childId") Long childId,
                                                     @Param("year") String academicYear);
}