package com.example.demo.repositories;


import com.example.demo.entities.Examen;
import com.example.demo.entities.Laboratoire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamenRepository extends JpaRepository<Examen,Long> {

    Examen findByIdExamen(Long idExamen);

    List<Examen> findAllByLaboratoireOrderByLibelle(Laboratoire laboratoire);

    List<Examen> findAllByLaboratoire_IdLaboratoireOrderByLibelle(Long idLabo);
}
