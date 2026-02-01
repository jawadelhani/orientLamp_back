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
@Table(name = "bac2_student")
@PrimaryKeyJoinColumn(name = "id_user")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Bac2Student extends User {

    @Size(max = 100)
    @Column(name = "diploma_type", length = 100)
    private String diplomaType;

    @Size(max = 100)
    @Column(name = "bac_major", length = 100)
    private String bacMajor;

    @Size(max = 255)
    @Column(name = "institution")
    private String institution;

    @Column(name = "avg_s1", precision = 5, scale = 2)
    private BigDecimal avgS1;

    @Column(name = "avg_s2", precision = 5, scale = 2)
    private BigDecimal avgS2;

    @Column(name = "avg_s3", precision = 5, scale = 2)
    private BigDecimal avgS3;

    @Column(name = "avg_s4", precision = 5, scale = 2)
    private BigDecimal avgS4;
}