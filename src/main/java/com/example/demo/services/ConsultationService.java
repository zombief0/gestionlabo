package com.example.demo.services;

import com.example.demo.entities.Consultation;

import java.util.List;

public interface ConsultationService {

    void saveConsultation(String username, Long idPatient, String prescripteur);

    Long deleteConsultationById(Long idConsultation);

    Long terminerConsultation(Long idConsultation);

    Long activerConsultation(Long idConsultation);

    List<Consultation> fetchAllByIdPatient(Long idPersonne);
}
