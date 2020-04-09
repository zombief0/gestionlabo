package com.example.demo.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Utilisateur extends Personne implements Serializable {

    @OneToMany(mappedBy = "utilisateur", cascade = {CascadeType.MERGE, CascadeType.REFRESH},orphanRemoval = true)
    private List<Facture> factures = new ArrayList<>();

    @OneToMany(mappedBy = "medecin", cascade = {CascadeType.MERGE, CascadeType.REFRESH},orphanRemoval = true)
    private List<ExamenSouscrit> examenSouscrits = new ArrayList<>();

    @Column(nullable = false)
    @NotNull
    private String mdp;

    @Column(nullable = false)
    private String role;


    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private boolean isActive;

    public Utilisateur(String nom, String prenom, String email, Date date, int telephone, Sexe sexe, String mdp, String role) {

        super(nom, prenom, date, telephone, sexe);
        this.mdp = mdp;
        this.role = role;
        this.isActive = true;
        this.email = email;
    }

    public Utilisateur() {

    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Facture> getFactures() {
        return factures;
    }

    public void setFactures(List<Facture> factures) {
        this.factures = factures;
    }

    public static Comparator<Utilisateur> utilisateurComparator = (o1, o2) -> {
        String utilisateurnom1 = o1.getNom().toUpperCase();
        String utilisateurnom2 = o2.getNom().toUpperCase();
        return utilisateurnom1.compareTo(utilisateurnom2);

    };


    public List<ExamenSouscrit> getExamenSouscrits() {
        return examenSouscrits;
    }

    public void setExamenSouscrits(List<ExamenSouscrit> examenSouscrits) {
        this.examenSouscrits = examenSouscrits;
    }
}
