package com.example.orientlamp_back.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "university")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class University {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank(message = "University name is required")
    @Size(max = 255)
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 255)
    @Column(name = "location")
    private String location;

    @Size(max = 100)
    @Column(name = "type", length = 100)
    private String type;

    @Lob
    @Column(name = "description")
    private String description;

    @Size(max = 255)
    @Column(name = "website")
    private String website;

    @Email(message = "Email should be valid")
    @Size(max = 255)
    @Column(name = "contact_email")
    private String contactEmail;

    @Size(max = 20)
    @Column(name = "phone", length = 20)
    private String phone;

    @Size(max = 100)
    @Column(name = "accreditation_status", length = 100)
    private String accreditationStatus;

    @Lob
    @Column(name = "programs")
    private String programs;


    @Column(name = "created_at")
    private Instant createdAt;


    @Column(name = "updated_at")
    private Instant updatedAt;

    // Relationship with Filiere (One-to-Many)
    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Filiere> filieres = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    // Helper methods for bidirectional relationship
    public void addFiliere(Filiere filiere) {
        filieres.add(filiere);
        filiere.setUniversity(this);
    }

    public void removeFiliere(Filiere filiere) {
        filieres.remove(filiere);
        filiere.setUniversity(null);
    }
}