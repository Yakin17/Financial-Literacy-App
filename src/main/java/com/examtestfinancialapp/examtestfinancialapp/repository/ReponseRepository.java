package com.examtestfinancialapp.examtestfinancialapp.repository;

import com.examtestfinancialapp.examtestfinancialapp.model.Reponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReponseRepository extends JpaRepository<Reponse, Long> {
    List<Reponse> findByQuizId(Long quizId);
}
