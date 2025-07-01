package org.kcauniproject.kiroramanagementsystem.healthManagement;

import jakarta.persistence.*;
import lombok.*;
import org.kcauniproject.kiroramanagementsystem.childrenManagement.Child;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "health_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"child"})
@EqualsAndHashCode(exclude = {"child"})
public class HealthRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "child_id", nullable = false)
    private Child child;

    private LocalDate visitDate;

    @Column(length = 500)
    private String diagnosis;

    @Column(length = 500)
    private String treatment;

    @Column(length = 1000)
    private String notes;

    private LocalDate nextAppointment;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
