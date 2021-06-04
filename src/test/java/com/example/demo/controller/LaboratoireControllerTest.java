package com.example.demo.controller;

import com.example.demo.entities.Laboratoire;
import com.example.demo.repositories.LaboratoireRepository;
import com.example.demo.services.LaboratoireService;
import com.example.demo.services.LaboratoireServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(LaboratoireController.class)
class LaboratoireControllerTest extends BaseControllerTest{

    @MockBean
    private LaboratoireServiceImpl laboratoireService;

    @MockBean
    private LaboratoireRepository laboratoireRepository;

    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "userTest@mail.com")
    @Test
    void listelaboUserLoggedIn() throws Exception {
        Laboratoire lab1 = new Laboratoire();
        lab1.setIdLaboratoire(1L);
        lab1.setLibelle("Labo 1");
        Laboratoire lab2 = new Laboratoire();
        lab2.setLibelle("Labo 2");
        lab2.setIdLaboratoire(1L);
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

    @Test
    void addLaboratoireForm() {
    }

    @Test
    void enregistrerLaboratoire() {
    }

    @Test
    void editerFormLabo() {
    }

    @Test
    void modifierLaboratoire() {
    }

    @Test
    void deleteLaboratoire() {
    }
}
