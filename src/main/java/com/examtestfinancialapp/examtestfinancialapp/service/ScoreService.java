package com.examtestfinancialapp.examtestfinancialapp.service;

import com.examtestfinancialapp.examtestfinancialapp.dto.ScoreDTO;
import com.examtestfinancialapp.examtestfinancialapp.exception.ResourceNotFoundException;
import com.examtestfinancialapp.examtestfinancialapp.model.Quiz;
import com.examtestfinancialapp.examtestfinancialapp.model.Score;
import com.examtestfinancialapp.examtestfinancialapp.model.Utilisateur;
import com.examtestfinancialapp.examtestfinancialapp.repository.QuizRepository;
import com.examtestfinancialapp.examtestfinancialapp.repository.ScoreRepository;
import com.examtestfinancialapp.examtestfinancialapp.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScoreService {

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private QuizRepository quizRepository;

    public List<ScoreDTO> getAllScores() {
        return scoreRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ScoreDTO getScoreById(Long id) {
        Score score = scoreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Score non trouvé avec l'ID : " + id));
        return convertToDTO(score);
    }

    public List<ScoreDTO> getScoresByUtilisateurId(Long utilisateurId) {
        return scoreRepository.findByUtilisateurId(utilisateurId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ScoreDTO> getScoresByQuizId(Long quizId) {
        return scoreRepository.findByQuizId(quizId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ScoreDTO createOrUpdateScore(ScoreDTO scoreDTO) {
        Utilisateur utilisateur = utilisateurRepository.findById(scoreDTO.getUtilisateurId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Utilisateur non trouvé avec l'ID : " + scoreDTO.getUtilisateurId()));

        Quiz quiz = quizRepository.findById(scoreDTO.getQuizId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Quiz non trouvé avec l'ID : " + scoreDTO.getQuizId()));

        // Vérifier si un score existe déjà pour cet utilisateur et ce quiz
        Optional<Score> existingScore = scoreRepository.findByUtilisateurIdAndQuizId(
                scoreDTO.getUtilisateurId(), scoreDTO.getQuizId());

        Score score;
        if (existingScore.isPresent()) {
            // Mettre à jour le score existant
            score = existingScore.get();
            score.setPointsObtenus(scoreDTO.getPointsObtenus());
        } else {
            // Créer un nouveau score
            score = new Score();
            score.setUtilisateur(utilisateur);
            score.setQuiz(quiz);
            score.setPointsObtenus(scoreDTO.getPointsObtenus());
        }

        return convertToDTO(scoreRepository.save(score));
    }

    public void deleteScore(Long id) {
        if (!scoreRepository.existsById(id)) {
            throw new ResourceNotFoundException("Score non trouvé avec l'ID : " + id);
        }
        scoreRepository.deleteById(id);
    }

    /**
     * Vérifie si un utilisateur a déjà passé un quiz spécifique
     * 
     * @param utilisateurId ID de l'utilisateur
     * @param quizId        ID du quiz
     * @return true si l'utilisateur a déjà passé le quiz, false sinon
     */
    public boolean hasUserCompletedQuiz(Long utilisateurId, Long quizId) {
        Optional<Score> score = scoreRepository.findByUtilisateurIdAndQuizId(utilisateurId, quizId);
        return score.isPresent();
    }

    /**
     * Enregistre le score d'un utilisateur pour un quiz spécifique
     * 
     * @param utilisateurId ID de l'utilisateur
     * @param quizId        ID du quiz
     * @param pointsObtenus Nombre de points obtenus
     * @return Le score enregistré
     */
    public ScoreDTO saveUserQuizScore(Long utilisateurId, Long quizId, int pointsObtenus) {
        ScoreDTO scoreDTO = new ScoreDTO();
        scoreDTO.setUtilisateurId(utilisateurId);
        scoreDTO.setQuizId(quizId);
        scoreDTO.setPointsObtenus(pointsObtenus);

        return createOrUpdateScore(scoreDTO);
    }

    private ScoreDTO convertToDTO(Score score) {
        ScoreDTO scoreDTO = new ScoreDTO();
        scoreDTO.setId(score.getId());
        scoreDTO.setUtilisateurId(score.getUtilisateur().getId());
        scoreDTO.setQuizId(score.getQuiz().getId());
        scoreDTO.setPointsObtenus(score.getPointsObtenus());
        scoreDTO.setDatePassage(score.getDatePassage());
        return scoreDTO;
    }
}