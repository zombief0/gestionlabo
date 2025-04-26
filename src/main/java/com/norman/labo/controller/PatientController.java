package com.norman.labo.controller;

import com.norman.labo.entities.Patient;
import com.norman.labo.services.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientController {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    private final PatientService patientService;

    @GetMapping("/ajout-patient")
    public String formAjoutPatient(Model model) {
        Patient patient = new Patient();
        model.addAttribute("patient", patient);
        return "patient/ajout-patient";
    }

    @PostMapping("/enregistrer")
    public String ajoutPatient(@Valid Patient patient, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "patient/ajout-patient";
        }

        patientService.savePatient(patient);
        return "redirect:/";
    }

    @GetMapping("/editer/{id}")
    public String modifierFormPatient(@PathVariable Long id, Model model) {
        Patient patient = patientService.findPatientById(id);
        model.addAttribute("patient", patient);
        return "patient/modifier-patient";
    }

    @PostMapping("/modifier/{id}")
    public String updatePatient(@PathVariable Long id, @Valid Patient patient, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            patient.setIdPersonne(id);
            return "patient/modifier-patient";
        }

        patientService.updatePatient(id, patient);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deletePatient(@PathVariable Long id) {
        patientService.deletePatientById(id);
        return "redirect:/";
    }

}
