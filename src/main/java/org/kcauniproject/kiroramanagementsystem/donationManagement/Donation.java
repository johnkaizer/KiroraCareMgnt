package org.kcauniproject.kiroramanagementsystem.donationManagement;

import jakarta.persistence.*;
import lombok.*;
import org.kcauniproject.kiroramanagementsystem.enums.DonationType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "donations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String donorName;
    private String donorEmail;
    private String donorPhone;

    @Enumerated(EnumType.STRING)
    private DonationType type;

    @Column(length = 500)
    private String description;

    private Double monetaryAmount;
    private Integer quantity;

    @Builder.Default
    private LocalDate donationDate = LocalDate.now();

    @Column(length = 1000)
    private String notes;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (donationDate == null) {
            donationDate = LocalDate.now();
        }
    }
}

