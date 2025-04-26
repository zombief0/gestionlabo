package com.norman.labo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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


    public enum Sexe {
        MASCULIN, FEMININ
    }

    @Enumerated(EnumType.STRING)
    private Sexe sexe;

}
