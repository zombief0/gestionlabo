package com.norman.labo.repositories;


import com.norman.labo.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ExamenSouscritRepository extends JpaRepository<ExamenSouscrit,Long> {
    ExamenSouscrit findByIdExamenPasser(Long idExamen);


    List<ExamenSouscrit> findAllByPatient(Patient patient);

    List<ExamenSouscrit> findAllByPatientAndFactureIsNull(Patient patient);

    List<ExamenSouscrit> findAllByDate(Date date);

    List<ExamenSouscrit> findAllByConsultation(Consultation consultation);

    List<ExamenSouscrit> findAllByFacture(Facture facture);

    List<ExamenSouscrit> findAllByExamen(Examen examen);
}
