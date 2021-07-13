package com.norman.labo.repositories;


import com.norman.labo.entities.Examen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamenRepository extends JpaRepository<Examen,Long> {


    List<Examen> findAllByLaboratoire_IdLaboratoireOrderByLibelle(Long idLabo);
}
