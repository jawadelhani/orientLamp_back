package com.example.orientlamp_back.repository;

import com.example.orientlamp_back.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UniversityRepository extends JpaRepository<University, Long> {

    Optional<University> findByName(String name);

    List<University> findByLocation(String location);

    List<University> findByType(String type);

    List<University> findByAccreditationStatus(String accreditationStatus);

    boolean existsByName(String name);
}