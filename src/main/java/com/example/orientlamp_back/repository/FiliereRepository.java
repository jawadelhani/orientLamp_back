package com.example.orientlamp_back.repository;

import com.example.orientlamp_back.entity.Filiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FiliereRepository extends JpaRepository<Filiere, Long> {

    List<Filiere> findByUniversityId(Long universityId);

    List<Filiere> findByAdmissionType(String admissionType);

    List<Filiere> findByLanguage(String language);

    @Query("SELECT f FROM Filiere f WHERE f.applicationDeadline >= :date")
    List<Filiere> findByApplicationDeadlineAfter(@Param("date") LocalDate date);

    @Query("SELECT f FROM Filiere f WHERE f.university.id = :universityId AND f.admissionType = :admissionType")
    List<Filiere> findByUniversityIdAndAdmissionType(
            @Param("universityId") Long universityId,
            @Param("admissionType") String admissionType);

    @Query("SELECT f FROM Filiere f WHERE f.seatsAvailabial > 0")
    List<Filiere> findAvailableFilieres();

    boolean existsByNameAndUniversityId(String name, Long universityId);
}