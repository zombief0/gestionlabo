package com.norman.labo.controller;


import com.norman.labo.entities.Patient;
import com.norman.labo.services.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PatientService patientService;

    @GetMapping("/")
    public String listPatients(Model model){
        List<Patient> patients = patientService.fetchAllPatients();
        model.addAttribute("patients", patients);
        return "utilisateur/home";
    }

    @GetMapping("/login")
    public String login(){
        return "utilisateur/login";
    }

}
