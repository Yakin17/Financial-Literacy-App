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
    private String username;
    private String nom;
    private String email;
    private String role;

    public JwtResponseDTO(String token, Long id, String username, String nom, String email, String role) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.nom = nom;
        this.email = email;
        this.role = role;
    }

}
