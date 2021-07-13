package com.norman.labo.controller;

import com.norman.labo.entities.*;
import com.norman.labo.repositories.FactureRepository;
import com.norman.labo.services.FactureService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.test.context.support.WithUserDetails;

import javax.sql.DataSource;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FactureController.class)
class FactureControllerTestIT extends BaseControllerUserAndAnonymousTest {
    @MockBean
    private FactureRepository factureRepository;

    @MockBean
    private DataSource dataSource;
    @MockBean
    private ResourceLoader resourceLoader;


    @MockBean
    private FactureService factureService;

    @Captor
    ArgumentCaptor<ExamenSouscrit> examenSouscritArgumentCaptor;

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "userTest@mail.com")
    void ajoutFactureForm() throws Exception {
        Facture facture = new Facture();
        facture.setIdFacture(6L);
        ExamenSouscrit examenSouscrit = new ExamenSouscrit();
        examenSouscrit.setPatient(new Patient());
        examenSouscrit.setExamen(new Examen());
        facture.setExamenSouscrits(Collections.singletonList(examenSouscrit));
        given(factureService.findByCode(anyLong())).willReturn(facture);
        mockMvc.perform(get("/facture/ajout-facture/{codeFacture}", 6))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("examenSouscrits", "facture", "message", "examenSouscrit"))
                .andExpect(view().name("facture/add-facture"));

        then(factureService).should().findByCode(anyLong());
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "userTest@mail.com")
    void ajoutFactureFormExamnSouscritEmpty() throws Exception {
        Facture facture = new Facture();
        facture.setIdFacture(6L);
        facture.setExamenSouscrits(Collections.emptyList());
        given(factureService.findByCode(anyLong())).willReturn(facture);
        mockMvc.perform(get("/facture/ajout-facture/{codeFacture}", 6))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/facture/list-facture"));

        then(factureService).should().findByCode(anyLong());
    }

    @Test
    void ajoutFactureFormUserNotLoggedIn() throws Exception {

        mockMvc.perform(get("/facture/ajout-facture/{codeFacture}", 6))
                .andExpect(status().is3xxRedirection());

        then(factureService).shouldHaveNoInteractions();
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "userTest@mail.com")
    void enregistrerFacture() throws Exception {
        given(factureService.saveFacture(anyString(), any(ExamenSouscrit.class), anyLong())).willReturn(1L);
        mockMvc.perform(post("/facture/enregistrer/{codeFacture}", 1)
                .with(csrf())
                .param("idExamenPasser", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:facture/ajout-facture/1"));

        then(factureService).should()
                .saveFacture(anyString(), examenSouscritArgumentCaptor.capture(), anyLong());
        assertThat(examenSouscritArgumentCaptor.getValue().getIdExamenPasser()).isEqualTo(1L);
    }

    @Test
    void enregistrerFactureUserNotLoggedIn() throws Exception {
        mockMvc.perform(post("/facture/enregistrer/{codeFacture}", 1)
                .with(csrf()))
                .andExpect(status().is3xxRedirection());

        then(factureService).shouldHaveNoInteractions();
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "userTest@mail.com")
    void listFacture() throws Exception {
        given(factureService.findAll()).willReturn(Collections.emptyList());
        mockMvc.perform(get("/facture/list-facture"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("factures", "message"))
                .andExpect(view().name("facture/list-facture"));
        then(factureService).should().findAll();
    }

    @Test
    void listFactureUserNotLoggedIn() throws Exception {
        mockMvc.perform(get("/facture/list-facture"))
                .andExpect(status().is3xxRedirection());
        then(factureService).shouldHaveNoInteractions();
    }

    @Test
    void creerPdfFacture() {
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "userTest@mail.com")
    void afficherDetail() throws Exception{
        Facture facture = new Facture();
        facture.setIdFacture(6L);
        ExamenSouscrit examenSouscrit = new ExamenSouscrit();
        examenSouscrit.setPatient(new Patient());
        examenSouscrit.setExamen(new Examen());
        examenSouscrit.setConsultation(new Consultation());
        facture.setExamenSouscrits(Collections.singletonList(examenSouscrit));
        given(factureService.findByCode(anyLong())).willReturn(facture);

        mockMvc.perform(get("/facture/detail-facture/{idFacture}", facture.getIdFacture()))
                .andExpect(status().isOk())
                .andExpect(view().name("facture/detail-facture"));

        then(factureService).should().findByCode(anyLong());
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "userTest@mail.com")
    void deleteFacture() throws Exception{
        mockMvc.perform(get("/facture/delete/{idFacture}", 1))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/facture/list-facture"));

        then(factureService).should().delete(anyLong());
    }
}
