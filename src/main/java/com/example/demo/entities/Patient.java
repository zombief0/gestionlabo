package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patient extends Personne implements Serializable {

    @OneToMany(mappedBy = "patient", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<ExamenSouscrit> examenSouscrits = new ArrayList<>();

    @OneToMany(mappedBy = "patient",cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private List<Consultation> consultations = new ArrayList<>();

    @DateTimeFormat
    private Date dateEnregistrement;

    @DateTimeFormat
    private Date dateModification;

    public static Comparator<Patient> patientComparator = (o1, o2) -> {
        String patientnom1 = o1.getNom().toUpperCase();
        String patientnom2 = o2.getNom().toUpperCase();
        return patientnom1.compareTo(patientnom2);
    };
}
