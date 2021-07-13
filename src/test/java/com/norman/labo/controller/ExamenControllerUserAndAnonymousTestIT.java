package com.norman.labo.controller;

import com.norman.labo.entities.Examen;
import com.norman.labo.entities.Laboratoire;
import com.norman.labo.services.ExamenServiceImpl;
import com.norman.labo.services.LaboratoireService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExamenController.class)
class ExamenControllerUserAndAnonymousTestIT extends BaseControllerUserAndAnonymousTest {

    @MockBean
    private ExamenServiceImpl examenService;

    @MockBean
    private LaboratoireService laboratoireService;

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "userTest@mail.com")
    void listeExamenUserLoggedIn() throws Exception {
        Laboratoire laboratoire = new Laboratoire();
        laboratoire.setLibelle("Lab 1");
        laboratoire.setIdLaboratoire(1L);
        laboratoire.setDescription("Desc Lab 1");
        Examen examen = new Examen();
        examen.setLibelle("examen1");
        examen.setIdExamen(2L);
        examen.setLaboratoire(laboratoire);
        given(examenService.getAllByLaboratoire(1L)).willReturn(Collections.singletonList(examen));
        mockMvc.perform(get("/examen/listExamen/{idLaboratoire}", 1L))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("examens"))
                .andExpect(model().attributeExists("laboratoire"))
                .andExpect(view().name("examen/list-examen"));

        then(examenService).should().getAllByLaboratoire(1L);
    }

    @Test
    void listeExamenUserNotLoggedIn() throws Exception {
        mockMvc.perform(get("/examen/listExamen/{idLaboratoire}", 1L))
                .andExpect(status().is3xxRedirection());

        then(examenService).shouldHaveNoInteractions();
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "userTest@mail.com")
    void ajoutExamenFormUserNotAdmin() throws Exception {

        mockMvc.perform(get("/examen/ajout-examen/{idLaboratoire}", 1L))
                .andExpect(status().is4xxClientError());

        then(laboratoireService).shouldHaveNoInteractions();
    }

    @Test
    void ajoutExamenFormUserNotLoggedIn() throws Exception {

        mockMvc.perform(get("/examen/ajout-examen/{idLaboratoire}", 1L))
                .andExpect(status().is3xxRedirection());

        then(laboratoireService).shouldHaveNoInteractions();
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "userTest@mail.com")
    void enregistrerExamen() throws Exception {

        // When
        mockMvc.perform(post("/examen/enregistrer/{idLaboratoire}", 1L).with(csrf())
                .param("code", "fto0")
                .param("minValeur", "2")
                .param("maxValeur", "4")
                .param("libelle", "examen 1")
                .param("description", "desc examen 1"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "userTest@mail.com")
    void editerFormExamen() throws Exception {
        mockMvc.perform(get("/examen/editer/{id}", 1))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void editerFormExamenNotLoggedIn() throws Exception {
        mockMvc.perform(get("/examen/editer/{id}", 1))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "userTest@mail.com")
    void modifierExamen() throws Exception {
        mockMvc.perform(post("/examen/modifier/{id}", 1))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void modifierExamenNotLoggedIn() throws Exception {
        mockMvc.perform(post("/examen/modifier/{id}", 1))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "userTest@mail.com")
    void deleteExamen() throws Exception {
        mockMvc.perform(get("/examen/delete/{id}", 2L))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void deleteExamenNotLoggedIn() throws Exception {
        mockMvc.perform(get("/examen/delete/{id}", 2L))
                .andExpect(status().is3xxRedirection());
    }

}
