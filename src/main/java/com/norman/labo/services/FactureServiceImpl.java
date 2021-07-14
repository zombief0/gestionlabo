package com.norman.labo.services;

import com.norman.labo.entities.ExamenSouscrit;
import com.norman.labo.entities.Facture;
import com.norman.labo.entities.Utilisateur;
import com.norman.labo.repositories.ExamenSouscritRepository;
import com.norman.labo.repositories.FactureRepository;
import com.norman.labo.repositories.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FactureServiceImpl implements FactureService {
    private final FactureRepository factureRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final ExamenSouscritRepository examenSouscritRepository;

    @Override
    public Facture findByCode(Long code) {
        return factureRepository.findById(code).orElse(null);
    }

    @Override
    public Long saveFacture(String username,
                            ExamenSouscrit examenSouscrit,
                            Long codeFacture) {
        if (codeFacture == 0) {
            Utilisateur utilisateur = utilisateurRepository.findByEmail(username);
            Facture facture = new Facture(new Date(), new Date(), utilisateur, examenSouscrit.getExamen().getPrix());
            factureRepository.save(facture);
            codeFacture = facture.getIdFacture();
            examenSouscrit.setFacture(facture);
        } else {
            Facture facture = factureRepository.findByIdFacture(codeFacture);
            double so = facture.getSomme();
            so += examenSouscrit.getExamen().getPrix();
            facture.setSomme(so);
            factureRepository.save(facture);
            codeFacture = facture.getIdFacture();
            examenSouscrit.setFacture(facture);
        }
        examenSouscritRepository.save(examenSouscrit);
        return codeFacture;
    }

    @Override
    public List<Facture> findAll() {

        return factureRepository.findAll(Sort.by(Sort.Direction.DESC, "dateCreationOriginale"));
    }

    @Override
    public void delete(Long codeFacture) {
        List<ExamenSouscrit> examenSouscrits = examenSouscritRepository.findAllByFacture_IdFacture(codeFacture);

        for (ExamenSouscrit examen : examenSouscrits) {
            examen.setFacture(null);
            examenSouscritRepository.save(examen);
        }

        factureRepository.deleteById(codeFacture);
    }

    @Override
    public void updateFacture(Long id) {
        Facture facture = factureRepository.findByIdFacture(id);
        facture.setDateCreationSecondaire(new Date());
        factureRepository.save(facture);
    }
}
