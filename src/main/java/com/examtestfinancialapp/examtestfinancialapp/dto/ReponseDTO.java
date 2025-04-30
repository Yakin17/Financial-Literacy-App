package com.examtestfinancialapp.examtestfinancialapp.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReponseDTO {
    private Long id;

    @NotNull(message = "L'ID du quiz est obligatoire")
    private Long quizId;

    @NotBlank(message = "Le contenu est obligatoire")
    @Size(max = 500, message = "Le contenu ne doit pas dépasser 500 caractères")
    private String contenu;

}
