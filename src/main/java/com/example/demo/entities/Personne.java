package com.example.demo.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Personne implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPersonne;

    @Column(nullable = false)
    @NotNull
    private String nom;


    private String prenom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    @Min(0)
    @Max(value = 999999999,message = "Valeur trop grande")
    private int telephone;

    public Personne() {

    }

    public enum Sexe {
        MASCULIN, FEMININ
    }

    @Enumerated(EnumType.STRING)
    private Sexe sexe;

    public Personne(String nom, String prenom, Date date, int telephone, Sexe sexe) {
        this.nom = nom;
        this.prenom = prenom;
        this.date = date;
        this.telephone = telephone;
        this.sexe = sexe;
    }

    public Long getIdPersonne() {
        return idPersonne;
    }

    public void setIdPersonne(Long idPersonne) {
        this.idPersonne = idPersonne;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getTelephone() {
        return telephone;
    }

    public void setTelephone(int telephone) {
        this.telephone = telephone;
    }

    public Sexe getSexe() {
        return sexe;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }
}
