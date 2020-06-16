package com.example.demo.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Entity
public class Patient extends Personne implements Serializable {

    @OneToMany(mappedBy = "patient", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<ExamenSouscrit> examenSouscrits = new ArrayList<>();

    @OneToMany(mappedBy = "patient",cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private List<Consultation> consultations = new ArrayList<>();

    @DateTimeFormat
    private Date dateEnregistrement;

    @DateTimeFormat
    private Date dateModification;

    public Patient(String nom, String prenom, Date date, int telephone, Sexe sexe, Date dateEnregistrement) {
        super(nom, prenom, date, telephone, sexe);
        this.dateEnregistrement = dateEnregistrement;
    }

    public Patient() {

    }

    public Date getDateModification() {
        return dateModification;
    }

    public void setDateModification(Date dateModification) {
        this.dateModification = dateModification;
    }

    public List<ExamenSouscrit> getExamenSouscrits() {
        return examenSouscrits;
    }

    public void setExamenSouscrits(List<ExamenSouscrit> examenSouscrits) {
        this.examenSouscrits = examenSouscrits;
    }

    public Date getDateEnregistrement() {
        return dateEnregistrement;
    }

    public void setDateEnregistrement(Date dateEnregistrement) {
        this.dateEnregistrement = dateEnregistrement;
    }

    public static Comparator<Patient> patientComparator = (o1, o2) -> {
        String patientnom1 = o1.getNom().toUpperCase();
        String patientnom2 = o2.getNom().toUpperCase();
        return patientnom1.compareTo(patientnom2);
    };

    public List<Consultation> getConsultations() {
        return consultations;
    }

    public void setConsultations(List<Consultation> consultations) {
        this.consultations = consultations;
    }
}
