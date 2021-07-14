package com.norman.labo.services;

import com.norman.labo.entities.Examen;
import com.norman.labo.entities.ExamenSouscrit;

import java.util.List;

public interface ExamenSouscritService {
    ExamenSouscrit fetchById(Long id);

    Long updateExamenSouscrit(Long idExamenSouscrit,
                              Double valeurNormale,
                              String unite);

    Long saveExamSouscrit(Long idConsultation, Examen examen);

    Long deleteById(Long idExamenSouscrit);

    List<ExamenSouscrit> findAllByPatientAndFactureNull(Long idPatient);
}
