package com.example.demo.services;

import com.example.demo.DTO.UtilisateurDTO;
import com.example.demo.entities.Utilisateur;

import java.util.List;

public interface UtilisateurService {

    List<Utilisateur> fetchAll();

    void save(UtilisateurDTO utilisateur);

    UtilisateurDTO fetchById(Long id);

    boolean updateAdmin(Long id, UtilisateurDTO utilisateur);

    void updateUser(Long id, UtilisateurDTO utilisateur);

    void deleteUser(Long id);

    void activateDeactivateUser(Long id);
}
