package com.example.demo.DTO;

import com.example.demo.controller.PasswordMatches;
import com.example.demo.entities.Personne;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

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

    public UtilisateurDTO(){}

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

    public int getTelephone() {
        return telephone;
    }

    public void setTelephone(int telephone) {
        this.telephone = telephone;
    }

    public Personne.Sexe getSexe() {
        return sexe;
    }

    public void setSexe(Personne.Sexe sexe) {
        this.sexe = sexe;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConf() {
        return conf;
    }

    public void setConf(String conf) {
        this.conf = conf;
    }
}
