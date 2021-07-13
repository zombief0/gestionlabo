package com.norman.labo.services;

import com.norman.labo.entities.ExamenSouscrit;
import com.norman.labo.entities.Facture;

import java.util.List;

public interface FactureService {
    Facture findByCode(Long code);

    Long saveFacture(String username, ExamenSouscrit examenSouscrit, Long codeFacture);

    List<Facture> findAll();

    void delete(Long codeFacture);
}
