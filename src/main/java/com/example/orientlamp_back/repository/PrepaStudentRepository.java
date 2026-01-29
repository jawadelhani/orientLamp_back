package com.example.orientlamp_back.repository;

import com.example.orientlamp_back.entity.PrepaStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PrepaStudentRepository extends JpaRepository<PrepaStudent, Long> {

    Optional<PrepaStudent> findByIdUser(Long idUser);

    List<PrepaStudent> findByPrepaMajor(String prepaMajor);

    List<PrepaStudent> findByAnneeBac(String anneeBac);

    @Query("SELECT p FROM PrepaStudent p WHERE p.cncRating >= :minRating")
    List<PrepaStudent> findByCncRatingGreaterThanEqual(@Param("minRating") BigDecimal minRating);

    @Query("SELECT p FROM PrepaStudent p WHERE p.prepaMajor = :prepaMajor AND p.cncRating >= :minRating")
    List<PrepaStudent> findByPrepaMajorAndCncRatingGreaterThanEqual(
            @Param("prepaMajor") String prepaMajor,
            @Param("minRating") BigDecimal minRating);

    @Query("SELECT p FROM PrepaStudent p WHERE p.cncRating BETWEEN :minRating AND :maxRating")
    List<PrepaStudent> findByCncRatingBetween(
            @Param("minRating") BigDecimal minRating,
            @Param("maxRating") BigDecimal maxRating);

    boolean existsByUser_IdUser(Long idUser);
}