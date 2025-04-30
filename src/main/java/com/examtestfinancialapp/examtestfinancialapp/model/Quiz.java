package com.examtestfinancialapp.examtestfinancialapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    private String question;

    @NotBlank
    @Size(max = 500)
    @Column(name = "reponse_correcte")
    private String reponseCorrecte;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private List<Reponse> reponses;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private List<Score> scores;


}
