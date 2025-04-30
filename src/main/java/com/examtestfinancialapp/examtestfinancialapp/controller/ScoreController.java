package com.examtestfinancialapp.examtestfinancialapp.controller;

import com.examtestfinancialapp.examtestfinancialapp.dto.ScoreDTO;
import com.examtestfinancialapp.examtestfinancialapp.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/scores")
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    @GetMapping
    public ResponseEntity<List<ScoreDTO>> getAllScores() {
        return ResponseEntity.ok(scoreService.getAllScores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScoreDTO> getScoreById(@PathVariable Long id) {
        return ResponseEntity.ok(scoreService.getScoreById(id));
    }

    @GetMapping("/utilisateur/{utilisateurId}")
    public ResponseEntity<List<ScoreDTO>> getScoresByUtilisateurId(@PathVariable Long utilisateurId) {
        return ResponseEntity.ok(scoreService.getScoresByUtilisateurId(utilisateurId));
    }

    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<List<ScoreDTO>> getScoresByQuizId(@PathVariable Long quizId) {
        return ResponseEntity.ok(scoreService.getScoresByQuizId(quizId));
    }

    @PostMapping
    public ResponseEntity<ScoreDTO> createScore(@Valid @RequestBody ScoreDTO scoreDTO) {
        return new ResponseEntity<>(scoreService.createOrUpdateScore(scoreDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScoreDTO> updateScore(@PathVariable Long id, @Valid @RequestBody ScoreDTO scoreDTO) {
        scoreDTO.setId(id);
        return ResponseEntity.ok(scoreService.createOrUpdateScore(scoreDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScore(@PathVariable Long id) {
        scoreService.deleteScore(id);
        return ResponseEntity.noContent().build();
    }

}
