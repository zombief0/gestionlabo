package com.example.demo.repositories;

import com.example.demo.entities.Facture;
import com.example.demo.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FactureRepository extends JpaRepository<Facture,Long> {

    Facture findByIdFacture(Long idFacture);

    List<Facture> findAllByUtilisateur(Utilisateur utilisateur);
}
