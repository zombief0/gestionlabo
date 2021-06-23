package com.example.demo.services;

import com.example.demo.entities.Examen;
import com.example.demo.entities.ExamenSouscrit;

public interface ExamenSouscritService {
    ExamenSouscrit fetchById(Long id);

    Long updateExamenSouscrit(Long idExamenSouscrit,
                              Double valeurNormale,
                              String unite);

    Long saveExamSouscrit(Long idConsultation, Examen examen);

    Long deleteById(Long idExamenSouscrit);
}
