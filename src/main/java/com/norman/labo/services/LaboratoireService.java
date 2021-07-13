package com.norman.labo.services;


import com.norman.labo.entities.Laboratoire;

import java.util.List;

public interface LaboratoireService {
    List<Laboratoire> fetchAllLaboratoire();

    void saveLaboratoire(Laboratoire laboratoire);

    Laboratoire fetchById(Long id);

    void updateLaboratoire(Long idLabo, Laboratoire laboratoire);

    void deleteLaboratoire(Long id);
}
