package com.example.demo.repositories;


import com.example.demo.entities.Examen;
import com.example.demo.entities.TypeExamen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamenRepository extends JpaRepository<Examen,Long> {

    Examen findByIdExamen(Long idExamen);

    List<Examen> findAllByTypeExamen(TypeExamen typeExamen);
}
