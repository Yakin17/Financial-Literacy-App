package com.examtestfinancialapp.examtestfinancialapp.service;

import com.examtestfinancialapp.examtestfinancialapp.dto.ArticleDTO;
import com.examtestfinancialapp.examtestfinancialapp.exception.ResourceNotFoundException;
import com.examtestfinancialapp.examtestfinancialapp.model.Article;
import com.examtestfinancialapp.examtestfinancialapp.model.Utilisateur;
import com.examtestfinancialapp.examtestfinancialapp.repository.ArticleRepository;
import com.examtestfinancialapp.examtestfinancialapp.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    // Méthode pour vérifier si l'utilisateur connecté est admin
    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null &&
                authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    // Méthode pour vérifier si l'utilisateur connecté est admin
    private void verifierSiAdmin() {
        if (!isAdmin()) {
            throw new AccessDeniedException("Seuls les administrateurs peuvent accéder à cette ressource");
        }
    }

    public List<ArticleDTO> getAllArticles() {
        return articleRepository.findAllByOrderByDatePublicationDesc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ArticleDTO getArticleById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article non trouvé avec l'ID : " + id));
        return convertToDTO(article);
    }

    public List<ArticleDTO> getArticlesByAuteurId(Long auteurId) {
        return articleRepository.findByAuteurId(auteurId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ArticleDTO createArticle(ArticleDTO articleDTO) {
        // Vérifier les droits d'administration
        verifierSiAdmin();

        Utilisateur auteur = utilisateurRepository.findById(articleDTO.getAuteurId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Utilisateur non trouvé avec l'ID : " + articleDTO.getAuteurId()));

        Article article = new Article();
        article.setTitre(articleDTO.getTitre());
        article.setContenu(articleDTO.getContenu());
        article.setAuteur(auteur);

        return convertToDTO(articleRepository.save(article));
    }

    public ArticleDTO updateArticle(Long id, ArticleDTO articleDTO) {
        // Vérifier les droits d'administration
        verifierSiAdmin();

        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article non trouvé avec l'ID : " + id));

        article.setTitre(articleDTO.getTitre());
        article.setContenu(articleDTO.getContenu());

        // Si l'auteur a changé, on met à jour
        if (!article.getAuteur().getId().equals(articleDTO.getAuteurId())) {
            Utilisateur nouvelAuteur = utilisateurRepository.findById(articleDTO.getAuteurId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Utilisateur non trouvé avec l'ID : " + articleDTO.getAuteurId()));
            article.setAuteur(nouvelAuteur);
        }

        return convertToDTO(articleRepository.save(article));
    }

    public void deleteArticle(Long id) {
        // Vérifier les droits d'administration
        verifierSiAdmin();

        if (!articleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Article non trouvé avec l'ID : " + id);
        }
        articleRepository.deleteById(id);
    }

    private ArticleDTO convertToDTO(Article article) {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(article.getId());
        articleDTO.setTitre(article.getTitre());
        articleDTO.setContenu(article.getContenu());
        articleDTO.setDatePublication(article.getDatePublication());
        articleDTO.setAuteurId(article.getAuteur().getId());
        articleDTO.setAuteurNom(article.getAuteur().getNom());
        return articleDTO;
    }
}