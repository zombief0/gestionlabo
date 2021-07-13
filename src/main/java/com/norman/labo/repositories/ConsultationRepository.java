package com.norman.labo.repositories;

import com.norman.labo.entities.Consultation;
import com.norman.labo.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation,Long> {

    Consultation findByIdConsultation(Long id);

    List<Consultation> findAllByPatient(Patient patient);

    List<Consultation> findAllByPatient_IdPersonneOrderByDateConsultationDesc(Long idPatient);
}

