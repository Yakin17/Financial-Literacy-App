package com.examtestfinancialapp.examtestfinancialapp.security;


import com.examtestfinancialapp.examtestfinancialapp.model.Utilisateur;
import com.examtestfinancialapp.examtestfinancialapp.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UtilisateurRepository utilisateurRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Essayer de trouver l'utilisateur par username
        Utilisateur utilisateur = utilisateurRepository.findByUsername(username)
                .orElse(null);

        // Si non trouvé par username, essayer par email
        if (utilisateur == null) {
            utilisateur = utilisateurRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException(
                            "Utilisateur non trouvé avec le nom d'utilisateur ou l'email : " + username));
        }

        return UserDetailsImpl.build(utilisateur);
    }
}
