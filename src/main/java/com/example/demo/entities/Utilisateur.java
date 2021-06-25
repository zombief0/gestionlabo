package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Utilisateur extends Personne implements Serializable {

    @OneToMany(mappedBy = "utilisateur", cascade = {CascadeType.MERGE, CascadeType.REFRESH},orphanRemoval = true)
    private List<Facture> factures = new ArrayList<>();

    @Column(nullable = false)
    @NotNull
    private String mdp;

    @Column(nullable = false)
    private String role;


    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private boolean isActive;


    public Utilisateur(String nom, String prenom, String email, Date date, int telephone, Sexe sexe, String pwd, String role) {
        super(null, nom, prenom, date, telephone, sexe);
        this.mdp = pwd;
        this.role = role;
        this.email = email;
    }
}
