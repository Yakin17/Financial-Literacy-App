package com.examtestfinancialapp.examtestfinancialapp.repository;

import com.examtestfinancialapp.examtestfinancialapp.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByEmail(String email);
    
    Optional<Utilisateur> findByUsername(String username);

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);
}