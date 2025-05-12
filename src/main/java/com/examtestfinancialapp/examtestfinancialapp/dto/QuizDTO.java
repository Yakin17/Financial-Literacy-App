package com.examtestfinancialapp.examtestfinancialapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizDTO {

    private Long id;

    @NotNull(message = "L'ID de l'article est obligatoire")
    private Long articleId;

    @NotBlank(message = "La question est obligatoire")
    @Size(max = 500, message = "La question ne doit pas dépasser 500 caractères")
    private String question;

    @NotBlank(message = "La réponse correcte est obligatoire")
    @Size(max = 500, message = "La réponse correcte ne doit pas dépasser 500 caractères")
    private String reponseCorrecte;

    @NotBlank(message = "La première réponse incorrecte est obligatoire")
    @Size(max = 500, message = "La réponse incorrecte ne doit pas dépasser 500 caractères")
    private String reponseInc1;

    @NotBlank(message = "La deuxième réponse incorrecte est obligatoire")
    @Size(max = 500, message = "La réponse incorrecte ne doit pas dépasser 500 caractères")
    private String reponseInc2;

    @NotBlank(message = "La troisième réponse incorrecte est obligatoire")
    @Size(max = 500, message = "La réponse incorrecte ne doit pas dépasser 500 caractères")
    private String reponseInc3;
}