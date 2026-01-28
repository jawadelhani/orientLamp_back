package com.example.orientlamp_back.repository;

import com.example.orientlamp_back.entity.Critere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CritereRepository extends JpaRepository<Critere, Long> {

    Optional<Critere> findByFiliereId(Long filiereId);

    List<Critere> findByAnneeAcademique(String anneeAcademique);

    List<Critere> findByTypeCandidat(String typeCandidat);

    List<Critere> findBySerieBacCible(String serieBacCible);

    @Query("SELECT c FROM Critere c WHERE c.seuilCalcul >= :minSeuil")
    List<Critere> findBySeuilCalculGreaterThanEqual(@Param("minSeuil") BigDecimal minSeuil);

    @Query("SELECT c FROM Critere c WHERE c.aEntretien = true")
    List<Critere> findWithEntretien();

    @Query("SELECT c FROM Critere c WHERE c.ageMax IS NOT NULL AND c.ageMax <= :age")
    List<Critere> findByAgeMaxLessThanEqual(@Param("age") Integer age);

    boolean existsByFiliereId(Long filiereId);
}