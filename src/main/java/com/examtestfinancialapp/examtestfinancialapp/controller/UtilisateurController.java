package com.examtestfinancialapp.examtestfinancialapp.controller;

import com.examtestfinancialapp.examtestfinancialapp.dto.UtilisateurCreationDTO;
import com.examtestfinancialapp.examtestfinancialapp.dto.UtilisateurDTO;
import com.examtestfinancialapp.examtestfinancialapp.dto.UtilisateurUpdateRequest;
import com.examtestfinancialapp.examtestfinancialapp.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.examtestfinancialapp.examtestfinancialapp.dto.UtilisateurUpdateRequest;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    @GetMapping
    public ResponseEntity<List<UtilisateurDTO>> getAllUtilisateurs() {
        return ResponseEntity.ok(utilisateurService.getAllUtilisateurs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UtilisateurDTO> getUtilisateurById(@PathVariable Long id) {
        return ResponseEntity.ok(utilisateurService.getUtilisateurById(id));
    }

    @PostMapping
    public ResponseEntity<UtilisateurDTO> createUtilisateur(@Valid @RequestBody UtilisateurCreationDTO utilisateurDTO) {
        // Par défaut, attribuer le rôle USER si aucun rôle n'est spécifié
        if (utilisateurDTO.getRole() == null || utilisateurDTO.getRole().isEmpty()) {
            utilisateurDTO.setRole("ROLE_USER");
        }

        return new ResponseEntity<>(utilisateurService.createUtilisateur(utilisateurDTO), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UtilisateurDTO> updateUtilisateur(@PathVariable Long id,
            @Valid @RequestBody UtilisateurUpdateRequest utilisateurUpdateRequest) {
        return ResponseEntity.ok(utilisateurService.updateUtilisateur(id, utilisateurUpdateRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUtilisateur(@PathVariable Long id) {
        utilisateurService.deleteUtilisateur(id);
        return ResponseEntity.noContent().build();
    }
}