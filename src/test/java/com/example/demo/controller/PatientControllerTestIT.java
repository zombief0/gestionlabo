package com.example.demo.controller;

import com.example.demo.entities.Patient;
import com.example.demo.repositories.PatientRepository;
import com.example.demo.repositories.UtilisateurRepository;
import com.example.demo.services.PatientService;
import com.example.demo.services.PatientServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
class PatientControllerTestIT extends BaseControllerTest {

    @MockBean
    private PatientServiceImpl patientService;

    @MockBean
    private PatientRepository patientRepository;

    @MockBean
    private UtilisateurRepository utilisateurRepository;

    @DisplayName("Should display add patient form when logged in")
    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "userTest@mail.com")
    @Test
    void displayAjoutPatientFormWhenUserIsLoggedIn() throws Exception {
        mockMvc.perform(get("/patient/ajout-patient"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("patient"))
                .andExpect(view().name("/patient/ajout-patient"));
    }

    @DisplayName("Should display login page when trying to access add patient form not loggedIn")
    @Test
    void redirectAjoutPatientFormWhenUserIsNotLoggedIn() throws Exception {
        mockMvc.perform(get("/patient/ajout-patient"))
                .andExpect(status().is3xxRedirection());
    }

    @DisplayName("Save patient successfully no form errors")
    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "userTest@mail.com")
    @Test
    void ajoutPatientNoErrors() throws Exception {

        mockMvc.perform(post("/patient/enregistrer").with(csrf())
                .param("nom", "Mbouende")
                .param("prenom", "Norman")
                .param("telephone", "25698714")
                .param("date", "1992-08-19")
                .param("sexe", "MASCULIN"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

    }

    @DisplayName("Post request to patient/enregistrer with required field nom null")
    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "userTest@mail.com")
    @Test
    void ajoutPatientError() throws Exception {

        mockMvc.perform(post("/patient/enregistrer").with(csrf())
                .param("prenom", "Norman")
                .param("telephone", "25698714")
                .param("date", "1992-08-19")
                .param("sexe", "MASCULIN"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("patient"))
                .andExpect(model().attributeHasFieldErrors("patient", "nom"));

    }


    @Test
    void modifierFormPatient() {
    }

    @Test
    void updatePatient() {
    }

    @Test
    void deletePatient() {
    }
}
