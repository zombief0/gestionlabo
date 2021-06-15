package com.example.demo.services;

import com.example.demo.entities.Examen;
import com.example.demo.entities.Laboratoire;
import com.example.demo.repositories.ExamenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class ExamenServiceImpl implements ExamenService{

    private final ExamenRepository examenRepository;
    @Override
    public List<Examen> getAllByLaboratoire(Long idLaboratoire) {
        return examenRepository.findAllByLaboratoire_IdLaboratoireOrderByLibelle(idLaboratoire);
    }

    @Override
    public void saveExamen(Examen examen, Laboratoire laboratoire) {
        examen.setLaboratoire(laboratoire);
        examen.setDateAjout(new Date());
        examenRepository.save(examen);
    }

    @Override
    public Examen fetchExamById(Long id) {
        return examenRepository.findById(id).orElse(null);
    }

    @Override
    public void updateExam(Examen examen, Examen precedentValue) {
        precedentValue.setLibelle(examen.getLibelle());
        precedentValue.setCode(examen.getCode());
        precedentValue.setMaxValeur(examen.getMaxValeur());
        precedentValue.setMinValeur(examen.getMinValeur());
        precedentValue.setPrix(examen.getPrix());
        precedentValue.setDescription(examen.getDescription());
        examenRepository.save(precedentValue);
    }

    @Override
    public Long deleteExamenById(Long idExamen) {
        AtomicReference<Long> idLab = new AtomicReference<>();
        Optional<Examen> examen = examenRepository.findById(idExamen);
        examen.ifPresent(examenToDelete -> {
            if (examenToDelete.getExamenSouscrits().size() == 0){
                idLab.set(examenToDelete.getLaboratoire().getIdLaboratoire());
                examenRepository.deleteById(idExamen);
            }
        });
        return idLab.get();
    }
}
