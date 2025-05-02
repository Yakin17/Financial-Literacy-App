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
        // Vérifier si le username existe déjà
        if (utilisateurRepository.existsByUsername(utilisateurCreationDTO.getUsername())) {
            throw new RuntimeException("Le nom d'utilisateur est déjà utilisé");
        }

        // Vérifier si l'email existe déjà
        if (utilisateurRepository.existsByEmail(utilisateurCreationDTO.getEmail())) {
            throw new RuntimeException("L'email est déjà utilisé");
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(utilisateurCreationDTO.getNom());
        utilisateur.setUsername(utilisateurCreationDTO.getUsername());
        utilisateur.setEmail(utilisateurCreationDTO.getEmail());
        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateurCreationDTO.getMotDePasse()));

        // Attribution du rôle (modification ici)
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

        // Vérifier si le username est déjà utilisé par un autre utilisateur
        if (!utilisateur.getUsername().equals(utilisateurDTO.getUsername()) &&
                utilisateurRepository.existsByUsername(utilisateurDTO.getUsername())) {
            throw new RuntimeException("Le nom d'utilisateur est déjà utilisé");
        }

        // Vérifier si l'email est déjà utilisé par un autre utilisateur
        if (!utilisateur.getEmail().equals(utilisateurDTO.getEmail()) &&
                utilisateurRepository.existsByEmail(utilisateurDTO.getEmail())) {
            throw new RuntimeException("L'email est déjà utilisé");
        }

        utilisateur.setNom(utilisateurDTO.getNom());
        utilisateur.setUsername(utilisateurDTO.getUsername());
        utilisateur.setEmail(utilisateurDTO.getEmail());

        // Mise à jour du mot de passe si fourni
        if (utilisateurDTO.getMotDePasse() != null && !utilisateurDTO.getMotDePasse().isEmpty()) {
            utilisateur.setMotDePasse(passwordEncoder.encode(utilisateurDTO.getMotDePasse()));
        }

        // Mise à jour du rôle si fourni (modification ici)
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
                    } else if (!utilisateur.getRole().equals(utilisateurDTO.getRole())) {
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