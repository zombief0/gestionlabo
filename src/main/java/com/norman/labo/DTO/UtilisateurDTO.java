package com.norman.labo.DTO;

import com.norman.labo.controller.PasswordMatches;
import com.norman.labo.entities.Personne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@PasswordMatches(first = "mdp",second = "conf")
public class UtilisateurDTO {

    private Long id;

    @NotNull
    private String nom;


    private String prenom;

    @Min(0)
    @Max(999999999)
    private int telephone;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    public enum Sexe {
        MASCULIN, FEMININ
    }

    @Enumerated(EnumType.STRING)
    private Personne.Sexe sexe;
    private String mdp;
    private String role;

    @NotNull
    @Email
    private String email;

    private String conf;
    private String oldPassword;

    public UtilisateurDTO(Long id, @NotNull String nom, String prenom, @Min(0) @Max(999999999) int telephone, Date date, Personne.Sexe sexe, String mdp, String role, @NotNull @Email String email, String conf) {
        this.id=id;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.date = date;
        this.sexe = sexe;
        this.mdp = mdp;
        this.role = role;
        this.email = email;
        this.conf = conf;
    }

}
