package com.example.demo.entities;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Entity
public class Examen implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idExamen;

    @NotNull
    private String code;

    @NotNull
    private double minValeur;

    @NotNull
    private double maxValeur;

    @Column(nullable = false)
    @NotNull
    private String libelle;

    private Date dateAjout;

    private String description;

    @Min(0)
    private double prix;

    @ManyToOne
    private Laboratoire laboratoire;

    @OneToMany(mappedBy = "examen",cascade = {CascadeType.PERSIST, CascadeType.MERGE},orphanRemoval = true)
    private List<ExamenSouscrit> examenSouscrits = new ArrayList<>();

    public Examen(String code, @NotNull double minValeur, @NotNull double maxValeur, String libelle, String description, double prix, Laboratoire laboratoire) {
        this.code = code;
        this.minValeur = minValeur;
        this.maxValeur = maxValeur;
        this.libelle = libelle;
        this.description = description;
        this.prix = prix;
        this.laboratoire = laboratoire;
    }

    public Examen() {

    }

    public Long getIdExamen() {
        return idExamen;
    }

    public void setIdExamen(Long idExamen) {
        this.idExamen = idExamen;
    }



    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public Laboratoire getLaboratoire() {
        return laboratoire;
    }

    public void setLaboratoire(Laboratoire laboratoire) {
        this.laboratoire = laboratoire;
    }

    public List<ExamenSouscrit> getExamenSouscrits() {
        return examenSouscrits;
    }

    public void setExamenSouscrits(List<ExamenSouscrit> examenSouscrits) {
        this.examenSouscrits = examenSouscrits;
    }

    public static Comparator<Examen> examenComparator = (o1, o2) -> {
        String examennom1 = o1.libelle.toUpperCase();
        String examennom2 = o2.libelle.toUpperCase();
        return examennom1.compareTo(examennom2);
    };

    public Date getDateAjout() {
        return dateAjout;
    }

    public void setDateAjout(Date dateAjout) {
        this.dateAjout = dateAjout;
    }

    public double getMinValeur() {
        return minValeur;
    }

    public void setMinValeur(double minValeur) {
        this.minValeur = minValeur;
    }

    public double getMaxValeur() {
        return maxValeur;
    }

    public void setMaxValeur(double maxValeur) {
        this.maxValeur = maxValeur;
    }
}
