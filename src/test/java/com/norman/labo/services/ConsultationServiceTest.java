package com.norman.labo.services;

import com.norman.labo.entities.*;
import com.norman.labo.repositories.ConsultationRepository;
import com.norman.labo.repositories.PatientRepository;
import com.norman.labo.repositories.UtilisateurRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ConsultationServiceTest {
    @Mock
    private PatientRepository patientRepository;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private ConsultationRepository consultationRepository;

    @InjectMocks
    private ConsultationServiceImpl consultationService;

    @Captor
    private ArgumentCaptor<Consultation> consultationArgumentCaptor;

    @Test
    void saveConsultationUserRoleUtilisateur() {
        Patient patient = new Patient();
        patient.setNom("Test");
        patient.setPrenom("Prenom Test");
        patient.setIdPersonne(6L);
        given(patientRepository.findById(6L)).willReturn(Optional.of(patient));

        Utilisateur loggedInUser = new Utilisateur();
        loggedInUser.setRole("UTILISATEUR");
        loggedInUser.setEmail("user@mail.com");
        loggedInUser.setIdPersonne(2L);
        given(utilisateurRepository.findByEmail("user@mail.com")).willReturn(loggedInUser);

        consultationService.saveConsultation("user@mail.com", 6L, "Prescripteur Test");

        then(patientRepository).should().findById(6L);
        then(utilisateurRepository).should().findByEmail("user@mail.com");
        then(consultationRepository).should().save(consultationArgumentCaptor.capture());
        assertThat(consultationArgumentCaptor.getValue().getStatut()).isEqualTo("EN_COURS");
        assertThat(consultationArgumentCaptor.getValue().getPrescripteur()).isEqualTo("Prescripteur Test");
    }

    @Test
    void saveConsultationUserRoleMedecin() {
        Patient patient = new Patient();
        patient.setNom("Test");
        patient.setPrenom("Prenom Test");
        patient.setIdPersonne(6L);
        given(patientRepository.findById(6L)).willReturn(Optional.of(patient));

        Utilisateur loggedInUser = new Utilisateur();
        loggedInUser.setRole("MEDECIN");
        loggedInUser.setNom("Medecin");
        loggedInUser.setEmail("medecin@mail.com");
        loggedInUser.setIdPersonne(2L);
        given(utilisateurRepository.findByEmail("medecin@mail.com")).willReturn(loggedInUser);

        consultationService.saveConsultation("medecin@mail.com", 6L, "");

        then(patientRepository).should().findById(6L);
        then(utilisateurRepository).should().findByEmail("medecin@mail.com");
        then(consultationRepository).should().save(consultationArgumentCaptor.capture());
        assertThat(consultationArgumentCaptor.getValue().getStatut()).isEqualTo("EN_COURS");
        assertThat(consultationArgumentCaptor.getValue().getPrescripteur()).isEqualTo("Medecin");
    }

    @Test
    void deleteConsultation() {
        Patient patient = new Patient();
        patient.setNom("Test");
        patient.setPrenom("Prenom Test");
        patient.setIdPersonne(6L);
        Consultation consultation = new Consultation();
        consultation.setIdConsultation(1L);
        consultation.setPatient(patient);
        given(consultationRepository.findById(1L)).willReturn(Optional.of(consultation));

        Long idPatient = consultationService.deleteConsultationById(1L);

        then(consultationRepository).should().findById(1L);
        then(consultationRepository).should().deleteById(1L);
        assertThat(idPatient).isEqualTo(6L);
    }

    @Test
    void terminerConsultation() {
        Patient patient = new Patient();
        patient.setNom("Test");
        patient.setPrenom("Prenom Test");
        patient.setIdPersonne(6L);
        Consultation consultation = new Consultation();
        consultation.setIdConsultation(1L);
        consultation.setStatut("EN_COURS");
        consultation.setPatient(patient);
        given(consultationRepository.findById(1L)).willReturn(Optional.of(consultation));

        Long idPatient = consultationService.terminerConsultation(1L);

        then(consultationRepository).should().findById(1L);
        then(consultationRepository).should().save(consultationArgumentCaptor.capture());
        assertThat(idPatient).isEqualTo(6L);
        assertThat(consultationArgumentCaptor.getValue().getStatut()).isEqualTo("TERMINE");
    }

    @Test
    void activerConsultation() {
        Patient patient = new Patient();
        patient.setNom("Test");
        patient.setPrenom("Prenom Test");
        patient.setIdPersonne(6L);
        Consultation consultation = new Consultation();
        consultation.setIdConsultation(1L);
        consultation.setStatut("TERMINE");
        consultation.setPatient(patient);
        given(consultationRepository.findById(1L)).willReturn(Optional.of(consultation));

        Long idPatient = consultationService.activerConsultation(1L);

        then(consultationRepository).should().findById(1L);
        then(consultationRepository).should().save(consultationArgumentCaptor.capture());
        assertThat(idPatient).isEqualTo(6L);
        assertThat(consultationArgumentCaptor.getValue().getStatut()).isEqualTo("EN_COURS");
    }

    @Test
    void fetchAllByIdPatient() {
        Patient patient = new Patient();
        patient.setNom("Test");
        patient.setPrenom("Prenom Test");
        patient.setIdPersonne(6L);
        Consultation consultation = new Consultation();
        consultation.setIdConsultation(1L);
        consultation.setStatut("TERMINE");
        consultation.setPatient(patient);
        Examen examen = new Examen();
        examen.setIdExamen(1L);
        examen.setLibelle("Examen Test");
        ExamenSouscrit examenSouscrit = new ExamenSouscrit(null, examen, consultation, patient);
        consultation.setExamenSouscritList(Collections.singletonList(examenSouscrit));
        given(consultationRepository.findAllByPatient_IdPersonneOrderByDateConsultationDesc(patient.getIdPersonne()))
                .willReturn(Collections.singletonList(consultation));

        List<Consultation> consultations = consultationService.fetchAllByIdPatient(patient.getIdPersonne());

        then(consultationRepository).should().findAllByPatient_IdPersonneOrderByDateConsultationDesc(patient.getIdPersonne());
        assertThat(consultations.size()).isGreaterThan(0);
    }
}
