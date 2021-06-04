package com.example.demo.services;

import com.example.demo.entities.Patient;
import com.example.demo.repositories.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    @Override
    public Patient findPatientById(Long l) {
        return patientRepository.findById(l).orElse(null);
    }

    @Override
    public void updatePatient(Long id, Patient patient) {
        Optional<Patient> patientToUpdate = patientRepository.findById(id);
        patientToUpdate.ifPresent(patient1 -> {
            patient1.setNom(patient.getNom());
            patient1.setPrenom(patient.getPrenom());
            patient1.setTelephone(patient.getTelephone());
            patient1.setDate(patient.getDate());
            patient1.setSexe(patient.getSexe());
            patient1.setDateModification(new Date());
            patientRepository.save(patient1);
        });
    }

    @Override
    public void deletePatientById(Long id) {
        patientRepository.deleteById(id);
    }
}
