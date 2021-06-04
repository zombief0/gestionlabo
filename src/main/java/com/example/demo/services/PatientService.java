package com.example.demo.services;

import com.example.demo.entities.Patient;

import java.util.List;

public interface PatientService {
    List<Patient> fetchAllPatients();

    Patient savePatient(Patient patient);

    Patient findPatientById(Long l);

    void updatePatient(Long id, Patient patient);

    void deletePatientById(Long id);
}
