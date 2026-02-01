package com.example.orientlamp_back.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "prepa_student")
@PrimaryKeyJoinColumn(name = "id_user")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PrepaStudent extends User {

    @Size(max = 100)
    @Column(name = "prepa_major", length = 100)
    private String prepaMajor;

    @Column(name = "cnc_rating", precision = 5, scale = 2)
    private BigDecimal cncRating;

    @Lob
    @Column(name = "notes_trimestre")
    private String notesTrimestre;

    @Size(max = 20)
    @Column(name = "annee_bac", length = 20)
    private String anneeBac;
}