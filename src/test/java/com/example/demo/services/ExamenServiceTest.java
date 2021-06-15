package com.example.demo.services;

import com.example.demo.entities.Examen;
import com.example.demo.entities.Laboratoire;
import com.example.demo.repositories.ExamenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ExamenServiceTest {
    @InjectMocks
    private ExamenServiceImpl examenService;

    @Mock
    private ExamenRepository examenRepository;

    @Captor
    private ArgumentCaptor<Examen> examenArgumentCaptor;

    @Captor
    private ArgumentCaptor<Laboratoire> laboratoireArgumentCaptor;
    @Captor
    private ArgumentCaptor<Examen> examenToUpdateArgumentCaptor;
    private Examen examen;
    private Laboratoire laboratoire;
    private List<Examen> examenList;

    @BeforeEach
    void setUp() {
        laboratoire = new Laboratoire();
        laboratoire.setLibelle("Lab 1");
        laboratoire.setIdLaboratoire(2L);
        laboratoire.setDescription("Desc Lab 1");
        examen = new Examen();
        examen.setIdExamen(1L);
        examen.setLibelle("Examen Test");
        examen.setLaboratoire(laboratoire);

        examenList = new ArrayList<>(Collections.singleton(examen));

    }

    @Test
    void getAllExamenByLaboratoire() {
        given(examenRepository.findAllByLaboratoire_IdLaboratoireOrderByLibelle(2L)).willReturn(examenList);

        examenService.getAllByLaboratoire(2L);

        then(examenRepository).should().findAllByLaboratoire_IdLaboratoireOrderByLibelle(2L);
    }

    @Test
    void fetchExamById() {
        given(examenRepository.findById(1L)).willReturn(Optional.of(examen));

        examenService.fetchExamById(1L);

        then(examenRepository).should().findById(1L);
    }

    @Test
    void updateExam() {
        Examen updatedExamen = new Examen();
        updatedExamen.setPrix(500);
        updatedExamen.setCode("code");
        updatedExamen.setLibelle("libelle");
        updatedExamen.setMinValeur(2);
        updatedExamen.setMaxValeur(3);

        examenService.updateExam(updatedExamen, examen);

        then(examenRepository).should().save(examenArgumentCaptor.capture());
        assertThat(examenArgumentCaptor.getValue().getLibelle()).isEqualTo(updatedExamen.getLibelle());
        assertThat(examenArgumentCaptor.getValue().getMaxValeur()).isEqualTo(updatedExamen.getMaxValeur());
        assertThat(examenArgumentCaptor.getValue().getMinValeur()).isEqualTo(updatedExamen.getMinValeur());
        assertThat(examenArgumentCaptor.getValue().getPrix()).isEqualTo(updatedExamen.getPrix());
    }

    @Test
    void saveExamen() {
        Examen examenToSave = new Examen();
        examenToSave.setPrix(500);
        examenToSave.setCode("code");
        examenToSave.setLibelle("libelle");
        examenToSave.setMinValeur(2);
        examenToSave.setMaxValeur(3);
        examenService.saveExamen(examenToSave, laboratoire);

        then(examenRepository).should().save(examenArgumentCaptor.capture());
        assertThat(examenArgumentCaptor.getValue().getLaboratoire()).isNotNull();
        assertThat(examenArgumentCaptor.getValue().getDateAjout()).isNotNull();
    }

    @Test
    void deleteExamenById() {
        given(examenRepository.findById(1L)).willReturn(Optional.of(examen));

        Long idLaboratoire = examenService.deleteExamenById(1L);

        then(examenRepository).should().findById(1L);
        then(examenRepository).should().deleteById(1L);
        assertThat(idLaboratoire).isEqualTo(2L);
    }
}
