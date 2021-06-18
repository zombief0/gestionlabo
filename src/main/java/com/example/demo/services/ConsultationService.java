package com.example.demo.services;

public interface ConsultationService {

    void saveConsultation(String username, Long idPatient, String prescripteur);

    Long deleteConsultationById(Long idConsultation);

    Long terminerConsultation(Long idConsultation);

    Long activerConsultation(Long idConsultation);
}
