package com.norman.labo.controller;

import com.norman.labo.services.PatientService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTestIT {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PatientService patientService;

    @WithMockUser
    @DisplayName("Should display patient list on UI when user connected")
    @Test
    void listPatientsWhenUserConnected() throws Exception {

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("patients"))
                .andExpect(view().name("utilisateur/home"));

        then(patientService).should().fetchAllPatients();
    }


    @Test
    void listPatientsWhenNotConnected() throws Exception {

        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection());

        then(patientService).shouldHaveNoInteractions();
    }

    @Test
    void login() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }
}
