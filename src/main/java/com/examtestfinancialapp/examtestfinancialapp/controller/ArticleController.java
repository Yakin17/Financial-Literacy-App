package com.examtestfinancialapp.examtestfinancialapp.controller;

import com.examtestfinancialapp.examtestfinancialapp.dto.ArticleDTO;
import com.examtestfinancialapp.examtestfinancialapp.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping
    public ResponseEntity<List<ArticleDTO>> getAllArticles() {
        return ResponseEntity.ok(articleService.getAllArticles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleDTO> getArticleById(@PathVariable Long id) {
        return ResponseEntity.ok(articleService.getArticleById(id));
    }

    @GetMapping("/auteur/{auteurId}")
    public ResponseEntity<List<ArticleDTO>> getArticlesByAuteurId(@PathVariable Long auteurId) {
        return ResponseEntity.ok(articleService.getArticlesByAuteurId(auteurId));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ArticleDTO> createArticle(@Valid @RequestBody ArticleDTO articleDTO) {
        return new ResponseEntity<>(articleService.createArticle(articleDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ArticleDTO> updateArticle(@PathVariable Long id, @Valid @RequestBody ArticleDTO articleDTO) {
        return ResponseEntity.ok(articleService.updateArticle(id, articleDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }

    // Gestionnaire d'erreur pour les problèmes de parsing JSON
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Format JSON invalide");
        errorResponse.put("details",
                "Vérifiez que votre JSON est bien formé et que les caractères spéciaux sont correctement échappés");
        errorResponse.put("message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}