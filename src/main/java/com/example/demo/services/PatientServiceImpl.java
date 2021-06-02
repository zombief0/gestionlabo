package com.example.demo.services;

import com.example.demo.entities.Patient;
import com.example.demo.repositories.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService{
    private final PatientRepository patientRepository;

    public List<Patient> fetchAllPatients() {
        List<Patient> patients = patientRepository.findAll();

        patients.sort(Patient.patientComparator);
        return patients;
    }

    @Override
    public Patient savePatient(Patient patient) {
        patient.setDateEnregistrement(new Date());
        patient.setDateModification(new Date());
        patientRepository.save(patient);
        return patient;
    }
}
