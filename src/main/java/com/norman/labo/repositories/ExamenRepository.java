package com.norman.labo.repositories;


import com.norman.labo.entities.Examen;
import com.norman.labo.entities.Laboratoire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamenRepository extends JpaRepository<Examen,Long> {

    Examen findByIdExamen(Long idExamen);

    List<Examen> findAllByLaboratoireOrderByLibelle(Laboratoire laboratoire);

    List<Examen> findAllByLaboratoire_IdLaboratoireOrderByLibelle(Long idLabo);
}
