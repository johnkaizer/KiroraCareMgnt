package org.kcauniproject.kiroramanagementsystem.volunteerManagement;

import jakarta.persistence.*;
import lombok.*;
import org.kcauniproject.kiroramanagementsystem.enums.VolunteerStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "volunteers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Volunteer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String email;
    private String phone;

    @Column(length = 1000)
    private String skills;

    @Column(length = 500)
    private String availability;

    @Builder.Default
    private LocalDate registrationDate = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private VolunteerStatus status = VolunteerStatus.ACTIVE;

    @Column(length = 1000)
    private String notes;

    // Add the linkedUserId field that the frontend expects
    private Long linkedUserId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (registrationDate == null) {
            registrationDate = LocalDate.now();
        }
        if (status == null) {
            status = VolunteerStatus.ACTIVE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}