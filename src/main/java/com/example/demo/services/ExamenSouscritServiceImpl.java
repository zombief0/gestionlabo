package com.example.demo.services;

import com.example.demo.entities.Consultation;
import com.example.demo.entities.Examen;
import com.example.demo.entities.ExamenSouscrit;
import com.example.demo.repositories.ConsultationRepository;
import com.example.demo.repositories.ExamenSouscritRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExamenSouscritServiceImpl implements ExamenSouscritService{
    private final ExamenSouscritRepository examenSouscritRepository;
    private final ConsultationRepository consultationRepository;

    @Override
    public ExamenSouscrit fetchById(Long id) {
        return examenSouscritRepository.findById(id).orElse(null);
    }

    @Override
    public Long updateExamenSouscrit(Long idExamenSouscrit, Double valeurNormale, String unite) {
        Optional<ExamenSouscrit> examenSouscrit = examenSouscritRepository.findById(idExamenSouscrit);
        if (examenSouscrit.isPresent()){
            ExamenSouscrit exam = examenSouscrit.get();
            exam.setUnite(unite);
            exam.setValeurNormalePatient(valeurNormale);
            examenSouscritRepository.save(exam);
            return exam.getPatient().getIdPersonne();
        }
        return null;
    }

    @Override
    public Long saveExamSouscrit(Long idConsultation, Examen examen) {
        Optional<Consultation> consultation = consultationRepository.findById(idConsultation);
        Long idPatient = null;
        if (consultation.isPresent()){
            Consultation presentConsultation = consultation.get();
            idPatient = presentConsultation.getPatient().getIdPersonne();
            ExamenSouscrit examenSouscrit = new ExamenSouscrit(new Date(), examen,
                    presentConsultation, presentConsultation.getPatient());
            examenSouscrit.setValeurNormalePatient(-1);
            examenSouscritRepository.save(examenSouscrit);
        }
        return idPatient;
    }

    @Override
    public Long deleteById(Long idExamenSouscrit) {
        Optional<ExamenSouscrit> examenSouscrit = examenSouscritRepository.findById(idExamenSouscrit);
        Long idPatient = null;
        if (examenSouscrit.isPresent()){
            if (examenSouscrit.get().getFacture() == null){
                idPatient = examenSouscrit.get().getPatient().getIdPersonne();
                examenSouscritRepository.deleteById(idExamenSouscrit);
            }
        }
        return idPatient;
    }
}
