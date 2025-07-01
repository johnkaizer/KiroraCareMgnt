package org.kcauniproject.kiroramanagementsystem.educationManagement;

import jakarta.persistence.*;
import lombok.*;
import org.kcauniproject.kiroramanagementsystem.childrenManagement.Child;

import java.time.LocalDateTime;

@Entity
@Table(name = "education_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"child"})
@EqualsAndHashCode(exclude = {"child"})
public class EducationRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "child_id", nullable = false)
    private Child child;

    private String schoolName;
    private String grade;
    private String academicYear;

    @Column(length = 500)
    private String performance;

    @Column(length = 1000)
    private String notes;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}