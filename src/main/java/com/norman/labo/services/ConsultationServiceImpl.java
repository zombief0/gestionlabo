package com.norman.labo.services;

import com.norman.labo.entities.Consultation;
import com.norman.labo.entities.Patient;
import com.norman.labo.entities.Utilisateur;
import com.norman.labo.repositories.ConsultationRepository;
import com.norman.labo.repositories.PatientRepository;
import com.norman.labo.repositories.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConsultationServiceImpl implements ConsultationService {
    private final UtilisateurRepository utilisateurRepository;
    private final PatientRepository patientRepository;
    private final ConsultationRepository consultationRepository;

    @Override
    public void saveConsultation(String username, Long idPatient, String prescripteur) {
        Utilisateur medecin = utilisateurRepository.findByEmail(username);
        Optional<Patient> patient = patientRepository.findById(idPatient);

        patient.ifPresent(pat -> {
            Consultation consultation;
            if (!medecin.getRole().equals("MEDECIN")) {
                consultation = new Consultation(prescripteur, pat, new Date());
            } else {
                consultation = new Consultation(medecin.getNom(), pat, new Date());

            }
            consultation.setStatut("EN_COURS");
            consultationRepository.save(consultation);
        });

    }

    @Override
    public Long deleteConsultationById(Long idConsultation) {
        Optional<Consultation> consultation = consultationRepository.findById(idConsultation);
        Long idPatient = null;
        if (consultation.isPresent()){
            idPatient = consultation.get().getPatient().getIdPersonne();
            if (consultation.get().getExamenSouscritList().size() == 0){
                consultationRepository.deleteById(idConsultation);
            }
        }
        return idPatient;
    }

    @Override
    public Long terminerConsultation(Long idConsultation) {
        Optional<Consultation> consultation = consultationRepository.findById(idConsultation);
        Long idPatient = null;
        if (consultation.isPresent()){
            Consultation consultPresent = consultation.get();
            idPatient = consultPresent.getPatient().getIdPersonne();
            consultPresent.setStatut("TERMINE");
            consultationRepository.save(consultPresent);
        }
        return idPatient;
    }

    @Override
    public Long activerConsultation(Long idConsultation) {
        Optional<Consultation> consultation = consultationRepository.findById(idConsultation);
        Long idPatient = null;
        if (consultation.isPresent()){
            Consultation consultPresent = consultation.get();
            idPatient = consultPresent.getPatient().getIdPersonne();
            consultPresent.setStatut("EN_COURS");
            consultationRepository.save(consultPresent);
        }
        return idPatient;
    }

    @Override
    public List<Consultation> fetchAllByIdPatient(Long idPersonne) {
        return consultationRepository.findAllByPatient_IdPersonneOrderByDateConsultationDesc(idPersonne);
    }
}
