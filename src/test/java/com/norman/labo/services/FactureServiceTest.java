package com.norman.labo.services;

import com.norman.labo.entities.Examen;
import com.norman.labo.entities.ExamenSouscrit;
import com.norman.labo.entities.Facture;
import com.norman.labo.entities.Utilisateur;
import com.norman.labo.repositories.ExamenSouscritRepository;
import com.norman.labo.repositories.FactureRepository;
import com.norman.labo.repositories.UtilisateurRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class FactureServiceTest {
    @Mock
    private FactureRepository factureRepository;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private ExamenSouscritRepository examenSouscritRepository;

    @InjectMocks
    private FactureServiceImpl factureService;

    @Captor
    private ArgumentCaptor<Facture> factureArgumentCaptor;

    @Captor
    private ArgumentCaptor<ExamenSouscrit> examenSouscritArgumentCaptor;
    @Test
    void findByCode() {
        Facture testFacture = new Facture();
        testFacture.setIdFacture(7L);
        given(factureRepository.findById(anyLong())).willReturn(Optional.of(testFacture));

        Facture facture = factureService.findByCode(7L);

        then(factureRepository).should().findById(anyLong());
        assertThat(facture).isNotNull();
    }

    @Test
    void saveFactureNewFacture() {
        // Given
        Facture testFacture = new Facture();
        testFacture.setIdFacture(7L);
        Examen examen = new Examen();
        examen.setPrix(1200);
        ExamenSouscrit examenSouscrit = new ExamenSouscrit();
        examenSouscrit.setExamen(examen);
        given(utilisateurRepository.findByEmail(anyString())).willReturn(new Utilisateur());
        given(factureRepository.save(any(Facture.class))).willReturn(testFacture);
        given(examenSouscritRepository.save(any(ExamenSouscrit.class))).willReturn(examenSouscrit);

        // When
        Long codeFacture = factureService.saveFacture("user@mail.com", examenSouscrit, 0L);

        // Then
        then(factureRepository).should().save(factureArgumentCaptor.capture());
        assertThat(codeFacture).isEqualTo(factureArgumentCaptor.getValue().getIdFacture());
        then(factureRepository).shouldHaveNoMoreInteractions();
        then(examenSouscritRepository).should().save(examenSouscritArgumentCaptor.capture());

        assertThat(examenSouscritArgumentCaptor.getValue().getFacture()).isNotNull();
    }

    @Test
    void saveFactureOldFacture() {
        Facture testFacture = new Facture();
        testFacture.setIdFacture(7L);
        testFacture.setSomme(2000);
        Examen examen = new Examen();
        examen.setPrix(1200);
        ExamenSouscrit examenSouscrit = new ExamenSouscrit();
        examenSouscrit.setExamen(examen);
        given(factureRepository.findByIdFacture(anyLong())).willReturn(testFacture);
        given(factureRepository.save(any(Facture.class))).willReturn(testFacture);
        given(examenSouscritRepository.save(any(ExamenSouscrit.class))).willReturn(examenSouscrit);

        factureService.saveFacture("user@mail.com", examenSouscrit, 7L);

        then(factureRepository).should().findByIdFacture(anyLong());
        then(factureRepository).should().save(factureArgumentCaptor.capture());
        assertThat(factureArgumentCaptor.getValue().getSomme()).isEqualTo(3200);
        then(examenSouscritRepository).should().save(examenSouscritArgumentCaptor.capture());
        then(utilisateurRepository).shouldHaveNoInteractions();
        assertThat(examenSouscritArgumentCaptor.getValue().getFacture()).isNotNull();
    }

    @Test
    void findAll() {
        Facture testFacture = new Facture();
        testFacture.setIdFacture(7L);
        given(factureRepository.findAll(any(Sort.class))).willReturn(Collections.singletonList(testFacture));

        List<Facture> factures = factureService.findAll();

        then(factureRepository).should().findAll(any(Sort.class));
        assertThat(factures.size()).isEqualTo(1);
    }

    @Test
    void delete() {
        Facture testFacture = new Facture();
        testFacture.setIdFacture(7L);
        testFacture.setSomme(2000);
        ExamenSouscrit examenSouscrit = new ExamenSouscrit();
        examenSouscrit.setFacture(testFacture);
        ExamenSouscrit examenSouscrit1 = new ExamenSouscrit();
        examenSouscrit1.setFacture(testFacture);
        given(examenSouscritRepository.findAllByFacture_IdFacture(anyLong())).willReturn(Arrays.asList(examenSouscrit1, examenSouscrit));

        factureService.delete(7L);

        then(examenSouscritRepository).should(times(2)).save(any(ExamenSouscrit.class));
        then(factureRepository).should().deleteById(anyLong());
    }
}
