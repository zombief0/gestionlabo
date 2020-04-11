package com.example.demo.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Laboratoire implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLaboratoire;

    @Column(nullable = false)
    private String libelle;

    private String description;

    @OneToMany(mappedBy = "laboratoire", cascade = {CascadeType.MERGE, CascadeType.PERSIST},orphanRemoval = true)
    private List<Examen> examenList = new ArrayList<>();

    public Laboratoire(String libelle, String description, List<Examen> examenList) {
        this.libelle = libelle;
        this.description = description;
        this.examenList = examenList;
    }


    public Laboratoire() {

    }

    public Long getIdLaboratoire() {
        return idLaboratoire;
    }

    public void setIdLaboratoire(Long idTypeExamen) {
        this.idLaboratoire = idTypeExamen;
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

    public List<Examen> getExamenList() {
        return examenList;
    }

    public void setExamenList(List<Examen> examenList) {
        this.examenList = examenList;
    }


}
