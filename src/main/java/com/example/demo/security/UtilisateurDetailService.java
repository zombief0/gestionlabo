package com.example.demo.security;

import com.example.demo.entities.Utilisateur;
import com.example.demo.repositories.UtilisateurRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurDetailService implements UserDetailsService {

    private UtilisateurRepository utilisateurRepository;

    public UtilisateurDetailService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(s);
        if(utilisateur !=null) {
            return new UtilisateurDetail(utilisateur);
        }else {
            return null;
        }
    }
}
