package com.norman.labo.repositories;

import com.norman.labo.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur,Long> {

    Utilisateur findByIdPersonne(Long idPersonne);

    Utilisateur findByEmail(String email);
}
