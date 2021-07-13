package com.norman.labo.services;

import com.norman.labo.DTO.UtilisateurDTO;
import com.norman.labo.entities.Utilisateur;

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
