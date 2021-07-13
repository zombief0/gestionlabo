package com.norman.labo.services;

import com.norman.labo.entities.Patient;
import com.norman.labo.repositories.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {
    @Mock
    private PatientRepository patientRepository;
    
    @InjectMocks
    private PatientServiceImpl patientService;

    private Patient patientExemple1;
    private Patient patientExemple2;

    @Captor
    ArgumentCaptor<Patient> patientArgumentCaptor;

    @Captor
    ArgumentCaptor<Long> idArgumentCaptor;

    private List<Patient> patients;

    @BeforeEach
    void setUp() {
        patientExemple1 = new Patient();
        patientExemple1.setNom("nom 2");
        patientExemple1.setPrenom("prenom 2");
        patientExemple1.setIdPersonne(2L);

        patientExemple2 = new Patient();
        patientExemple2.setNom("nom 1");
        patientExemple2.setPrenom("prenom 1");
        patientExemple2.setIdPersonne(1L);

        patients = new ArrayList<>(Arrays.asList(patientExemple1, patientExemple2));
    }

    @DisplayName("Should get patient list")
    @Test
    void fetchPatientList() {
        //GIVEN
        given(patientRepository.findAll()).willReturn(patients);

        //WHEN
        List<Patient> patientList = patientService.fetchAllPatients();

        //THEN
        then(patientRepository).should().findAll();
        assertThat(patientList.get(0)).isEqualTo(patientExemple2);
        assertThat(patientList.get(1)).isEqualTo(patientExemple1);
    }

    @Test
    void savePatient(){
        given(patientRepository.save(patientExemple1)).willReturn(patientExemple1);

        Patient savedPatient = patientService.savePatient(patientExemple1);

        then(patientRepository).should().save(patientExemple1);
        assertThat(savedPatient.getDateEnregistrement()).isNotNull();
        assertThat(savedPatient.getDateModification()).isNotNull();
    }

    @Test
    void fetchPatientById() {
        given(patientRepository.findById(2L)).willReturn(Optional.of(patientExemple1));

        Patient patientById = patientService.findPatientById(2L);

        then(patientRepository).should().findById(2L);
        assertThat(patientById).isNotNull();
    }

    @Test
    void updatePatient() {
        given(patientRepository.findById(2L)).willReturn(Optional.of(patientExemple1));

        patientService.updatePatient(2L, patientExemple2);

        then(patientRepository).should().findById(2L);
        then(patientRepository).should().save(patientArgumentCaptor.capture());

        assertThat(patientArgumentCaptor.getValue().getNom()).isEqualTo(patientExemple2.getNom());
    }

    @Test
    void deletePatient() {
        given(patientRepository.findById(2L)).willReturn(Optional.of(patientExemple1));
        patientService.deletePatientById(2L);
        then(patientRepository).should().deleteById(idArgumentCaptor.capture());
        assertThat(idArgumentCaptor.getValue()).isEqualTo(2L);
    }
}
