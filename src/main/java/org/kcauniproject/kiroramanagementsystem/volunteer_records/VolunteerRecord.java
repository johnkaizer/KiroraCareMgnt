package org.kcauniproject.kiroramanagementsystem.volunteer_records;

import jakarta.persistence.*;
import lombok.*;
import org.kcauniproject.kiroramanagementsystem.enums.VolunteerRecordStatus;
import org.kcauniproject.kiroramanagementsystem.usersmanagement.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "volunteer_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class VolunteerRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String activityTitle;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private LocalDate activityDate;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal hoursWorked;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private VolunteerRecordStatus status = VolunteerRecordStatus.COMPLETED;

    @Column(length = 255)
    private String location;

    @Column(length = 255)
    private String supervisor;

    private Integer beneficiaries;

    @Column(length = 1000)
    private String skills;

    @Column(length = 2000)
    private String notes;

    // Link to User entity - ignore this in JSON serialization
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;

    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = VolunteerRecordStatus.COMPLETED;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Helper method to get hours as double for frontend compatibility
    public double getHoursWorkedAsDouble() {
        return hoursWorked != null ? hoursWorked.doubleValue() : 0.0;
    }

    // Helper method to set hours from double
    public void setHoursWorked(double hours) {
        this.hoursWorked = BigDecimal.valueOf(hours);
    }
}