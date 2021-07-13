package com.norman.labo.controller;

import com.norman.labo.DTO.UtilisateurDTO;
import com.norman.labo.entities.Utilisateur;
import com.norman.labo.services.UtilisateurService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UtilisateurController.class)
class UtilisateurControllerAdminTestIT extends BaseControllerAdminTest {

    @MockBean
    private UtilisateurService utilisateurService;

    @Captor
    private ArgumentCaptor<UtilisateurDTO> utilisateurArgumentCaptor;

    @Captor
    private ArgumentCaptor<Long> idCaptor;

    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "adminTest@mail.com")
    @Test
    void listUser() throws Exception {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom("User 1");
        Utilisateur utilisateur1 = new Utilisateur();
        utilisateur1.setNom("User 2");
        given(utilisateurService.fetchAll()).willReturn(Arrays.asList(utilisateur, utilisateur1));
        mockMvc.perform(get("/user/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("utilisateurs"))
                .andExpect(view().name("utilisateur/list-user"));

        then(utilisateurService).should().fetchAll();
    }

    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "adminTest@mail.com")
    @Test
    void adduserForm() throws Exception {
        mockMvc.perform(get("/user/add-user"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("utilisateur"))
                .andExpect(view().name("utilisateur/add-user"));
    }

    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "adminTest@mail.com")
    @Test
    void enregistrerUser() throws Exception {

        mockMvc.perform(post("/user/enregistrer")
                .with(csrf())
                .param("nom", "Mbouende")
                .param("email", "mbouendenorman@gmail.com")
                .param("mdp", "12354")
                .param("conf", "12354"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/list"));

        then(utilisateurService).should().save(utilisateurArgumentCaptor.capture());
        assertThat(utilisateurArgumentCaptor.getValue().getEmail()).isEqualTo("mbouendenorman@gmail.com");
        assertThat(utilisateurArgumentCaptor.getValue().getMdp()).isEqualTo("12354");
        assertThat(utilisateurArgumentCaptor.getValue().getNom()).isEqualTo("Mbouende");
    }

    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "adminTest@mail.com")
    @Test
    void enregistrerUserEmailError() throws Exception {

        mockMvc.perform(post("/user/enregistrer")
                .with(csrf())
                .param("nom", "Mbouende")
                .param("email", "mbouendenormangmail.com")
                .param("mdp", "12354")
                .param("conf", "12354"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("utilisateur"))
                .andExpect(model().attributeHasFieldErrors("utilisateur", "email"))
                .andExpect(view().name("utilisateur/add-user"));

    }

    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "adminTest@mail.com")
    @Test
    void enregistrerUserMdpNotMatch() throws Exception {

        mockMvc.perform(post("/user/enregistrer")
                .with(csrf())
                .param("nom", "Mbouende")
                .param("email", "mbouendenorman@gmail.com")
                .param("mdp", "12354")
                .param("conf", "1254"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("utilisateur"))
                .andExpect(view().name("utilisateur/add-user"));

    }

    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "adminTest@mail.com")
    @Test
    void enregistrerUserNomNullEmailNull() throws Exception {

        mockMvc.perform(post("/user/enregistrer")
                .with(csrf())
                .param("mdp", "12354")
                .param("conf", "12354"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("utilisateur"))
                .andExpect(model().attributeHasFieldErrors("utilisateur", "email", "nom"))
                .andExpect(view().name("utilisateur/add-user"));

    }

    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "adminTest@mail.com")
    @Test
    void modifierUserForm() throws Exception {
        UtilisateurDTO utilisateur = new UtilisateurDTO();
        utilisateur.setNom("User 1");
        utilisateur.setEmail("user@mail.com");
        given(utilisateurService.fetchById(anyLong())).willReturn(utilisateur);

        mockMvc.perform(get("/user/editer/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("utilisateur"))
                .andExpect(view().name("utilisateur/modifier-user"));

        then(utilisateurService).should().fetchById(1L);
    }

    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "adminTest@mail.com")
    @Test
    void modifierAdminForm() throws Exception {
        UtilisateurDTO utilisateur = new UtilisateurDTO();
        utilisateur.setNom("Admin 1");
        utilisateur.setEmail("admin@mail.com");
        given(utilisateurService.fetchById(anyLong())).willReturn(utilisateur);

        mockMvc.perform(get("/user/editerAdmin/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("utilisateur"))
                .andExpect(view().name("utilisateur/modifier-admin"));

        then(utilisateurService).should().fetchById(1L);
    }

    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "adminTest@mail.com")
    @Test
    void updateAdminUser() throws Exception {
        given(utilisateurService.updateAdmin(anyLong(), any(UtilisateurDTO.class))).willReturn(true);
        mockMvc.perform(post("/user/modifierAdmin/{id}", 1)
                .with(csrf())
                .param("nom", "Mbouende")
                .param("email", "mbouendenorman@gmail.com")
                .param("mdp", "1234")
                .param("conf", "1234")
                .param("oldPassword", "12354"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/list"));
        then(utilisateurService).should().updateAdmin(anyLong(), any(UtilisateurDTO.class));
    }

    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "adminTest@mail.com")
    @Test
    void updateAdminUserOldPasswordNotMatching() throws Exception {
        given(utilisateurService.updateAdmin(anyLong(), any(UtilisateurDTO.class))).willReturn(false);
        mockMvc.perform(post("/user/modifierAdmin/{id}", 1)
                .with(csrf())
                .param("nom", "Mbouende")
                .param("email", "mbouendenorman@gmail.com")
                .param("mdp", "1234")
                .param("conf", "1234")
                .param("oldPassword", "12354"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("utilisateur"))
                .andExpect(view().name("utilisateur/modifier-admin"));
        then(utilisateurService).should().updateAdmin(anyLong(), any(UtilisateurDTO.class));

    }

    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "adminTest@mail.com")
    @Test
    void updateAdminUserEmailNomInvalid() throws Exception {
        mockMvc.perform(post("/user/modifierAdmin/{id}", 1)
                .with(csrf())
                .param("mdp", "1234")
                .param("conf", "1234")
                .param("oldPassword", "12354"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("utilisateur", "nom", "email"))
                .andExpect(view().name("utilisateur/modifier-admin"));
        then(utilisateurService).shouldHaveNoInteractions();

    }

    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "adminTest@mail.com")
    @Test
    void updateAdminUserPasswordNotMatching() throws Exception {
        mockMvc.perform(post("/user/modifierAdmin/{id}", 1)
                .with(csrf())
                .param("nom", "Mbouende")
                .param("email", "mbouendenorman@gmail.com")
                .param("mdp", "1234")
                .param("conf", "12354")
                .param("oldPassword", "12354"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("utilisateur"))
                .andExpect(view().name("utilisateur/modifier-admin"));
        then(utilisateurService).shouldHaveNoInteractions();

    }

    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "adminTest@mail.com")
    @Test
    void updateAdminNotOldPassword() throws Exception {
        mockMvc.perform(post("/user/modifierAdmin/{id}", 1)
                .with(csrf())
                .param("nom", "Mbouende")
                .param("email", "mbouendenorman@gmail.com")
                .param("mdp", "1234")
                .param("conf", "1234"))
                .andExpect(status().isOk())
                .andExpect(view().name("utilisateur/modifier-admin"));
        then(utilisateurService).shouldHaveNoInteractions();

    }

    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "adminTest@mail.com")
    @Test
    void updateUser() throws Exception {
        mockMvc.perform(post("/user/modifier/{id}", 2)
                .with(csrf())
                .param("nom", "Mbouende")
                .param("email", "mbouendenorman@gmail.com")
                .param("mdp", "12354")
                .param("conf", "12354"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/list"));

        then(utilisateurService).should().updateUser(idCaptor.capture(), utilisateurArgumentCaptor.capture());
        assertThat(idCaptor.getValue()).isEqualTo(2);
        assertThat(utilisateurArgumentCaptor.getValue()).isNotNull();
    }

    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "adminTest@mail.com")
    @Test
    void updateUserUserEmailInvalid() throws Exception {
        mockMvc.perform(post("/user/modifier/{id}", 2)
                .with(csrf())
                .param("mdp", "12354")
                .param("conf", "12354"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("utilisateur", "nom", "email"))
                .andExpect(view().name("utilisateur/modifier-user"));

        then(utilisateurService).shouldHaveNoInteractions();

    }

    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "adminTest@mail.com")
    @Test
    void updateUserPasswordNotMatch() throws Exception {
        mockMvc.perform(post("/user/modifier/{id}", 2)
                .with(csrf())
                .param("nom", "Mbouende")
                .param("email", "mbouendenorman@gmail.com")
                .param("mdp", "12354")
                .param("conf", "123546"))
                .andExpect(status().isOk())
                .andExpect(view().name("utilisateur/modifier-user"));

        then(utilisateurService).shouldHaveNoInteractions();
    }

    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "adminTest@mail.com")
    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(get("/user/deleteUser/{id}", 1))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/list"));

        then(utilisateurService).should().deleteUser(idCaptor.capture());
        assertThat(idCaptor.getValue()).isEqualTo(1L);
    }

    @WithUserDetails(userDetailsServiceBeanName = "utilisateurDetailService", value = "adminTest@mail.com")
    @Test
    void activateDeactivate() throws Exception {
        mockMvc.perform(get("/user/activateDeactivateUser/{id}", 1))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/list"));
        then(utilisateurService).should().activateDeactivateUser(anyLong());

    }

}
