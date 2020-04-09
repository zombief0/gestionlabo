package com.example.demo.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Entity
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


    public Facture() {

    }

    public Long getIdFacture() {
        return idFacture;
    }

    public Date getDateCreationSecondaire() {
        return dateCreationSecondaire;
    }

    public void setDateCreationSecondaire(Date dateCreationSecondaire) {
        this.dateCreationSecondaire = dateCreationSecondaire;
    }

    public void setIdFacture(Long idFacture) {
        this.idFacture = idFacture;
    }

    public Date getDateCreationOriginale() {
        return dateCreationOriginale;
    }

    public void setDateCreationOriginale(Date date) {
        this.dateCreationOriginale = date;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public List<ExamenSouscrit> getExamenSouscrits() {
        return examenSouscrits;
    }

    public void setExamenSouscrits(List<ExamenSouscrit> examenSouscrits) {
        this.examenSouscrits = examenSouscrits;
    }

    public double getSomme() {
        return somme;
    }

    public void setSomme(double somme) {
        this.somme = somme;
    }

    public static Comparator<Facture> FactureComparator = (o1, o2) -> o2.getDateCreationOriginale().compareTo(o1.getDateCreationOriginale());
}
