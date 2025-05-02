package com.examtestfinancialapp.examtestfinancialapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "utilisateurs",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        }   )
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String nom;

    @NotBlank
    @Size(min = 3, max = 20)
    private String username;


    @NotBlank
    @Email
    @Size(max = 150)
    @Column(unique = true)
    private String email;

    @NotBlank
    @Size(max = 255)
    private String motDePasse;

    @Column(length = 20)
    private String role = "ROLE_USER";

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @OneToMany(mappedBy = "auteur", cascade = CascadeType.ALL)
    private List<Article> articles;

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    private List<Score> scores;

    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
    }

    public Utilisateur(String username, String nom, String email, String motDePasse) {
        this.username = username;
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = "ROLE_USER"; // Par défaut, tous les nouveaux utilisateurs ont le rôle USER
    }

    public boolean isAdmin() {
        return "ROLE_ADMIN".equals(this.role);
    }

}
