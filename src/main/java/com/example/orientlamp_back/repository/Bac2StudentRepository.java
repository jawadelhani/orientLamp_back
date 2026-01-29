package com.example.orientlamp_back.repository;

import com.example.orientlamp_back.entity.Bac2Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface Bac2StudentRepository extends JpaRepository<Bac2Student, Long> {

    Optional<Bac2Student> findByIdUser(Long idUser);

    List<Bac2Student> findByDiplomaType(String diplomaType);

    List<Bac2Student> findByBacMajor(String bacMajor);

    List<Bac2Student> findByInstitution(String institution);

    @Query("SELECT b FROM Bac2Student b WHERE " +
            "(b.avgS1 + b.avgS2 + b.avgS3 + b.avgS4) / 4.0 >= :minAvg")
    List<Bac2Student> findByOverallAverageGreaterThanEqual(@Param("minAvg") BigDecimal minAvg);

    @Query("SELECT b FROM Bac2Student b WHERE b.avgS1 >= :minAvg")
    List<Bac2Student> findByAvgS1GreaterThanEqual(@Param("minAvg") BigDecimal minAvg);

    @Query("SELECT b FROM Bac2Student b WHERE b.diplomaType = :diplomaType AND b.institution = :institution")
    List<Bac2Student> findByDiplomaTypeAndInstitution(
            @Param("diplomaType") String diplomaType,
            @Param("institution") String institution);

    @Query("SELECT b FROM Bac2Student b WHERE b.bacMajor = :bacMajor AND " +
            "(b.avgS1 + b.avgS2 + b.avgS3 + b.avgS4) / 4.0 >= :minAvg")
    List<Bac2Student> findByBacMajorAndOverallAverageGreaterThanEqual(
            @Param("bacMajor") String bacMajor,
            @Param("minAvg") BigDecimal minAvg);

    boolean existsByUser_IdUser(Long idUser);
}