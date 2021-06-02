package com.example.demo.controller;

import com.example.demo.services.PatientServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;

import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(HomeController.class)
class HomeControllerTestIT extends BaseControllerTest{

    @MockBean
    private PatientServiceImpl patientService;

    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "userTest@mail.com")
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
