package com.example.demo.services;

import com.example.demo.entities.Examen;
import com.example.demo.entities.Laboratoire;

import java.util.List;

public interface ExamenService {

    List<Examen> getAllByLaboratoire(Long idLaboratoire);

    void saveExamen(Examen examen, Laboratoire laboratoire);

    Examen fetchExamById(Long id);

    void updateExam(Examen examen, Examen precedentValue);

    Long deleteExamenById(Long idExamen);
}
