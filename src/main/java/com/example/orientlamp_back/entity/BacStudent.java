package com.example.orientlamp_back.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "bac_student")
@PrimaryKeyJoinColumn(name = "id_user")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BacStudent extends User {

    @Size(max = 100)
    @Column(name = "bac_major", length = 100)
    private String bacMajor;

    @Size(max = 100)
    @Column(name = "bac_degree", length = 100)
    private String bacDegree;

    @Column(name = "grade", precision = 5, scale = 2)
    private BigDecimal grade;

    @Size(max = 100)
    @Column(name = "subject_degree", length = 100)
    private String subjectDegree;

    @Column(name = "bac_year_graduation")
    private Integer bacYearGraduation;
}