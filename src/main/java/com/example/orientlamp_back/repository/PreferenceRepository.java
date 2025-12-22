package com.example.orientlamp_back.repository;

import com.example.orientlamp_back.entity.Preference;
import com.example.orientlamp_back.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PreferenceRepository extends JpaRepository<Preference, Long> {
    Optional<Preference> findByUser(User user);
    Optional<Preference> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}