package com.example.demo.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

@Entity
public class ExamenSouscrit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idExamenPasser;

    @DateTimeFormat
    private Date date;

    @ManyToOne
    private Examen examen;

    @ManyToOne
    private Consultation consultation;

    private double valeurNormalePatient;

    private String unite;

    @ManyToOne
    private Patient patient;

    @ManyToOne
    private Facture facture;

    public ExamenSouscrit(Date date, Examen examen, Consultation consultation, Patient patient) {
        this.date = date;
        this.examen = examen;
        this.consultation = consultation;
        this.patient = patient;
    }

    public ExamenSouscrit() {

    }

    public double getValeurNormalePatient() {
        return valeurNormalePatient;
    }

    public void setValeurNormalePatient(double valeurNormalePatient) {
        this.valeurNormalePatient = valeurNormalePatient;
    }

    public Long getIdExamenPasser() {
        return idExamenPasser;
    }

    public Facture getFacture() {
        return facture;
    }

    public void setFacture(Facture facture) {
        this.facture = facture;
    }

    public void setIdExamenPasser(Long idExamenPasser) {
        this.idExamenPasser = idExamenPasser;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Consultation getConsultation() {
        return consultation;
    }

    public void setConsultation(Consultation consultation) {
        this.consultation = consultation;
    }

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public Examen getExamen() {
        return examen;
    }

    public void setExamen(Examen examen) {
        this.examen = examen;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public static Comparator<ExamenSouscrit> ExamenSouscritComparator = (o1, o2) -> {
        return o2.getDate().compareTo(o1.getDate());
    };
}
