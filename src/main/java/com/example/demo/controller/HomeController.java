package com.example.demo.controller;


import com.example.demo.entities.Patient;
import com.example.demo.repositories.PatientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {


    private PatientRepository patientRepository;

    public HomeController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @GetMapping("/")
    public String listPatients(Model model){
        List<Patient> patients = patientRepository.findAll();

        patients.sort(Patient.patientComparator);
        model.addAttribute("patients", patients);
        return "utilisateur/home";
    }

    @GetMapping("/login")
    public String login(){
        return "utilisateur/login";
    }

   /* @GetMapping("/error")
    public String errorPage(){
        return "error";
    }*/
}
