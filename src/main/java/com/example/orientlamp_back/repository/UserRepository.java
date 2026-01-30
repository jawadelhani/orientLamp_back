package com.example.orientlamp_back.repository;

import com.example.orientlamp_back.entity.CurrentStudyLevel;
import com.example.orientlamp_back.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByEnabled(boolean enabled);

    @Query("SELECT u FROM User u WHERE u.age >= :minAge AND u.age <= :maxAge")
    List<User> findByAgeBetween(@Param("minAge") Integer minAge, @Param("maxAge") Integer maxAge);

    List<User> findByCurrentStudyLevel(CurrentStudyLevel currentStudyLevel);

    boolean existsByEmail(String email);
}