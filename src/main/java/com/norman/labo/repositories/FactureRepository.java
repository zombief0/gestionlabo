package com.norman.labo.repositories;

import com.norman.labo.entities.Facture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FactureRepository extends JpaRepository<Facture,Long> {

    Facture findByIdFacture(Long idFacture);

}
