package com.examtestfinancialapp.examtestfinancialapp.service;


import com.examtestfinancialapp.examtestfinancialapp.dto.QuizDTO;
import com.examtestfinancialapp.examtestfinancialapp.dto.ReponseDTO;
import com.examtestfinancialapp.examtestfinancialapp.exception.ResourceNotFoundException;
import com.examtestfinancialapp.examtestfinancialapp.model.Article;
import com.examtestfinancialapp.examtestfinancialapp.model.Quiz;
import com.examtestfinancialapp.examtestfinancialapp.model.Reponse;
import com.examtestfinancialapp.examtestfinancialapp.repository.ArticleRepository;
import com.examtestfinancialapp.examtestfinancialapp.repository.QuizRepository;
import com.examtestfinancialapp.examtestfinancialapp.repository.ReponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ReponseRepository reponseRepository;

    public List<QuizDTO> getAllQuizzes() {
        return quizRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public QuizDTO getQuizById(Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz non trouvé avec l'ID : " + id));
        return convertToDTO(quiz);
    }

    public List<QuizDTO> getQuizzesByArticleId(Long articleId) {
        return quizRepository.findByArticleId(articleId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    @Transactional
    public QuizDTO createQuiz(QuizDTO quizDTO) {
        Article article = articleRepository.findById(quizDTO.getArticleId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Article non trouvé avec l'ID : " + quizDTO.getArticleId()));

        Quiz quiz = new Quiz();
        quiz.setArticle(article);
        quiz.setQuestion(quizDTO.getQuestion());
        quiz.setReponseCorrecte(quizDTO.getReponseCorrecte());

        Quiz savedQuiz = quizRepository.save(quiz);

        // Sauvegarder les réponses associées si elles existent
        if (quizDTO.getReponses() != null && !quizDTO.getReponses().isEmpty()) {
            for (ReponseDTO reponseDTO : quizDTO.getReponses()) {
                Reponse reponse = new Reponse();
                reponse.setQuiz(savedQuiz);
                reponse.setContenu(reponseDTO.getContenu());
                reponseRepository.save(reponse);
            }
        }

        return convertToDTO(savedQuiz);
    }


    @Transactional
    public QuizDTO updateQuiz(Long id, QuizDTO quizDTO) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz non trouvé avec l'ID : " + id));

        // Si l'article a changé, on met à jour
        if (!quiz.getArticle().getId().equals(quizDTO.getArticleId())) {
            Article nouvelArticle = articleRepository.findById(quizDTO.getArticleId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Article non trouvé avec l'ID : " + quizDTO.getArticleId()));
            quiz.setArticle(nouvelArticle);
        }

        quiz.setQuestion(quizDTO.getQuestion());
        quiz.setReponseCorrecte(quizDTO.getReponseCorrecte());

        Quiz savedQuiz = quizRepository.save(quiz);

        // Mise à jour des réponses
        if (quizDTO.getReponses() != null) {
            // Supprimer les anciennes réponses
            reponseRepository.findByQuizId(id).forEach(reponse -> reponseRepository.delete(reponse));

            // Ajouter les nouvelles réponses
            for (ReponseDTO reponseDTO : quizDTO.getReponses()) {
                Reponse reponse = new Reponse();
                reponse.setQuiz(savedQuiz);
                reponse.setContenu(reponseDTO.getContenu());
                reponseRepository.save(reponse);
            }
        }

        return convertToDTO(savedQuiz);
    }


    public void deleteQuiz(Long id) {
        if (!quizRepository.existsById(id)) {
            throw new ResourceNotFoundException("Quiz non trouvé avec l'ID : " + id);
        }
        quizRepository.deleteById(id);
    }

    private QuizDTO convertToDTO(Quiz quiz) {
        QuizDTO quizDTO = new QuizDTO();
        quizDTO.setId(quiz.getId());
        quizDTO.setArticleId(quiz.getArticle().getId());
        quizDTO.setQuestion(quiz.getQuestion());
        quizDTO.setReponseCorrecte(quiz.getReponseCorrecte());

        // Récupérer les réponses associées
        if (quiz.getReponses() != null) {
            List<ReponseDTO> reponses = quiz.getReponses().stream()
                    .map(this::convertToReponseDTO)
                    .collect(Collectors.toList());
            quizDTO.setReponses(reponses);
        }

        return quizDTO;
    }

    private ReponseDTO convertToReponseDTO(Reponse reponse) {
        ReponseDTO reponseDTO = new ReponseDTO();
        reponseDTO.setId(reponse.getId());
        reponseDTO.setQuizId(reponse.getQuiz().getId());
        reponseDTO.setContenu(reponse.getContenu());
        return reponseDTO;
    }



}
