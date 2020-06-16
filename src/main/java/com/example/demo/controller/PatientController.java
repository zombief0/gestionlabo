package com.example.demo.controller;

import com.example.demo.entities.Patient;
import com.example.demo.repositories.PatientRepository;
import com.example.demo.repositories.UtilisateurRepository;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@Controller
@RequestMapping("/Gestion_Laboratoire_EMMAUS/patient")
public class PatientController {

    @InitBinder
    public void initBinder(WebDataBinder binder){
        binder.registerCustomEditor(String.class,new StringTrimmerEditor(true));
    }

    private PatientRepository patientRepository;
    private UtilisateurRepository medecinRepository;

    public PatientController(PatientRepository patientRepository, UtilisateurRepository medecinRepository) {
        this.patientRepository = patientRepository;
        this.medecinRepository = medecinRepository;
    }



    @GetMapping("/ajout-patient")
    public String formAjoutPatient(Model model){
        Patient patient = new Patient();
        model.addAttribute("patient",patient);
        return "/patient/ajout-patient";
    }

    @PostMapping("/enregistrer")
    public String ajoutPatient(@Valid Patient patient, BindingResult result, Model model){
        if(result.hasErrors()){
            return "/patient/ajout-patient";
        }

        patient.setDateEnregistrement(new Date());
        patient.setDateModification(new Date());
        patientRepository.save(patient);
        return "redirect:/";
    }

    @GetMapping("/editer/{id}")
    public String modifierFormPatient(@PathVariable Long id,Model model){
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Id invalide:" + id));
        model.addAttribute("patient",patient);
        return "patient/modifier-patient";
    }

    @PostMapping("/modifier/{id}")
    public String updatePatient(@PathVariable Long id,@Valid Patient patient,BindingResult bindingResult,Model model){

        if(bindingResult.hasErrors()){
            patient.setIdPersonne(id);
            return "patient/modifier-patient";
        }

        Patient patient1 = patientRepository.findByIdPersonne(id);
        patient1.setDate(patient.getDate());
        patient1.setNom(patient.getNom());
        patient1.setPrenom(patient.getPrenom());
        patient1.setSexe(patient.getSexe());
        patient1.setTelephone(patient.getTelephone());
        patient1.setDateModification(new Date());
        patientRepository.save(patient1);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deletePatient(@PathVariable Long id){
        patientRepository.deleteById(id);
        return "redirect:/";
    }

}
