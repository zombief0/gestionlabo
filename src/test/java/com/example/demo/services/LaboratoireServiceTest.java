package com.example.demo.services;

import com.example.demo.entities.Laboratoire;
import com.example.demo.repositories.LaboratoireRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        lab2.setIdLaboratoire(1L);
        laboratoires = new ArrayList<>(Arrays.asList(lab1, lab2));
    }

    @Test
    void fetchAllLaboratoire() {
        given(laboratoireRepository.findAll()).willReturn(laboratoires);

        List<Laboratoire> listeLabo = laboratoireService.fetchAllLaboratoire();
        then(laboratoireRepository).should().findAll();
        assertThat(listeLabo).hasSize(2);
    }
}
