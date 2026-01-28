package com.example.orientlamp_back.repository;

import com.example.orientlamp_back.entity.Preference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PreferenceRepository extends JpaRepository<Preference, Long> {

    Optional<Preference> findByUser_IdUser(Long userId);

    List<Preference> findByBudgetRange(String budgetRange);

    @Query("SELECT p FROM Preference p WHERE p.desiredCitiest LIKE %:city%")
    List<Preference> findByDesiredCitiesContaining(@Param("city") String city);

    @Query("SELECT p FROM Preference p WHERE p.interests LIKE %:interest%")
    List<Preference> findByInterestsContaining(@Param("interest") String interest);

    @Query("SELECT p FROM Preference p WHERE p.languagePreferences LIKE %:language%")
    List<Preference> findByLanguagePreferencesContaining(@Param("language") String language);

    boolean existsByUser_IdUser(Long userId);

    void deleteByUser_IdUser(Long userId);
}