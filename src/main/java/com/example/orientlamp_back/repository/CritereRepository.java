package com.example.orientlamp_back.repository;


import com.example.orientlamp_back.entity.Critere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CritereRepository extends JpaRepository<Critere, Long> {
}