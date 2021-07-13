package com.norman.labo.services;

import com.norman.labo.entities.Laboratoire;
import com.norman.labo.repositories.LaboratoireRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LaboratoireServiceImpl implements LaboratoireService {
    private final LaboratoireRepository laboratoireRepository;

    @Override
    public List<Laboratoire> fetchAllLaboratoire() {
        return laboratoireRepository.findAll();
    }

    @Override
    public void saveLaboratoire(Laboratoire laboratoire) {
        laboratoireRepository.save(laboratoire);
    }

    @Override
    public Laboratoire fetchById(Long id) {
        return laboratoireRepository.findById(id).orElse(null);
    }

    @Override
    public void updateLaboratoire(Long idLabo, Laboratoire laboratoire) {
        Optional<Laboratoire> laboratoireToUpdate = laboratoireRepository.findById(idLabo);
        laboratoireToUpdate.ifPresent(l -> {
            l.setDescription(laboratoire.getDescription());
            l.setLibelle(laboratoire.getLibelle());
            laboratoireRepository.save(l);
        });
    }

    @Override
    public void deleteLaboratoire(Long id) {
        Optional<Laboratoire> laboratoireToDelete = laboratoireRepository.findById(id);
        laboratoireToDelete.ifPresent(laboratoire -> {
            if (laboratoire.getExamenList().size() == 0){
                laboratoireRepository.deleteById(id);
            }
        });
    }
}
