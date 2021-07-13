package com.norman.labo.services;

import com.norman.labo.entities.Consultation;
import com.norman.labo.entities.Examen;
import com.norman.labo.entities.ExamenSouscrit;
import com.norman.labo.entities.Patient;
import com.norman.labo.repositories.ConsultationRepository;
import com.norman.labo.repositories.ExamenSouscritRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

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
