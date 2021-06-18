package com.example.demo.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
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

    public Consultation(String pres, Patient patient, Date dateConsultation){
        this.prescripteur = pres;
        this.patient = patient;
        this.dateConsultation = dateConsultation;
    }

    public static Comparator<Consultation> ConsultationComparator = (o1, o2) -> {
        return o2.getDateConsultation().compareTo(o1.getDateConsultation());
    };
}
