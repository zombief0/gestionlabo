package com.norman.labo.services;

import com.norman.labo.DTO.UtilisateurDTO;
import com.norman.labo.entities.Utilisateur;
import com.norman.labo.repositories.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UtilisateurServiceImpl implements UtilisateurService {
    private final UtilisateurRepository utilisateurRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public List<Utilisateur> fetchAll() {
        return utilisateurRepository.findAll(Sort.by(Sort.Direction.ASC, "nom"));
    }

    @Override
    public void save(UtilisateurDTO utilisateur) {
        String pwd = utilisateur.getMdp();
        pwd = bCryptPasswordEncoder.encode(pwd);
        Utilisateur utilisateur1;
        if (utilisateur.getPrenom() == null) {
            utilisateur.setPrenom("");
        }
        utilisateur1 = new Utilisateur(utilisateur.getNom(),
                utilisateur.getPrenom(), utilisateur.getEmail(),
                utilisateur.getDate(), utilisateur.getTelephone(),
                utilisateur.getSexe(), pwd, utilisateur.getRole());
        utilisateur1.setActive(true);
        utilisateurRepository.save(utilisateur1);
    }

    @Override
    public UtilisateurDTO fetchById(Long id) {
        Optional<Utilisateur> utilisateurOp = utilisateurRepository.findById(id);
        if (utilisateurOp.isPresent()){
            Utilisateur utilisateur = utilisateurOp.get();
            return new UtilisateurDTO(utilisateur.getIdPersonne(), utilisateur.getNom(),
                    utilisateur.getPrenom(), utilisateur.getTelephone(), utilisateur.getDate(), utilisateur.getSexe(),
                    utilisateur.getMdp(), utilisateur.getRole(), utilisateur.getEmail(), "");
        }
        return null;
    }

    @Override
    public boolean updateAdmin(Long id, UtilisateurDTO utilisateur) {
        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findById(id);
        if (utilisateurOptional.isPresent()){
            Utilisateur user = utilisateurOptional.get();
            if (bCryptPasswordEncoder.matches(utilisateur.getOldPassword(), user.getMdp())){
                String pwd = utilisateur.getMdp();
                pwd = bCryptPasswordEncoder.encode(pwd);

                user.setEmail(utilisateur.getEmail());
                user.setNom(utilisateur.getNom());
                user.setPrenom(utilisateur.getPrenom());
                user.setMdp(pwd);
                user.setSexe(utilisateur.getSexe());
                user.setTelephone(utilisateur.getTelephone());
                utilisateurRepository.save(user);
                return true;
            }
        }
        return false;
    }

    @Override
    public void updateUser(Long id, UtilisateurDTO utilisateur) {
        Optional<Utilisateur> optionalUtilisateur = utilisateurRepository.findById(id);
        if (optionalUtilisateur.isPresent()){
            String pwd = utilisateur.getMdp();
            pwd = bCryptPasswordEncoder.encode(pwd);

            Utilisateur utilisateur2 = optionalUtilisateur.get();
            utilisateur2.setTelephone(utilisateur.getTelephone());
            utilisateur2.setSexe(utilisateur.getSexe());
            utilisateur2.setMdp(pwd);
            utilisateur2.setPrenom(utilisateur.getPrenom());
            utilisateur2.setNom(utilisateur.getNom());
            utilisateur2.setEmail(utilisateur.getEmail());
            utilisateurRepository.save(utilisateur2);
        }
    }

    @Override
    public void deleteUser(Long id) {
        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findById(id);
        utilisateurOptional.ifPresent(utilisateur -> {
            if (utilisateur.getFactures().size() == 0){
                utilisateurRepository.deleteById(id);
            }
        });
    }

    @Override
    public void activateDeactivateUser(Long id) {
        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findById(id);
        utilisateurOptional.ifPresent(utilisateur -> {
            utilisateur.setActive(!utilisateur.isActive());
            utilisateurRepository.save(utilisateur);
        });
    }
}
