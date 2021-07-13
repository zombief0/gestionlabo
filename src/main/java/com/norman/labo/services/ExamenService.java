package com.norman.labo.services;

import com.norman.labo.entities.Examen;
import com.norman.labo.entities.Laboratoire;

import java.util.List;

public interface ExamenService {

    List<Examen> getAllByLaboratoire(Long idLaboratoire);

    void saveExamen(Examen examen, Laboratoire laboratoire);

    Examen fetchExamById(Long id);

    void updateExam(Examen examen, Examen precedentValue);

    Long deleteExamenById(Long idExamen);

    List<Examen> fetchAll();
}
