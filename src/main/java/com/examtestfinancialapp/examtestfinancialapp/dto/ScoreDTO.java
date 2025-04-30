package com.examtestfinancialapp.examtestfinancialapp.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreDTO {

    private Long id;

    @NotNull(message = "L'ID de l'utilisateur est obligatoire")
    private Long utilisateurId;

    @NotNull(message = "L'ID du quiz est obligatoire")
    private Long quizId;

    @NotNull(message = "Les points obtenus sont obligatoires")
    @Min(value = 0, message = "Les points ne peuvent pas être négatifs")
    @Max(value = 10, message = "Les points ne peuvent pas dépasser 10")
    private Integer pointsObtenus;

    private LocalDateTime datePassage;

}
