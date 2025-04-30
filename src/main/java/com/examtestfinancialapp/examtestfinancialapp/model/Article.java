package com.examtestfinancialapp.examtestfinancialapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "articles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 255)
    private String titre;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String contenu;

    @Column(name = "date_publication")
    private LocalDateTime datePublication;

    @ManyToOne
    @JoinColumn(name = "auteur_id", nullable = false)
    private Utilisateur auteur;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<Quiz> quizzes;

    @PrePersist
    protected void onCreate() {
        datePublication = LocalDateTime.now();
    }

}
