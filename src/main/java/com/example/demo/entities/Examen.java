package com.example.demo.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
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

    public static Comparator<Examen> examenComparator = (o1, o2) -> {
        String examennom1 = o1.libelle.toUpperCase();
        String examennom2 = o2.libelle.toUpperCase();
        return examennom1.compareTo(examennom2);
    };

}
