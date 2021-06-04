package com.example.demo.services;

import com.example.demo.entities.Laboratoire;
import com.example.demo.repositories.LaboratoireRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LaboratoireServiceImpl implements LaboratoireService {
    private final LaboratoireRepository laboratoireRepository;
    @Override
    public List<Laboratoire> fetchAllLaboratoire() {
        return laboratoireRepository.findAll();
    }
}
