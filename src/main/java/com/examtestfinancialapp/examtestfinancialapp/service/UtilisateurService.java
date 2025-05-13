package com.examtestfinancialapp.examtestfinancialapp.service;

import com.examtestfinancialapp.examtestfinancialapp.dto.UtilisateurCreationDTO;
import com.examtestfinancialapp.examtestfinancialapp.dto.UtilisateurDTO;
import com.examtestfinancialapp.examtestfinancialapp.exception.ResourceNotFoundException;
import com.examtestfinancialapp.examtestfinancialapp.model.Utilisateur;
import com.examtestfinancialapp.examtestfinancialapp.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UtilisateurDTO> getAllUtilisateurs() {
        return utilisateurRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UtilisateurDTO getUtilisateurById(Long id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID : " + id));
        return convertToDTO(utilisateur);
    }

    public UtilisateurDTO createUtilisateur(UtilisateurCreationDTO utilisateurCreationDTO) {
        String username = utilisateurCreationDTO.getUsername();
        String email = utilisateurCreationDTO.getEmail();

        if (username == null || username.trim().isEmpty()) {
            throw new RuntimeException("Le nom d'utilisateur ne peut pas être vide");
        }

        if (email == null || email.trim().isEmpty()) {
            throw new RuntimeException("L'email ne peut pas être vide");
        }

        // Vérifier si le username existe déjà (avec insensibilité à la casse)
        List<Utilisateur> existingUsersWithUsername = utilisateurRepository.findAll().stream()
                .filter(u -> u.getUsername() != null && u.getUsername().equalsIgnoreCase(username))
                .collect(Collectors.toList());

        if (!existingUsersWithUsername.isEmpty()) {
            throw new RuntimeException("Le nom d'utilisateur est déjà utilisé");
        }

        // Vérifier si l'email existe déjà (avec insensibilité à la casse)
        List<Utilisateur> existingUsersWithEmail = utilisateurRepository.findAll().stream()
                .filter(u -> u.getEmail() != null && u.getEmail().equalsIgnoreCase(email))
                .collect(Collectors.toList());

        if (!existingUsersWithEmail.isEmpty()) {
            throw new RuntimeException("L'email est déjà utilisé");
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(utilisateurCreationDTO.getNom());
        utilisateur.setUsername(username);
        utilisateur.setEmail(email);
        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateurCreationDTO.getMotDePasse()));

        // Attribution du rôle
        if (utilisateurCreationDTO.getRole() != null && "ROLE_ADMIN".equals(utilisateurCreationDTO.getRole())) {
            // Vérifier si l'utilisateur actuel est administrateur
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && !"anonymousUser".equals(auth.getPrincipal())) {
                Object principal = auth.getPrincipal();
                if (principal instanceof UserDetails) {
                    boolean isAdmin = ((UserDetails) principal).getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                    if (isAdmin) {
                        utilisateur.setRole("ROLE_ADMIN");
                    } else {
                        // Un utilisateur non-admin essaie de créer un admin
                        throw new RuntimeException("Vous n'avez pas l'autorisation de créer un compte administrateur");
                    }
                } else {
                    utilisateur.setRole("ROLE_USER");
                }
            } else {
                // Si nous sommes dans un contexte sans authentification (ex: création du
                // premier utilisateur)
                // Nous vérifions s'il existe déjà des administrateurs dans la base
                long adminCount = utilisateurRepository.findAll().stream()
                        .filter(u -> "ROLE_ADMIN".equals(u.getRole()))
                        .count();

                if (adminCount == 0) {
                    // Si aucun admin n'existe, on permet la création du premier admin
                    utilisateur.setRole("ROLE_ADMIN");
                } else {
                    // Sinon, on force le rôle USER pour les créations anonymes
                    utilisateur.setRole("ROLE_USER");
                }
            }
        } else {
            // Toujours attribuer le rôle USER par défaut
            utilisateur.setRole("ROLE_USER");
        }

        return convertToDTO(utilisateurRepository.save(utilisateur));
    }

    public UtilisateurDTO updateUtilisateur(Long id, UtilisateurCreationDTO utilisateurDTO) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID : " + id));

        String newUsername = utilisateurDTO.getUsername();
        String newEmail = utilisateurDTO.getEmail();

        // Vérifier que les valeurs fournies ne sont pas null ou vides
        if (newUsername == null || newUsername.trim().isEmpty()) {
            throw new RuntimeException("Le nom d'utilisateur ne peut pas être vide");
        }

        if (newEmail == null || newEmail.trim().isEmpty()) {
            throw new RuntimeException("L'email ne peut pas être vide");
        }

        String currentUsername = utilisateur.getUsername();
        String currentEmail = utilisateur.getEmail();

        // Vérifier si le username est déjà utilisé par un autre utilisateur
        if (!Objects.equals(currentUsername, newUsername)) {
            List<Utilisateur> existingUsersWithUsername = utilisateurRepository.findAll().stream()
                    .filter(u -> u.getUsername() != null && u.getUsername().equalsIgnoreCase(newUsername))
                    .collect(Collectors.toList());

            if (!existingUsersWithUsername.isEmpty() &&
                    existingUsersWithUsername.stream().anyMatch(u -> !u.getId().equals(id))) {
                throw new RuntimeException("Le nom d'utilisateur est déjà utilisé");
            }
        }

        // Vérifier si l'email est déjà utilisé par un autre utilisateur
        if (!Objects.equals(currentEmail, newEmail)) {
            List<Utilisateur> existingUsersWithEmail = utilisateurRepository.findAll().stream()
                    .filter(u -> u.getEmail() != null && u.getEmail().equalsIgnoreCase(newEmail))
                    .collect(Collectors.toList());

            if (!existingUsersWithEmail.isEmpty() &&
                    existingUsersWithEmail.stream().anyMatch(u -> !u.getId().equals(id))) {
                throw new RuntimeException("L'email est déjà utilisé");
            }
        }

        utilisateur.setNom(utilisateurDTO.getNom());
        utilisateur.setUsername(newUsername);
        utilisateur.setEmail(newEmail);

        // Mise à jour du mot de passe si fourni
        if (utilisateurDTO.getMotDePasse() != null && !utilisateurDTO.getMotDePasse().isEmpty()) {
            utilisateur.setMotDePasse(passwordEncoder.encode(utilisateurDTO.getMotDePasse()));
        }

        // Mise à jour du rôle si fourni
        if (utilisateurDTO.getRole() != null && !utilisateurDTO.getRole().isEmpty()) {
            // Vérifier si l'utilisateur authentifié est un admin
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                Object principal = auth.getPrincipal();
                if (principal instanceof UserDetails) {
                    boolean isAdmin = ((UserDetails) principal).getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                    if (isAdmin) {
                        // Un administrateur peut modifier n'importe quel rôle
                        utilisateur.setRole(utilisateurDTO.getRole());
                    } else if (!Objects.equals(utilisateur.getRole(), utilisateurDTO.getRole())) {
                        // Un utilisateur non-administrateur essaie de changer son propre rôle
                        throw new RuntimeException("Vous n'avez pas l'autorisation de modifier le rôle");
                    }
                }
            }
        }

        return convertToDTO(utilisateurRepository.save(utilisateur));
    }

    public void deleteUtilisateur(Long id) {
        // Seul un admin peut supprimer un utilisateur
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            boolean isAdmin = ((UserDetails) principal).getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            if (!isAdmin) {
                throw new RuntimeException("Vous n'avez pas l'autorisation de supprimer un utilisateur");
            }
        }

        if (!utilisateurRepository.existsById(id)) {
            throw new ResourceNotFoundException("Utilisateur non trouvé avec l'ID : " + id);
        }
        utilisateurRepository.deleteById(id);
    }

    private UtilisateurDTO convertToDTO(Utilisateur utilisateur) {
        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setId(utilisateur.getId());
        utilisateurDTO.setNom(utilisateur.getNom());
        utilisateurDTO.setUsername(utilisateur.getUsername());
        utilisateurDTO.setEmail(utilisateur.getEmail());
        utilisateurDTO.setRole(utilisateur.getRole());
        utilisateurDTO.setDateCreation(utilisateur.getDateCreation());
        return utilisateurDTO;
    }
}