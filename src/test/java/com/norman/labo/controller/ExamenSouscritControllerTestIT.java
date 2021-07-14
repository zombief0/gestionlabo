package com.norman.labo.controller;

import com.norman.labo.entities.Consultation;
import com.norman.labo.entities.Examen;
import com.norman.labo.entities.ExamenSouscrit;
import com.norman.labo.entities.Patient;
import com.norman.labo.services.ConsultationService;
import com.norman.labo.services.ExamenService;
import com.norman.labo.services.ExamenSouscritService;
import com.norman.labo.services.PatientService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExamenSouscritController.class)
class ExamenSouscritControllerTestIT extends BaseControllerUserAndAnonymousTest{

    @MockBean
    private ConsultationService consultationService;
    @MockBean
    private ExamenSouscritService examenSouscritService;
    @MockBean
    private ExamenService examenService;

    @MockBean
    private PatientService patientService;
    @Captor
    private ArgumentCaptor<String> uniteCaptor;

    @Captor
    private ArgumentCaptor<Long> idCaptor;

    @Captor
    private ArgumentCaptor<Examen> examenArgumentCaptor;

    @Captor
    private ArgumentCaptor<Double> valeurNormaleCaptor;

    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "userTest@mail.com")
    @Test
    void montrerProfilPatient() throws Exception {
        Patient patient = new Patient();
        patient.setNom("Test");
        patient.setPrenom("Prenom Test");
        patient.setIdPersonne(6L);
        Consultation consultation = new Consultation();
        consultation.setIdConsultation(1L);
        consultation.setStatut("TERMINE");
        consultation.setPatient(patient);
        Examen examen = new Examen();
        examen.setIdExamen(1L);
        examen.setLibelle("Examen Test");
        ExamenSouscrit examenSouscrit = new ExamenSouscrit(null, examen, consultation, patient);
        consultation.setExamenSouscritList(Collections.singletonList(examenSouscrit));
        given(consultationService.fetchAllByIdPatient(patient.getIdPersonne()))
                .willReturn(Collections.singletonList(consultation));
        given(patientService.findPatientById(anyLong())).willReturn(patient);

        mockMvc.perform(get("/examenSouscrit/profil-patient/{id}", patient.getIdPersonne()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("consultations", "patient"))
                .andExpect(view().name("examenSouscrit/profil-patient"));

        then(consultationService).should().fetchAllByIdPatient(patient.getIdPersonne());
    }

    @Test
    void montrerProfilPatientUserNotLoggedIn() throws Exception {

        mockMvc.perform(get("/examenSouscrit/profil-patient/{id}", 1))
                .andExpect(status().is3xxRedirection());

        then(consultationService).shouldHaveNoInteractions();
    }

    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "userTest@mail.com")
    @Test
    void addValeurNormaleForm() throws Exception {
        Patient patient = new Patient();
        patient.setNom("Test");
        patient.setPrenom("Prenom Test");
        patient.setIdPersonne(6L);
        Consultation consultation = new Consultation();
        consultation.setIdConsultation(1L);
        consultation.setStatut("TERMINE");
        consultation.setPatient(patient);
        Examen examen = new Examen();
        examen.setIdExamen(1L);
        examen.setLibelle("Examen Test");
        ExamenSouscrit examenSouscrit = new ExamenSouscrit(null, examen, consultation, patient);
        examenSouscrit.setIdExamenPasser(3L);
        consultation.setExamenSouscritList(Collections.singletonList(examenSouscrit));
        given(examenSouscritService.fetchById(3L)).willReturn(examenSouscrit);

        mockMvc.perform(get("/examenSouscrit/addValeurNormale/{id}", 3))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("patient", "examenSouscrit"))
                .andExpect(view().name("examenSouscrit/add-valeur-normale"));

        then(examenSouscritService).should().fetchById(3L);
    }

    @Test
    void addValeurNormaleFormUserNotLoggedIn() throws Exception {

        mockMvc.perform(get("/examenSouscrit/addValeurNormale/{id}", 3))
                .andExpect(status().is3xxRedirection());

        then(examenSouscritService).shouldHaveNoInteractions();
    }

    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "userTest@mail.com")
    @Test
    void addExamSouscritForm() throws Exception {
        Examen examen = new Examen();
        examen.setIdExamen(1L);
        examen.setLibelle("Examen Test");
        List<Examen> examenList = Collections.singletonList(examen);
        given(examenService.fetchAll()).willReturn(examenList);

        mockMvc.perform(get("/examenSouscrit/addExamenSouscrit/{idPatient}/{idConsultation}",2, 5))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("examens", "patient", "consultation", "examen"))
                .andExpect(view().name("examenSouscrit/add-examen-souscrit"));

        then(examenService).should().fetchAll();
    }

    @Test
    void addExamSouscritFormUserNotLoggedIn() throws Exception {

        mockMvc.perform(get("/examenSouscrit/addExamenSouscrit/{idPatient}/{idConsultation}",2, 5))
                .andExpect(status().is3xxRedirection());

        then(examenService).shouldHaveNoInteractions();
    }

    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "userTest@mail.com")
    @Test
    void enregistrerValeurResultat() throws Exception {
        given(examenSouscritService.updateExamenSouscrit(anyLong(), anyDouble(), anyString())).willReturn(5L);
        mockMvc.perform(
                post("/examenSouscrit/enregistrerValeurNormale/{idExamSouscrit}", 1)
                        .with(csrf()).param("valeur", "3").param("unite", "mm"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/examenSouscrit/profil-patient/" + 5));

        then(examenSouscritService).should().updateExamenSouscrit(idCaptor.capture(), valeurNormaleCaptor.capture(), uniteCaptor.capture());
        assertThat(idCaptor.getValue()).isEqualTo(1);
        assertThat(valeurNormaleCaptor.getValue()).isEqualTo(3);
        assertThat(uniteCaptor.getValue()).isEqualTo("mm");
    }

    @Test
    void enregistrerValeurResultatUserNotLoggedIn() throws Exception {
        mockMvc.perform(
                post("/examenSouscrit/enregistrerValeurNormale/{idExamSouscrit}", 1)
                        .with(csrf())
                        .param("valeur", "3")
                        .param("unite", "mm"))
                .andExpect(status().is3xxRedirection());

        then(examenSouscritService).shouldHaveNoInteractions();
    }

    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "userTest@mail.com")
    @Test
    void saveExamenSouscrit() throws Exception {
        given(examenSouscritService.saveExamSouscrit(anyLong(), any(Examen.class))).willReturn(2L);
        mockMvc.perform(
                post("/examenSouscrit/enregistrer/{idConsultation}", 2)
                        .with(csrf())
                        .param("id", "1")
                        .param("code", "fto0")
                        .param("minValeur", "2")
                        .param("maxValeur", "4")
                        .param("libelle", "examen 1")
                        .param("description", "desc examen 1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/examenSouscrit/profil-patient/2"));

        then(examenSouscritService).should().saveExamSouscrit(idCaptor.capture(),
                examenArgumentCaptor.capture());
        assertThat(examenArgumentCaptor.getValue()).isNotNull();
        assertThat(idCaptor.getValue()).isEqualTo(2);
    }

    @Test
    void saveExamenSouscritUserNotLoggedIn() throws Exception {
        mockMvc.perform(
                post("/examenSouscrit/enregistrer/{idConsultation}", 2)
                        .with(csrf())
                        .param("id", "1")
                        .param("code", "fto0")
                        .param("minValeur", "2")
                        .param("maxValeur", "4")
                        .param("libelle", "examen 1")
                        .param("description", "desc examen 1"))
                .andExpect(status().is3xxRedirection());

        then(examenSouscritService).shouldHaveNoInteractions();
    }

    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "userTest@mail.com")
    @Test
    void delete() throws Exception {
        given(examenSouscritService.deleteById(anyLong())).willReturn(1L);
        mockMvc.perform(get("/examenSouscrit/delete/{idExamen}", 2))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/examenSouscrit/profil-patient/1"));

        then(examenSouscritService).should().deleteById(idCaptor.capture());
    }
}
