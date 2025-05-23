package com.examtestfinancialapp.examtestfinancialapp.repository;
import com.examtestfinancialapp.examtestfinancialapp.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByAuteurId(Long auteurId);

    List<Article> findAllByOrderByDatePublicationDesc();
}