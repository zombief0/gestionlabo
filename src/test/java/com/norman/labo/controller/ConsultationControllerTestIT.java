package com.norman.labo.controller;

import com.norman.labo.services.ConsultationService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.test.context.support.WithUserDetails;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(ConsultationController.class)
class ConsultationControllerTestIT extends BaseControllerUserAndAnonymousTest {

    @MockBean
    private ResourceLoader resourceLoader;

    @MockBean
    private DataSource dataSource;

    @Captor
    private ArgumentCaptor<String> argumentCaptorUsername;

    @Captor
    private ArgumentCaptor<Long> argumentCaptorId;

    @Captor
    private ArgumentCaptor<String> argumentCaptorPrescripteur;

    @MockBean
    private ConsultationService consultationService;

    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "userTest@mail.com")
    @Test
    void enregistrerConsultation() throws Exception {
        mockMvc.perform(post("/consultation/enregistrer/{idPatient}", 5)
                .with(csrf())
                .param("prescripteur", "Norman Mbouende"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/examenSouscrit/profil-patient/5"));

        then(consultationService).should().saveConsultation(argumentCaptorUsername.capture(),
                argumentCaptorId.capture(), argumentCaptorPrescripteur.capture());
        assertThat(argumentCaptorUsername.getValue()).isEqualTo("userTest@mail.com");
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

    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "userTest@mail.com")
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

    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "userTest@mail.com")
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

    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "userTest@mail.com")
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
