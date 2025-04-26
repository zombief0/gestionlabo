package com.norman.labo.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Facture implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFacture;

    @DateTimeFormat
    private Date dateCreationOriginale;

    @DateTimeFormat
    private Date dateCreationSecondaire;

    private double somme;

    @ManyToOne
    private Utilisateur utilisateur;

    @OneToMany(mappedBy = "facture",cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<ExamenSouscrit> examenSouscrits = new ArrayList<>();

    public Facture(Date dateCreationOriginale, Date dateCreationSecondaire,Utilisateur utilisateur,double somme) {
        this.dateCreationOriginale = dateCreationOriginale;
        this.dateCreationSecondaire = dateCreationSecondaire;
        this.utilisateur = utilisateur;
        this.somme = somme;
    }

    public static Comparator<Facture> FactureComparator = (o1, o2) -> o2.getDateCreationOriginale().compareTo(o1.getDateCreationOriginale());
}
