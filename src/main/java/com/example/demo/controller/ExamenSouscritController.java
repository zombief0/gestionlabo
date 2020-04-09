package com.example.demo.controller;

import com.example.demo.entities.Examen;
import com.example.demo.entities.ExamenSouscrit;
import com.example.demo.entities.Patient;
import com.example.demo.entities.Utilisateur;
import com.example.demo.repositories.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/Gestion_Laboratoire_EMMAUS/examenSouscrit")
public class ExamenSouscritController {

    private PatientRepository patientRepository;
    private UtilisateurRepository medecinRepository;
    private FactureRepository factureRepository;
    private ExamenRepository examenRepository;
    private ExamenSouscritRepository examenSo;

    public ExamenSouscritController(PatientRepository patientRepository, UtilisateurRepository medecinRepository, FactureRepository factureRepository, ExamenRepository examenRepository, ExamenSouscritRepository examenSo) {
        this.patientRepository = patientRepository;
        this.medecinRepository = medecinRepository;
        this.factureRepository = factureRepository;
        this.examenRepository = examenRepository;
        this.examenSo = examenSo;
    }

    @GetMapping("/profil-patient/{id}")
    public String montrerProfilPatient(@PathVariable Long id, Model model){
        initiateView(id, model);
        FactureController.successMessage = "null";
        return "examenSouscrit/profil-patient";
    }

    @PostMapping("/enregistrer/{idPatient}")
    public String saveExamenSouscrit(@PathVariable Long idPatient,
                                     @Valid Examen examen,
                                     Model model,
                                     BindingResult result,
                                     Authentication authentication){
        initiateView(idPatient, model);
        if(result.hasErrors()){
            return "examenSouscrit/profil-patient";
        }
        Patient patient = patientRepository.findByIdPersonne(idPatient);
        Utilisateur medecin = medecinRepository.findByEmail(authentication.getName());
        if(medecin.getRole().equals("MEDECIN")) {
            ExamenSouscrit examenSouscrit = new ExamenSouscrit(new Date(), examen,
                    patient, medecin);
            examenSo.save(examenSouscrit);
        }else {
            ExamenSouscrit examenSouscrit = new ExamenSouscrit(new Date(), examen,
                    patient, null);
            examenSo.save(examenSouscrit);
        }
        return "redirect:/Gestion_Laboratoire_EMMAUS/examenSouscrit/profil-patient/" + idPatient;
    }

    private void initiateView(@PathVariable Long idPatient, Model model) {
        Patient patient = patientRepository.findByIdPersonne(idPatient);
        Examen selectedExamen = new Examen();
        List<ExamenSouscrit> examenSouscrits = patient.getExamenSouscrits();
        List<Examen> examenList = examenRepository.findAll();
        examenList.sort(Examen.examenComparator);
        examenSouscrits.sort(ExamenSouscrit.ExamenSouscritComparator);
        model.addAttribute("examen",selectedExamen);
        model.addAttribute("examens",examenList);
        model.addAttribute("examenSouscrits",examenSouscrits);
        model.addAttribute("patient",patient);
    }

    @GetMapping("/delete/{idExamen}/{idPatient}")
    public String delete(@PathVariable Long idExamen,
                         @PathVariable Long idPatient){
        examenSo.deleteById(idExamen);
        return "redirect:/Gestion_Laboratoire_EMMAUS/examenSouscrit/profil-patient/" + idPatient;

    }
}
