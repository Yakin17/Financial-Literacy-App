package com.examtestfinancialapp.examtestfinancialapp.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurDTO {

    private Long id;
    private String nom;
    private String username;
    private String email;
    private String role;
    private LocalDateTime dateCreation;

}
