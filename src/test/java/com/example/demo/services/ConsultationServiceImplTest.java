package com.example.demo.services;

import com.example.demo.entities.Consultation;
import com.example.demo.entities.Patient;
import com.example.demo.entities.Utilisateur;
import com.example.demo.repositories.ConsultationRepository;
import com.example.demo.repositories.PatientRepository;
import com.example.demo.repositories.UtilisateurRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ConsultationServiceImplTest {
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
}
