package com.example.orientlamp_back.repository;

import com.example.orientlamp_back.entity.BacStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface BacStudentRepository extends JpaRepository<BacStudent, Long> {

    Optional<BacStudent> findByIdUser(Long idUser);

    List<BacStudent> findByBacMajor(String bacMajor);

    List<BacStudent> findByBacDegree(String bacDegree);

    List<BacStudent> findByBacYearGraduation(Integer year);

    @Query("SELECT b FROM BacStudent b WHERE b.grade >= :minGrade")
    List<BacStudent> findByGradeGreaterThanEqual(@Param("minGrade") BigDecimal minGrade);

    @Query("SELECT b FROM BacStudent b WHERE b.bacMajor = :bacMajor AND b.grade >= :minGrade")
    List<BacStudent> findByBacMajorAndGradeGreaterThanEqual(
            @Param("bacMajor") String bacMajor,
            @Param("minGrade") BigDecimal minGrade);

    boolean existsByIdUser(Long idUser);
}