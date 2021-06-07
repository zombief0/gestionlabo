package com.example.demo.services;

import com.example.demo.entities.Laboratoire;
import com.example.demo.repositories.LaboratoireRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class LaboratoireServiceTest {

    @Mock
    private LaboratoireRepository laboratoireRepository;

    @InjectMocks
    private LaboratoireServiceImpl laboratoireService;

    @Captor
    private ArgumentCaptor<Laboratoire> laboratoireArgumentCaptor;

    @Captor
    private ArgumentCaptor<Long> idArgumentCaptor;
    Laboratoire lab1;
    Laboratoire lab2;
    List<Laboratoire> laboratoires;

    @BeforeEach
    void setUp() {
        lab1 = new Laboratoire();
        lab1.setIdLaboratoire(1L);
        lab1.setLibelle("Labo 1");
        lab2 = new Laboratoire();
        lab2.setLibelle("Labo 2");
        lab2.setIdLaboratoire(2L);
        laboratoires = new ArrayList<>(Arrays.asList(lab1, lab2));
    }

    @Test
    void fetchAllLaboratoire() {
        given(laboratoireRepository.findAll()).willReturn(laboratoires);

        List<Laboratoire> listeLabo = laboratoireService.fetchAllLaboratoire();
        then(laboratoireRepository).should().findAll();
        assertThat(listeLabo).hasSize(2);
    }

    @Test
    void saveLaboratoire() {
        given(laboratoireRepository.save(lab1)).willReturn(lab1);

        laboratoireService.saveLaboratoire(lab1);

        then(laboratoireRepository).should().save(lab1);
    }

    @DisplayName("Get laboratoire by ID")
    @Test
    void fetchById() {
        given(laboratoireRepository.findById(1L)).willReturn(Optional.of(lab1));

        Laboratoire foundLaboratoire = laboratoireService.fetchById(1L);

        then(laboratoireRepository).should().findById(1L);
        assertThat(foundLaboratoire).isNotNull();
    }

    @Test
    void updateLaboratoire() {
        Laboratoire lab3 = new Laboratoire();
        lab3.setLibelle("Labo 3");
        given(laboratoireRepository.findById(1L)).willReturn(Optional.of(lab1));

        laboratoireService.updateLaboratoire(1L, lab3);

        then(laboratoireRepository).should().findById(1L);
        then(laboratoireRepository).should().save(laboratoireArgumentCaptor.capture());

        assertThat(laboratoireArgumentCaptor.getValue().getLibelle()).isEqualTo(lab3.getLibelle());
        assertThat(laboratoireArgumentCaptor.getValue().getDescription()).isEqualTo(lab3.getDescription());
        assertThat(laboratoireArgumentCaptor.getValue().getIdLaboratoire()).isEqualTo(1L);
    }

    @Test
    void deleteLaboratoire() {
        given(laboratoireRepository.findById(1L)).willReturn(Optional.of(lab1));

        laboratoireService.deleteLaboratoire(1L);

        then(laboratoireRepository).should().findById(1L);
        then(laboratoireRepository).should().deleteById(idArgumentCaptor.capture());

        assertThat(idArgumentCaptor.getValue()).isEqualTo(1L);
    }
}
