package com.norman.labo.controller;

import com.norman.labo.entities.Laboratoire;
import com.norman.labo.services.LaboratoireService;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class LaboratoireControllerTestIT {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LaboratoireService laboratoireService;


    @Captor
    ArgumentCaptor<Laboratoire> laboratoireArgumentCaptor;

    @Captor
    ArgumentCaptor<Long> idLaboratoireArgumentCaptor;

    @WithMockUser
    @Test
    void listelaboUserLoggedIn() throws Exception {
        Laboratoire lab1 = new Laboratoire();
        lab1.setIdLaboratoire(200L);
        lab1.setLibelle("Labo 1");
        Laboratoire lab2 = new Laboratoire();
        lab2.setLibelle("Labo 2");
        lab2.setIdLaboratoire(2L);
        List<Laboratoire> laboratoires = new ArrayList<>(Arrays.asList(lab1, lab2));

        given(laboratoireService.fetchAllLaboratoire()).willReturn(laboratoires);
        mockMvc.perform(get("/laboratoire/listLaboratoire"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("laboratoires"))
                .andExpect(view().name("laboratoire/list-laboratoire"));

        then(laboratoireService).should().fetchAllLaboratoire();
    }

    @Test
    void listelaboUserNotLoggedIn() throws Exception {
        mockMvc.perform(get("/laboratoire/listLaboratoire"))
                .andExpect(status().is3xxRedirection());

        then(laboratoireService).shouldHaveNoInteractions();
    }

    @DisplayName("Display laboratoire add form")
    @WithMockUser
    @Test
    void addLaboratoireForm() throws Exception {
        mockMvc.perform(get("/laboratoire/add-laboratoire"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("laboratoire"))
                .andExpect(view().name("laboratoire/add-laboratoire"));
    }

    @DisplayName("Redirect to login page when trying to access add-form")
    @Test
    void addLaboratoireFormUserNotLoggedIn() throws Exception {
        mockMvc.perform(get("/laboratoire/add-laboratoire"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    void enregistrerLaboratoireNoFormErrors() throws Exception {
        mockMvc.perform(post("/laboratoire/enregistrer").with(csrf())
                .param("libelle", "Labo 1")
                .param("description", "Desc 1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/laboratoire/listLaboratoire"));
        then(laboratoireService).should().saveLaboratoire(laboratoireArgumentCaptor.capture());
        assertThat(laboratoireArgumentCaptor.getValue().getLibelle()).isEqualTo("Labo 1");
        assertThat(laboratoireArgumentCaptor.getValue().getDescription()).isEqualTo("Desc 1");
    }

    @Test
    void enregistrerLaboratoireUserNotLoggedIn() throws Exception {
        mockMvc.perform(post("/laboratoire/enregistrer").with(csrf())
                .param("libelle", "Labo 1")
                .param("description", "Desc 1"))
                .andExpect(status().is3xxRedirection());
        then(laboratoireService).shouldHaveNoInteractions();
    }

    @Test
    @WithMockUser
    void enregistrerLaboratoireFormErrors() throws Exception {
        mockMvc.perform(post("/laboratoire/enregistrer").with(csrf())
                .param("description", "Desc 1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("laboratoire"))
                .andExpect(model().attributeHasFieldErrors("laboratoire", "libelle"))
                .andExpect(view().name("laboratoire/add-laboratoire"));
        then(laboratoireService).shouldHaveNoInteractions();
    }

    @DisplayName("Show Edit labo form")
    @Test
    @WithMockUser
    void editerFormLabo() throws Exception {
        Laboratoire lab1 = new Laboratoire();
        lab1.setIdLaboratoire(1L);
        lab1.setLibelle("Labo 1");
        given(laboratoireService.fetchById(1L)).willReturn(lab1);

        mockMvc.perform(get("/laboratoire/editer/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("laboratoire"))
                .andExpect(view().name("laboratoire/modifier-laboratoire"));

        then(laboratoireService).should().fetchById(1L);
    }

    @Test
    void editerFormLaboUserNotLoggedIn() throws Exception {

        mockMvc.perform(get("/laboratoire/editer/{id}", 1))
                .andExpect(status().is3xxRedirection());

        then(laboratoireService).shouldHaveNoInteractions();
    }

    @DisplayName("Update Laboratoire Test")
    @Test
    @WithMockUser
    void modifierLaboratoire() throws Exception {

        mockMvc.perform(post("/laboratoire/modifier/{id}", 200L).with(csrf())
                .param("libelle", "Labo 1")
                .param("description", "Desc 1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/laboratoire/listLaboratoire"));

        then(laboratoireService).should().updateLaboratoire(idLaboratoireArgumentCaptor.capture(), laboratoireArgumentCaptor.capture());

        assertThat(idLaboratoireArgumentCaptor.getValue()).isEqualTo(200L);
        assertThat(laboratoireArgumentCaptor.getValue().getDescription()).isEqualTo("Desc 1");
        assertThat(laboratoireArgumentCaptor.getValue().getLibelle()).isEqualTo("Labo 1");
    }

    @Test
    void modifierLaboratoireUserNotLoggedIn() throws Exception {

        mockMvc.perform(post("/laboratoire/modifier/{id}", 200L).with(csrf())
                .param("libelle", "Labo 1")
                .param("description", "Desc 1"))
                .andExpect(status().is3xxRedirection());

        then(laboratoireService).shouldHaveNoInteractions();

    }

    @Test
    @WithMockUser
    void modifierLaboratoireFormError() throws Exception {

        mockMvc.perform(post("/laboratoire/modifier/{id}", 200L).with(csrf())
                .param("description", "Desc 1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("laboratoire"))
                .andExpect(model().attributeHasFieldErrors("laboratoire", "libelle"))
                .andExpect(view().name("laboratoire/modifier-laboratoire"));

        then(laboratoireService).shouldHaveNoMoreInteractions();

    }

    @Test
    @WithMockUser
    void deleteLaboratoire() throws Exception {
        mockMvc.perform(get("/laboratoire/delete/{id}", 200))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/laboratoire/listLaboratoire"));

        then(laboratoireService).should().deleteLaboratoire(idLaboratoireArgumentCaptor.capture());
        assertThat(idLaboratoireArgumentCaptor.getValue()).isEqualTo(200L);
    }

    @Test
    void deleteLaboratoireUserNotLoggedIn() throws Exception {
        mockMvc.perform(get("/laboratoire/delete/{id}", 200))
                .andExpect(status().is3xxRedirection());

        then(laboratoireService).shouldHaveNoInteractions();
    }
}
