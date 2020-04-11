package com.example.demo.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Entity
public class Consultation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idConsultation;

    @Column(nullable = false)
    private String prescripteur;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateConsultation;

    @ManyToOne
    private Patient patient;

    private String statut;

    @OneToMany(mappedBy = "consultation", cascade = {CascadeType.MERGE,CascadeType.REFRESH})
    private List<ExamenSouscrit> examenSouscritList = new ArrayList<>();

    public Consultation() {
    }

    public Consultation(String pres, Patient patient, Date dateConsultation){
        this.prescripteur = pres;
        this.patient = patient;
        this.dateConsultation = dateConsultation;
    }

    public Long getIdConsultation() {
        return idConsultation;
    }

    public void setIdConsultation(Long idConsultation) {
        this.idConsultation = idConsultation;
    }

    public String getPrescripteur() {
        return prescripteur;
    }

    public Date getDateConsultation() {
        return dateConsultation;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public void setDateConsultation(Date dateConsultation) {
        this.dateConsultation = dateConsultation;
    }

    public void setPrescripteur(String prescripteur) {
        this.prescripteur = prescripteur;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public List<ExamenSouscrit> getExamenSouscritList() {
        return examenSouscritList;
    }

    public void setExamenSouscritList(List<ExamenSouscrit> examenSouscritList) {
        this.examenSouscritList = examenSouscritList;
    }

    public static Comparator<Consultation> ConsultationComparator = (o1, o2) -> {
        return o2.getDateConsultation().compareTo(o1.getDateConsultation());
    };
}
