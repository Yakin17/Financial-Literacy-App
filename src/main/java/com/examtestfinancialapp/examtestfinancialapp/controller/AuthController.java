package com.examtestfinancialapp.examtestfinancialapp.controller;

import com.examtestfinancialapp.examtestfinancialapp.dto.JwtResponseDTO;
import com.examtestfinancialapp.examtestfinancialapp.dto.LoginDTO;
import com.examtestfinancialapp.examtestfinancialapp.dto.UtilisateurCreationDTO;
import com.examtestfinancialapp.examtestfinancialapp.dto.UtilisateurDTO;
import com.examtestfinancialapp.examtestfinancialapp.repository.UtilisateurRepository;
import com.examtestfinancialapp.examtestfinancialapp.security.JwtUtils;
import com.examtestfinancialapp.examtestfinancialapp.service.UtilisateurService;
import com.examtestfinancialapp.examtestfinancialapp.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UtilisateurService utilisateurService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDTO loginDTO) {
        // L'authentification peut se faire par username ou email
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getMotDePasse()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(item -> item.getAuthority())
                .orElse("ROLE_USER");

        return ResponseEntity.ok(new JwtResponseDTO(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getNom(),
                userDetails.getEmail(),
                role));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UtilisateurCreationDTO utilisateurCreationDTO) {
        if (utilisateurRepository.existsByUsername(utilisateurCreationDTO.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body("Erreur: Nom d'utilisateur déjà utilisé!");
        }

        if (utilisateurRepository.existsByEmail(utilisateurCreationDTO.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body("Erreur: Email déjà utilisé!");
        }

        // Par défaut, attribuer le rôle USER si aucun rôle n'est spécifié
        if (utilisateurCreationDTO.getRole() == null || utilisateurCreationDTO.getRole().isEmpty()) {
            utilisateurCreationDTO.setRole("ROLE_USER");
        }

        UtilisateurDTO newUser = utilisateurService.createUtilisateur(utilisateurCreationDTO);

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }
}