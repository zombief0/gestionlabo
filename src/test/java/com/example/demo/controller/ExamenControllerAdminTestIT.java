package com.example.demo.controller;

import com.example.demo.entities.Examen;
import com.example.demo.entities.Laboratoire;
import com.example.demo.repositories.ExamenRepository;
import com.example.demo.repositories.LaboratoireRepository;
import com.example.demo.services.ExamenServiceImpl;
import com.example.demo.services.LaboratoireService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExamenController.class)
public class ExamenControllerAdminTestIT extends BaseControllerAdminTest {

    @MockBean
    private ExamenServiceImpl examenService;

    @MockBean
    private LaboratoireService laboratoireService;

    @Captor
    private ArgumentCaptor<Examen> examenArgumentCaptor;

    @Captor
    private ArgumentCaptor<Examen> examenToUpdateArgumentCaptor;

    @Captor
    private ArgumentCaptor<Long> idArgumentCaptor;


    @Captor
    private ArgumentCaptor<Laboratoire> laboratoireArgumentCaptor;

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "adminTest@mail.com")
    void ajoutExamenForm() throws Exception {
        Laboratoire laboratoire = new Laboratoire();
        laboratoire.setLibelle("Lab 1");
        laboratoire.setIdLaboratoire(1L);
        laboratoire.setDescription("Desc Lab 1");
        given(laboratoireService.fetchById(1L)).willReturn(laboratoire);

        mockMvc.perform(get("/examen/ajout-examen/{idLaboratoire}", 1L))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("laboratoire", "examen"))
                .andExpect(view().name("examen/add-examen"));

        then(laboratoireService).should().fetchById(1L);
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "adminTest@mail.com")
    void enregistrerExamen() throws Exception {
        // Given
        Laboratoire laboratoire = new Laboratoire();
        laboratoire.setLibelle("Lab 1");
        laboratoire.setIdLaboratoire(1L);
        laboratoire.setDescription("Desc Lab 1");
        given(laboratoireService.fetchById(1L)).willReturn(laboratoire);

        // When
        mockMvc.perform(post("/examen/enregistrer/{idLaboratoire}", 1L).with(csrf())
                .param("code", "fto0")
                .param("minValeur", "2")
                .param("maxValeur", "4")
                .param("libelle", "examen 1")
                .param("description", "desc examen 1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/examen/listExamen/1"));

        // Then
        then(laboratoireService).should().fetchById(1L);

        then(examenService).should().saveExamen(examenArgumentCaptor.capture(), laboratoireArgumentCaptor.capture());

        assertThat(examenArgumentCaptor.getValue().getCode()).isEqualTo("fto0");
        assertThat(examenArgumentCaptor.getValue().getDescription()).isEqualTo("desc examen 1");
        assertThat(examenArgumentCaptor.getValue().getMinValeur()).isEqualTo(2);
        assertThat(examenArgumentCaptor.getValue().getMaxValeur()).isEqualTo(4);
        assertThat(examenArgumentCaptor.getValue().getLibelle()).isEqualTo("examen 1");
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "adminTest@mail.com")
    void enregistrerExamenErrorForm() throws Exception {
        // Given
        Laboratoire laboratoire = new Laboratoire();
        laboratoire.setLibelle("Lab 1");
        laboratoire.setIdLaboratoire(1L);
        laboratoire.setDescription("Desc Lab 1");
        given(laboratoireService.fetchById(1L)).willReturn(laboratoire);

        // When
        mockMvc.perform(post("/examen/enregistrer/{idLaboratoire}", 1L).with(csrf())
                .param("description", "desc examen 1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("examen"))
                .andExpect(model().attributeHasFieldErrors("examen", "code", "libelle"))
                .andExpect(view().name("/examen/add-examen"));

        // Then
        then(laboratoireService).should().fetchById(1L);

        then(examenService).shouldHaveNoInteractions();
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "adminTest@mail.com")
    void modifierExamen() throws Exception {
        // Given
        Laboratoire laboratoire = new Laboratoire();
        laboratoire.setLibelle("Lab 1");
        laboratoire.setIdLaboratoire(1L);
        laboratoire.setDescription("Desc Lab 1");

        Examen examen = new Examen();
        examen.setLaboratoire(laboratoire);
        examen.setIdExamen(1L);
        given(examenService.fetchExamById(1L)).willReturn(examen);

        mockMvc.perform(post("/examen/modifier/{id}", 1L).with(csrf())
                .param("code", "fto0")
                .param("minValeur", "2")
                .param("maxValeur", "4")
                .param("libelle", "examen 1")
                .param("description", "desc examen 1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/examen/listExamen/1"));

        then(examenService).should().fetchExamById(1L);
        then(examenService).should().updateExam(examenArgumentCaptor.capture(), examenToUpdateArgumentCaptor.capture());

        assertThat(examenToUpdateArgumentCaptor.getValue()).isEqualTo(examen);
        assertThat(examenArgumentCaptor.getValue().getCode()).isEqualTo("fto0");
        assertThat(examenArgumentCaptor.getValue().getDescription()).isEqualTo("desc examen 1");
        assertThat(examenArgumentCaptor.getValue().getMinValeur()).isEqualTo(2);
        assertThat(examenArgumentCaptor.getValue().getMaxValeur()).isEqualTo(4);
        assertThat(examenArgumentCaptor.getValue().getLibelle()).isEqualTo("examen 1");
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "adminTest@mail.com")
    void modifierExamenFormError() throws Exception {

        // Given
        Laboratoire laboratoire = new Laboratoire();
        laboratoire.setLibelle("Lab 1");
        laboratoire.setIdLaboratoire(1L);
        laboratoire.setDescription("Desc Lab 1");
        Examen examen = new Examen();
        examen.setLaboratoire(laboratoire);
        examen.setIdExamen(1L);
        given(examenService.fetchExamById(1L)).willReturn(examen);

        // When
        mockMvc.perform(post("/examen/modifier/{id}", 1L).with(csrf())
                .param("description", "desc examen 1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("examen"))
                .andExpect(model().attributeHasFieldErrors("examen", "code", "libelle"))
                .andExpect(model().attributeExists("laboratoire"))
                .andExpect(view().name("examen/modifier-examen"));

        // Then
        then(examenService).should().fetchExamById(1L);

    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "adminTest@mail.com")
    void deleteExamen() throws Exception {
        given(examenService.deleteExamenById(2L)).willReturn(1L);
        mockMvc.perform(get("/examen/delete/{id}", 2L))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/examen/listExamen/1"));

        then(examenService).should().deleteExamenById(idArgumentCaptor.capture());

        assertThat(idArgumentCaptor.getValue()).isEqualTo(2L);
    }
}
