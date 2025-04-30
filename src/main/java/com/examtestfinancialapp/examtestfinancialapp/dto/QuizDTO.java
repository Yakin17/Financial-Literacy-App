package com.examtestfinancialapp.examtestfinancialapp.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

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

    private List<ReponseDTO> reponses;

}
