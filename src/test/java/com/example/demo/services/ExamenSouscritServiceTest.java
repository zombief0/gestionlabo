package com.example.demo.services;

import com.example.demo.entities.Consultation;
import com.example.demo.entities.Examen;
import com.example.demo.entities.ExamenSouscrit;
import com.example.demo.entities.Patient;
import com.example.demo.repositories.ConsultationRepository;
import com.example.demo.repositories.ExamenRepository;
import com.example.demo.repositories.ExamenSouscritRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ExamenSouscritServiceTest {
    @Mock
    private ExamenSouscritRepository examenSouscritRepository;

    @Mock
    private ConsultationRepository consultationRepository;

    @InjectMocks
    private ExamenSouscritServiceImpl examenSouscritService;

    @Captor
    private ArgumentCaptor<ExamenSouscrit> examenSouscritArgumentCaptor;

    @Captor
    private ArgumentCaptor<Long> idCaptor;
    private Patient patient;
    private Consultation consultation;
    private Examen examen;
    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.setNom("Test");
        patient.setPrenom("Prenom Test");
        patient.setIdPersonne(6L);

        consultation = new Consultation();
        consultation.setIdConsultation(1L);
        consultation.setStatut("TERMINE");
        consultation.setPatient(patient);

        examen = new Examen();
        examen.setIdExamen(1L);
        examen.setLibelle("Examen Test");
    }

    @Test
    void fetchById() {

        ExamenSouscrit examenSouscrit = new ExamenSouscrit(null, examen, consultation, patient);

        given(examenSouscritRepository.findById(anyLong()))
                .willReturn(Optional.of(examenSouscrit));

        examenSouscritService.fetchById(2L);

        then(examenSouscritRepository).should().findById(anyLong());
    }

    @Test
    void saveExamSouscrit() {

        given(consultationRepository.findById(1L)).willReturn(Optional.of(consultation));

        Long idPatient = examenSouscritService.saveExamSouscrit(1L, examen);

        then(consultationRepository).should().findById(1L);
        then(examenSouscritRepository).should().save(examenSouscritArgumentCaptor.capture());
        assertThat(idPatient).isNotNull();
        assertThat(examenSouscritArgumentCaptor.getValue()).isNotNull();
    }

    @Test
    void deleteById() {
        ExamenSouscrit examenSouscrit = new ExamenSouscrit(null, examen, consultation, patient);
        given(examenSouscritRepository.findById(anyLong())).willReturn(Optional.of(examenSouscrit));

        Long idPatient = examenSouscritService.deleteById(3L);

        then(examenSouscritRepository).should().findById(anyLong());
        then(examenSouscritRepository).should().deleteById(idCaptor.capture());
        assertThat(idCaptor.getValue()).isEqualTo(3L);
        assertThat(idPatient).isEqualTo(6L);
    }
}
