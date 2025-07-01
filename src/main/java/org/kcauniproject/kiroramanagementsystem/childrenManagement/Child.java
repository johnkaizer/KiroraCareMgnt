package org.kcauniproject.kiroramanagementsystem.childrenManagement;
import jakarta.persistence.*;
import lombok.*;
import org.kcauniproject.kiroramanagementsystem.educationManagement.EducationRecord;
import org.kcauniproject.kiroramanagementsystem.enums.ChildStatus;
import org.kcauniproject.kiroramanagementsystem.enums.Gender;
import org.kcauniproject.kiroramanagementsystem.healthManagement.HealthRecord;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "children")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"healthRecords", "educationRecords"})
@EqualsAndHashCode(exclude = {"healthRecords", "educationRecords"})
public class Child {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate admissionDate;

    @Column(length = 1000)
    private String backgroundStory;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ChildStatus status = ChildStatus.ACTIVE;

    // One-to-Many relationships
    @OneToMany(mappedBy = "child", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HealthRecord> healthRecords;

    @OneToMany(mappedBy = "child", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EducationRecord> educationRecords;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (admissionDate == null) {
            admissionDate = LocalDate.now();
        }
        if (status == null) {
            status = ChildStatus.ACTIVE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
