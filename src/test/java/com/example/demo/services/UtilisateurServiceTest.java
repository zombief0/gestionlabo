package com.example.demo.services;

import com.example.demo.DTO.UtilisateurDTO;
import com.example.demo.entities.Utilisateur;
import com.example.demo.repositories.UtilisateurRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class UtilisateurServiceTest {
    @Mock
    private UtilisateurRepository utilisateurRepository;
    @InjectMocks
    private UtilisateurServiceImpl utilisateurService;

    @Captor
    private ArgumentCaptor<Utilisateur> utilisateurArgumentCaptor;
    @Test
    void fetchAll() {

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom("User 1");
        Utilisateur utilisateur1 = new Utilisateur();
        utilisateur1.setNom("User 2");
        List<Utilisateur> givenUtilisateurs = Arrays.asList(utilisateur, utilisateur1);
        given(utilisateurRepository.findAll(any(Sort.class))).willReturn(givenUtilisateurs);

        List<Utilisateur> utilisateurs = utilisateurService.fetchAll();

        then(utilisateurRepository).should().findAll(any(Sort.class));
        assertThat(utilisateurs.size()).isEqualTo(givenUtilisateurs.size());
    }

    @Test
    void save() {
        UtilisateurDTO utilisateur = new UtilisateurDTO();
        utilisateur.setNom("User 1");
        utilisateur.setMdp("123648");
        utilisateur.setEmail("user@email.com");

        utilisateurService.save(utilisateur);

        then(utilisateurRepository).should().save(utilisateurArgumentCaptor.capture());
        assertThat(utilisateurArgumentCaptor.getValue()).isNotNull();
    }

    @Test
    void fetchById() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setIdPersonne(1L);
        utilisateur.setNom("User 1");
        utilisateur.setMdp("123648");
        utilisateur.setEmail("user@email.com");

        given(utilisateurRepository.findById(anyLong())).willReturn(Optional.of(utilisateur));

        UtilisateurDTO utilisateurFound = utilisateurService.fetchById(1L);

        then(utilisateurRepository).should().findById(anyLong());
        assertThat(utilisateurFound).isNotNull();
    }

    @Test
    void fetchByIdUserNull() {

        UtilisateurDTO utilisateurFound = utilisateurService.fetchById(1L);

        then(utilisateurRepository).should().findById(anyLong());
        assertThat(utilisateurFound).isNull();
    }

    @Test
    void updateAdmin() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setIdPersonne(1L);
        utilisateur.setNom("User 1");
        utilisateur.setMdp("$2a$10$PMSKbLsTOUTR3kcFYT1M0ed27HDA8BnGOxZYAMaoZIeEZaAiXXMg2");
        utilisateur.setEmail("user@email.com");

        given(utilisateurRepository.findById(1L)).willReturn(Optional.of(utilisateur));

        UtilisateurDTO utilisateurToUp = new UtilisateurDTO();
        utilisateurToUp.setNom("User 1");
        utilisateurToUp.setMdp("12348");
        utilisateurToUp.setOldPassword("1234");
        utilisateurToUp.setEmail("user@email.com");

        boolean result = utilisateurService.updateAdmin(1L, utilisateurToUp);

        then(utilisateurRepository).should().findById(1L);
        then(utilisateurRepository).should().save(utilisateurArgumentCaptor.capture());
        assertThat(utilisateurArgumentCaptor.getValue()).isNotNull();
        assertThat(result).isTrue();
    }

    @Test
    void updateUserOldPasswordNotMatch() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setIdPersonne(1L);
        utilisateur.setNom("User 1");
        utilisateur.setMdp("$2a$10$PMSKbLsTOUTR3kcFYT1M0ed27HDA8BnGOxZYAMaoZIeEZaAiXXMg2");
        utilisateur.setEmail("user@email.com");

        given(utilisateurRepository.findById(1L)).willReturn(Optional.of(utilisateur));

        UtilisateurDTO utilisateurToUp = new UtilisateurDTO();
        utilisateurToUp.setNom("User 1");
        utilisateurToUp.setMdp("12348");
        utilisateurToUp.setOldPassword("69966");
        utilisateurToUp.setEmail("user@email.com");

        boolean result = utilisateurService.updateAdmin(1L, utilisateurToUp);

        then(utilisateurRepository).should().findById(1L);
        then(utilisateurRepository).shouldHaveNoMoreInteractions();
        assertThat(result).isFalse();
    }

    @Test
    void updateUser() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setIdPersonne(1L);
        utilisateur.setNom("User 1");
        utilisateur.setMdp("$2a$10$PMSKbLsTOUTR3kcFYT1M0ed27HDA8BnGOxZYAMaoZIeEZaAiXXMg2");
        utilisateur.setEmail("user@email.com");

        given(utilisateurRepository.findById(1L)).willReturn(Optional.of(utilisateur));

        UtilisateurDTO utilisateurToUp = new UtilisateurDTO();
        utilisateurToUp.setNom("User 1");
        utilisateurToUp.setMdp("12348");
        utilisateurToUp.setOldPassword("69966");
        utilisateurToUp.setEmail("user@email.com");

        utilisateurService.updateUser(1L, utilisateurToUp);
        then(utilisateurRepository).should().findById(anyLong());
        then(utilisateurRepository).should().save(utilisateurArgumentCaptor.capture());
        assertThat(utilisateurArgumentCaptor.getValue()).isNotNull();
    }

    @Test
    void deleteUser() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setIdPersonne(1L);
        utilisateur.setNom("User 1");
        utilisateur.setMdp("$2a$10$PMSKbLsTOUTR3kcFYT1M0ed27HDA8BnGOxZYAMaoZIeEZaAiXXMg2");
        utilisateur.setEmail("user@email.com");

        given(utilisateurRepository.findById(anyLong())).willReturn(Optional.of(utilisateur));

        utilisateurService.deleteUser(1L);

        then(utilisateurRepository).should().findById(anyLong());
        then(utilisateurRepository).should().deleteById(1L);
    }

    @Test
    void activateUser() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setIdPersonne(1L);
        utilisateur.setNom("User 1");
        utilisateur.setMdp("$2a$10$PMSKbLsTOUTR3kcFYT1M0ed27HDA8BnGOxZYAMaoZIeEZaAiXXMg2");
        utilisateur.setEmail("user@email.com");
        utilisateur.setActive(true);

        given(utilisateurRepository.findById(anyLong())).willReturn(Optional.of(utilisateur));

        utilisateurService.activateDeactivateUser(1L);

        then(utilisateurRepository).should().save(utilisateurArgumentCaptor.capture());
        assertThat(utilisateurArgumentCaptor.getValue().isActive()).isFalse();
    }

    @Test
    void deactivate() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setIdPersonne(1L);
        utilisateur.setNom("User 1");
        utilisateur.setMdp("$2a$10$PMSKbLsTOUTR3kcFYT1M0ed27HDA8BnGOxZYAMaoZIeEZaAiXXMg2");
        utilisateur.setEmail("user@email.com");
        utilisateur.setActive(false);

        given(utilisateurRepository.findById(anyLong())).willReturn(Optional.of(utilisateur));

        utilisateurService.activateDeactivateUser(1L);

        then(utilisateurRepository).should().save(utilisateurArgumentCaptor.capture());
        assertThat(utilisateurArgumentCaptor.getValue().isActive()).isTrue();
    }
}
