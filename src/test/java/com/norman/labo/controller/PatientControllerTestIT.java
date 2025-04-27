package com.norman.labo.controller;

import com.norman.labo.entities.Patient;
import com.norman.labo.entities.Personne;
import com.norman.labo.services.PatientService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PatientControllerTestIT {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @Captor
    private ArgumentCaptor<Patient> argumentCaptorPatient;

    @Captor
    private ArgumentCaptor<Long> argumentCaptorId;

    @DisplayName("Should display add patient form when logged in")
    @WithMockUser(username = "user@test.com", roles = "UTILISATEUR")
    @Test
    void displayAjoutPatientFormWhenUserIsLoggedIn() throws Exception {
        mockMvc.perform(get("/patient/ajout-patient"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("patient"))
                .andExpect(view().name("patient/ajout-patient"));
    }

    @DisplayName("Should display login page when trying to access add patient form not loggedIn")
    @Test
    void redirectAjoutPatientFormWhenUserIsNotLoggedIn() throws Exception {
        mockMvc.perform(get("/patient/ajout-patient"))
                .andExpect(status().is3xxRedirection());
    }

    @DisplayName("Save patient successfully no form errors")
    @WithMockUser(username = "user@test.com", roles = "UTILISATEUR")
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

        then(patientService).should().savePatient(argumentCaptorPatient.capture());
    }

    @DisplayName("Post request to patient/enregistrer with required field nom null")
    @WithMockUser(username = "user@test.com", roles = "UTILISATEUR")
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
        then(patientService).shouldHaveNoInteractions();
    }

    @Test
    void ajoutPatientUserNotLoggedIn() throws Exception {

        mockMvc.perform(post("/patient/enregistrer").with(csrf())
                .param("nom", "Mbouende")
                .param("prenom", "Norman")
                .param("telephone", "25698714")
                .param("date", "1992-08-19")
                .param("sexe", "MASCULIN"))
                .andExpect(status().is3xxRedirection());
        then(patientService).shouldHaveNoInteractions();
    }


    @DisplayName("Should show edit patient form when logged in")
    @WithMockUser(username = "user@test.com", roles = "UTILISATEUR")
    @Test
    void modifierFormPatientUserLoggedIn() throws Exception {
        Patient patientToEdit = new Patient();
        patientToEdit.setNom("Patient 1");
        patientToEdit.setIdPersonne(2L);
        given(patientService.findPatientById(2L)).willReturn(patientToEdit);

        mockMvc.perform(get("/patient/editer/{id}", 2))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("patient"))
                .andExpect(view().name("patient/modifier-patient"));

        then(patientService).should().findPatientById(2L);
    }

    @DisplayName("Should show edit patient form when not logged in")
    @Test
    void modifierFormPatientUserNotLoggedIn() throws Exception {

        mockMvc.perform(get("/patient/editer/{id}", 2))
                .andExpect(status().is3xxRedirection());

    }

    @DisplayName("Update patient no form field errors")
    @WithMockUser(username = "user@test.com", roles = "UTILISATEUR")
    @Test
    void updatePatientNoErrors() throws Exception {

        mockMvc.perform(post("/patient/modifier/{id}", 2L).with(csrf())
                .param("nom", "Mbouende")
                .param("prenom", "Norman")
                .param("telephone", "25698714")
                .param("date", "1992-08-19")
                .param("sexe", "MASCULIN"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        then(patientService).should().updatePatient(argumentCaptorId.capture(), argumentCaptorPatient.capture());
        assertThat(argumentCaptorId.getValue()).isEqualTo(2L);
        assertThat(argumentCaptorPatient.getValue().getNom()).isEqualTo("Mbouende");
        assertThat(argumentCaptorPatient.getValue().getPrenom()).isEqualTo("Norman");
        assertThat(argumentCaptorPatient.getValue().getTelephone()).isEqualTo(25698714);
        assertThat(argumentCaptorPatient.getValue().getSexe()).isEqualTo(Personne.Sexe.MASCULIN);
    }

    @Test
    void updatePatientUserNotLoggedIn() throws Exception {

        mockMvc.perform(post("/patient/modifier/{id}", 2L).with(csrf())
                .param("nom", "Mbouende")
                .param("prenom", "Norman")
                .param("telephone", "25698714")
                .param("date", "1992-08-19")
                .param("sexe", "MASCULIN"))
                .andExpect(status().is3xxRedirection());
        then(patientService).shouldHaveNoInteractions();

    }

    @DisplayName("Update patient form field error nom")
    @WithMockUser(username = "user@test.com", roles = "UTILISATEUR")
    @Test
    void updatePatientErrorNom() throws Exception {

        mockMvc.perform(post("/patient/modifier/{id}", 2L).with(csrf())
                .param("prenom", "Norman")
                .param("telephone", "25698714")
                .param("date", "1992-08-19")
                .param("sexe", "MASCULIN"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("patient"))
                .andExpect(model().attributeHasFieldErrors("patient", "nom"))
                .andExpect(view().name("patient/modifier-patient"));

        then(patientService).shouldHaveNoInteractions();
    }

    @WithMockUser(username = "user@test.com", roles = "UTILISATEUR")
    @Test
    void deletePatient() throws Exception {
        mockMvc.perform(get("/patient/delete/{id}", 2L))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        then(patientService).should().deletePatientById(argumentCaptorId.capture());
        assertThat(argumentCaptorId.getValue()).isEqualTo(2L);
    }

    @Test
    void deletePatientUserNotLoggedIn() throws Exception {
        mockMvc.perform(get("/patient/delete/{id}", 2L))
                .andExpect(status().is3xxRedirection());

        then(patientService).shouldHaveNoInteractions();
    }
}
