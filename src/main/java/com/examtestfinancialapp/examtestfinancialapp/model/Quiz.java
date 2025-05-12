package com.examtestfinancialapp.examtestfinancialapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "quizzes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @NotBlank
    @Size(max = 500)
    @Column(nullable = false)
    private String question;

    @NotBlank
    @Size(max = 500)
    @Column(name = "reponse_correcte", nullable = false)
    private String reponseCorrecte;

    @NotBlank
    @Size(max = 500)
    @Column(name = "reponse_inc1", nullable = false)
    private String reponseInc1;

    @NotBlank
    @Size(max = 500)
    @Column(name = "reponse_inc2", nullable = false)
    private String reponseInc2;

    @NotBlank
    @Size(max = 500)
    @Column(name = "reponse_inc3", nullable = false)
    private String reponseInc3;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private List<Score> scores;
}