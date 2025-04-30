package com.examtestfinancialapp.examtestfinancialapp.repository;

import com.examtestfinancialapp.examtestfinancialapp.model.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {
    List<Score> findByUtilisateurId(Long utilisateurId);

    List<Score> findByQuizId(Long quizId);

    Optional<Score> findByUtilisateurIdAndQuizId(Long utilisateurId, Long quizId);
}
