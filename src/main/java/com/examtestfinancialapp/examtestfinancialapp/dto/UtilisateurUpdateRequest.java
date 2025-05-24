package com.examtestfinancialapp.examtestfinancialapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UtilisateurUpdateRequest {

    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères")
    private String nom;

    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    @Size(min = 3, max = 30, message = "Le nom d'utilisateur doit contenir entre 3 et 30 caractères")
    private String username;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;

    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String motDePasse; // Optionnel pour la mise à jour

    private String role;

    // Constructeur par défaut
    public UtilisateurUpdateRequest() {
    }

    // Constructeur avec paramètres
    public UtilisateurUpdateRequest(String nom, String username, String email, String motDePasse, String role) {
        this.nom = nom;
        this.username = username;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
    }

    // Getters
    public String getNom() {
        return nom;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public String getRole() {
        return role;
    }

    // Setters
    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UtilisateurUpdateRequest{" +
                "nom='" + nom + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", motDePasse='" + (motDePasse != null ? "[PROTECTED]" : "null") + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}