package com.norman.labo.repositories;

import com.norman.labo.entities.Facture;
import com.norman.labo.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FactureRepository extends JpaRepository<Facture,Long> {

    Facture findByIdFacture(Long idFacture);

    List<Facture> findAllByUtilisateur(Utilisateur utilisateur);
}
