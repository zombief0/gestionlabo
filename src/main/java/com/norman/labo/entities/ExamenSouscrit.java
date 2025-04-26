package com.norman.labo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
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

    public static Comparator<ExamenSouscrit> ExamenSouscritComparator = (o1, o2) -> {
        return o2.getDate().compareTo(o1.getDate());
    };
}
