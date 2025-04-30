package com.examtestfinancialapp.examtestfinancialapp.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponseDTO {

    private String token;
    private String type = "Bearer";
    private Long id;
    private String nom;
    private String email;

    public JwtResponseDTO(String token, Long id, String nom, String email) {
        this.token = token;
        this.id = id;
        this.nom = nom;
        this.email = email;
    }

}
