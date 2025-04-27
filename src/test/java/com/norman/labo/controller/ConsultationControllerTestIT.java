package com.norman.labo.controller;

import com.norman.labo.services.ConsultationService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class ConsultationControllerTestIT {
    @Autowired
    private MockMvc mockMvc;
    @Captor
    private ArgumentCaptor<String> argumentCaptorUsername;

    @Captor
    private ArgumentCaptor<Long> argumentCaptorId;

    @Captor
    private ArgumentCaptor<String> argumentCaptorPrescripteur;

    @MockBean
    private ConsultationService consultationService;

    @WithMockUser(username = "user@test.com", roles = "UTILISATEUR")
    @Test
    void enregistrerConsultation() throws Exception {
        mockMvc.perform(post("/consultation/enregistrer/{idPatient}", 5)
                .with(csrf())
                .param("prescripteur", "Norman Mbouende"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/examenSouscrit/profil-patient/5"));

        then(consultationService).should().saveConsultation(argumentCaptorUsername.capture(),
                argumentCaptorId.capture(), argumentCaptorPrescripteur.capture());
        assertThat(argumentCaptorUsername.getValue()).isEqualTo("user@test.com");
        assertThat(argumentCaptorId.getValue()).isEqualTo(5L);
        assertThat(argumentCaptorPrescripteur.getValue()).isEqualTo("Norman Mbouende");
    }

    @Test
    void enregistrerConsultationUserNotLoggedIn() throws Exception {
        mockMvc.perform(post("/consultation/enregistrer/{idPatient}", 5)
                .with(csrf())
                .param("prescripteur", "Norman Mbouende"))
                .andExpect(status().is3xxRedirection());

        then(consultationService).shouldHaveNoInteractions();
    }

    @WithMockUser(username = "user@test.com", roles = "UTILISATEUR")
    @Test
    void delete() throws Exception {
        given(consultationService.deleteConsultationById(1L)).willReturn(3L);
        mockMvc.perform(get("/consultation/delete/{idConsultation}", 1))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/examenSouscrit/profil-patient/3"));

        then(consultationService).should().deleteConsultationById(argumentCaptorId.capture());
        assertThat(argumentCaptorId.getValue()).isEqualTo(1);
    }

    @Test
    void deleteUserNotLoggedIn() throws Exception {
        mockMvc.perform(get("/consultation/delete/{idConsultation}", 1))
                .andExpect(status().is3xxRedirection());

        then(consultationService).shouldHaveNoInteractions();
    }

    @WithMockUser(username = "user@test.com", roles = "UTILISATEUR")
    @Test
    void terminer() throws Exception {
        given(consultationService.terminerConsultation(1L)).willReturn(3L);
        mockMvc.perform(get("/consultation/terminerConsultation/{idConsultation}", 1))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/examenSouscrit/profil-patient/3"));
        then(consultationService).should().terminerConsultation(argumentCaptorId.capture());
        assertThat(argumentCaptorId.getValue()).isEqualTo(1L);
    }

    @Test
    void terminerUserNotLoggedIn() throws Exception {
        mockMvc.perform(get("/consultation/terminerConsultation/{idConsultation}", 1))
                .andExpect(status().is3xxRedirection());
        then(consultationService).shouldHaveNoInteractions();
    }

    @WithMockUser(username = "user@test.com", roles = "UTILISATEUR")
    @Test
    void activer() throws Exception {
        given(consultationService.activerConsultation(1L)).willReturn(3L);
        mockMvc.perform(get("/consultation/activerConsultation/{idConsultation}", 1))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/examenSouscrit/profil-patient/3"));
        then(consultationService).should().activerConsultation(argumentCaptorId.capture());
        assertThat(argumentCaptorId.getValue()).isEqualTo(1L);
    }

    @Test
    void activerUserNotLoggedIn() throws Exception {
        mockMvc.perform(get("/consultation/activerConsultation/{idConsultation}", 1))
                .andExpect(status().is3xxRedirection());
        then(consultationService).shouldHaveNoInteractions();
    }
}
