package com.example.demo.services;


import com.example.demo.entities.Laboratoire;

import java.util.List;

public interface LaboratoireService {
    List<Laboratoire> fetchAllLaboratoire();

    void saveLaboratoire(Laboratoire laboratoire);

    Laboratoire fetchById(Long id);

    void updateLaboratoire(Long idLabo, Laboratoire laboratoire);

    void deleteLaboratoire(Long id);
}
