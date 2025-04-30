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

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
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
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getMotDePasse()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponseDTO(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail()));
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UtilisateurCreationDTO utilisateurCreationDTO) {
        if (utilisateurRepository.existsByEmail(utilisateurCreationDTO.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body("Erreur: Email déjà utilisé!");
        }

        UtilisateurDTO newUser = utilisateurService.createUtilisateur(utilisateurCreationDTO);

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }




}
