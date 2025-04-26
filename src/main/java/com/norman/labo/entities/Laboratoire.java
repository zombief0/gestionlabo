package com.norman.labo.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Laboratoire implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLaboratoire;

    @Column(nullable = false)
    @NotBlank
    private String libelle;

    private String description;

    @OneToMany(mappedBy = "laboratoire", cascade = {CascadeType.MERGE, CascadeType.PERSIST},orphanRemoval = true)
    private List<Examen> examenList = new ArrayList<>();

    public Laboratoire(String libelle, String description, List<Examen> examenList) {
        this.libelle = libelle;
        this.description = description;
        this.examenList = examenList;
    }

}
