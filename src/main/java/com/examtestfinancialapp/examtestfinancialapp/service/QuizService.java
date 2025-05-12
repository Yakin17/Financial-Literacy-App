package com.examtestfinancialapp.examtestfinancialapp.service;

import com.examtestfinancialapp.examtestfinancialapp.dto.QuizDTO;
import com.examtestfinancialapp.examtestfinancialapp.exception.ResourceNotFoundException;
import com.examtestfinancialapp.examtestfinancialapp.model.Article;
import com.examtestfinancialapp.examtestfinancialapp.model.Quiz;
import com.examtestfinancialapp.examtestfinancialapp.repository.ArticleRepository;
import com.examtestfinancialapp.examtestfinancialapp.repository.QuizRepository;
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
        quiz.setReponseInc1(quizDTO.getReponseInc1());
        quiz.setReponseInc2(quizDTO.getReponseInc2());
        quiz.setReponseInc3(quizDTO.getReponseInc3());

        Quiz savedQuiz = quizRepository.save(quiz);
        return convertToDTO(savedQuiz);
    }

    @Transactional
    public QuizDTO updateQuiz(Long id, QuizDTO quizDTO) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz non trouvé avec l'ID : " + id));

        if (!quiz.getArticle().getId().equals(quizDTO.getArticleId())) {
            Article nouvelArticle = articleRepository.findById(quizDTO.getArticleId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Article non trouvé avec l'ID : " + quizDTO.getArticleId()));
            quiz.setArticle(nouvelArticle);
        }

        quiz.setQuestion(quizDTO.getQuestion());
        quiz.setReponseCorrecte(quizDTO.getReponseCorrecte());
        quiz.setReponseInc1(quizDTO.getReponseInc1());
        quiz.setReponseInc2(quizDTO.getReponseInc2());
        quiz.setReponseInc3(quizDTO.getReponseInc3());

        Quiz savedQuiz = quizRepository.save(quiz);
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
        quizDTO.setReponseInc1(quiz.getReponseInc1());
        quizDTO.setReponseInc2(quiz.getReponseInc2());
        quizDTO.setReponseInc3(quiz.getReponseInc3());

        return quizDTO;
    }
}