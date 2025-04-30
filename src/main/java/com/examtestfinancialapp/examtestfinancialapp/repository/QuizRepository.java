package com.examtestfinancialapp.examtestfinancialapp.repository;

import com.examtestfinancialapp.examtestfinancialapp.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByArticleId(Long articleId);
}
