package com.examtestfinancialapp.examtestfinancialapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDTO {

    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    @Size(max = 255, message = "Le titre ne doit pas dépasser 255 caractères")
    private String titre;

    @NotBlank(message = "Le contenu est obligatoire")
    private String contenu;

    private LocalDateTime datePublication;

    @NotNull(message = "L'ID de l'auteur est obligatoire")
    private Long auteurId;

    private String auteurNom;

}