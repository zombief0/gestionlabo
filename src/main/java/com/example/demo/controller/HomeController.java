package com.example.demo.controller;


import com.example.demo.entities.Patient;
import com.example.demo.repositories.PatientRepository;
import com.example.demo.services.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

   /* @GetMapping("/error")
    public String errorPage(){
        return "error";
    }*/
}
