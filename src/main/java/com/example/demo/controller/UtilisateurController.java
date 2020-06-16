package com.example.demo.controller;

import com.example.demo.DTO.UtilisateurDTO;
import com.example.demo.entities.Utilisateur;
import com.example.demo.repositories.ExamenSouscritRepository;
import com.example.demo.repositories.UtilisateurRepository;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/Gestion_Laboratoire_EMMAUS/")
public class UtilisateurController {

    @InitBinder
    public void initBinder(WebDataBinder binder){
        binder.registerCustomEditor(String.class,new StringTrimmerEditor(true));
    }

    private UtilisateurRepository utilisateurRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private ExamenSouscritRepository examenSo;

    public UtilisateurController(UtilisateurRepository utilisateurRepository, ExamenSouscritRepository examenSo) {
        this.utilisateurRepository = utilisateurRepository;
        this.examenSo = examenSo;
    }


    @GetMapping("user")
    @ResponseBody
    public String currentUser(Authentication authentication){
        return authentication.getName();
    }

    @GetMapping("user/list")
    public String listUser(Model model){
        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
        utilisateurs.sort(Utilisateur.utilisateurComparator);
        model.addAttribute("utilisateurs",utilisateurs);
        return "utilisateur/list-user";
    }

    @GetMapping("user/add-user")
    public String adduserForm(Model model){
        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        model.addAttribute("utilisateur",utilisateurDTO);
        return "/utilisateur/add-user";
    }

    @PostMapping("user/enregistrer")
    public String enregistrerUser(@ModelAttribute("utilisateur") @Valid UtilisateurDTO utilisateur, BindingResult bindingResult,Model model){
        if(bindingResult.hasErrors()){
            return "utilisateur/add-user";
        }

        String pwd = utilisateur.getMdp();
        pwd = bCryptPasswordEncoder.encode(pwd);
        if(utilisateur.getPrenom() !=null) {
            Utilisateur utilisateur1 = new Utilisateur(utilisateur.getNom(),
                    utilisateur.getPrenom(), utilisateur.getEmail(),
                    utilisateur.getDate(), utilisateur.getTelephone(),
                    utilisateur.getSexe(), pwd, utilisateur.getRole());
            utilisateur1.setActive(true);
            utilisateurRepository.save(utilisateur1);
        }else {
            Utilisateur utilisateur1 = new Utilisateur(utilisateur.getNom(),
                    "", utilisateur.getEmail(),
                    utilisateur.getDate(), utilisateur.getTelephone(),
                    utilisateur.getSexe(), pwd, utilisateur.getRole());
            utilisateur1.setActive(true);
            utilisateurRepository.save(utilisateur1);
        }
        return "redirect:/Gestion_Laboratoire_EMMAUS/user/list";
    }

    @GetMapping("user/editer/{id}")
    public String modifierUserForm(@PathVariable Long id, Model model){
        Utilisateur utilisateur = utilisateurRepository.findByIdPersonne(id);
        UtilisateurDTO utilisateurDTO = new UtilisateurDTO(utilisateur.getIdPersonne(),utilisateur.getNom(),
                utilisateur.getPrenom(),utilisateur.getTelephone(), utilisateur.getDate(), utilisateur.getSexe(),
                utilisateur.getMdp(),utilisateur.getRole(),utilisateur.getEmail(),"");
        model.addAttribute("utilisateur",utilisateurDTO);
        return "utilisateur/modifier-user";
    }

    @GetMapping("user/editerAdmin/{id}")
    public String modifierAdminForm(@PathVariable Long id,Model model){
        Utilisateur utilisateur = utilisateurRepository.findByIdPersonne(id);
        UtilisateurDTO utilisateurDTO = new UtilisateurDTO(utilisateur.getIdPersonne(),utilisateur.getNom(),
                utilisateur.getPrenom(),utilisateur.getTelephone(), utilisateur.getDate(), utilisateur.getSexe(),
                utilisateur.getMdp(),utilisateur.getRole(),utilisateur.getEmail(),"");
        utilisateurDTO.setOldPassword("");
        model.addAttribute("utilisateur",utilisateurDTO);
        return "utilisateur/modifier-admin";
    }

    @PostMapping("user/modifierAdmin/{id}")
    public String updateAdminUser(@PathVariable Long id,@Valid UtilisateurDTO utilisateur,BindingResult bindingResult,Model model){
        String ancienMdp = utilisateur.getOldPassword();
        Utilisateur utilisateur2 = utilisateurRepository.findByIdPersonne(id);

        if(ancienMdp != null) {
            if(!bCryptPasswordEncoder.matches(ancienMdp, utilisateur2.getMdp())){
                if (bindingResult.hasErrors()) {
                    utilisateur.setId(id);
                    utilisateur.setOldPassword("test");
                    model.addAttribute("utilisateur", utilisateur);
                    return "utilisateur/modifier-admin";
                }
            }
        }else {
            utilisateur.setId(id);
            utilisateur.setOldPassword("");
            model.addAttribute("utilisateur", utilisateur);
            return "utilisateur/modifier-admin";
        }
        String pwd = utilisateur.getMdp();
        pwd = bCryptPasswordEncoder.encode(pwd);


            utilisateur2.setEmail(utilisateur.getEmail());
            utilisateur2.setNom(utilisateur.getNom());
            utilisateur2.setPrenom(utilisateur.getPrenom());
            utilisateur2.setMdp(pwd);
            utilisateur2.setSexe(utilisateur.getSexe());
            utilisateur2.setTelephone(utilisateur.getTelephone());
            utilisateurRepository.save(utilisateur2);
        return "redirect:/Gestion_Laboratoire_EMMAUS/user/list";


    }

    @PostMapping("user/modifier/{id}")
    public String updateUser(@PathVariable Long id,@Valid UtilisateurDTO utilisateur,BindingResult bindingResult,Model model){

        if(bindingResult.hasErrors()){
            model.addAttribute("utilisateur",utilisateur);
            return "utilisateur/modifier-user";
        }

        String pwd = utilisateur.getMdp();
        pwd = bCryptPasswordEncoder.encode(pwd);

            Utilisateur utilisateur2 = utilisateurRepository.findByIdPersonne(id);
            utilisateur2.setTelephone(utilisateur.getTelephone());
            utilisateur2.setSexe(utilisateur.getSexe());
            utilisateur2.setMdp(pwd);
            utilisateur2.setPrenom(utilisateur.getPrenom());
            utilisateur2.setNom(utilisateur.getNom());
            utilisateur2.setEmail(utilisateur.getEmail());
            utilisateurRepository.save(utilisateur2);
        return "redirect:/Gestion_Laboratoire_EMMAUS/user/list";


    }

    @GetMapping("user/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id){
            utilisateurRepository.deleteById(id);
        return "redirect:/Gestion_Laboratoire_EMMAUS/user/list";
    }

    @GetMapping("user/deactivateUser/{id}")
    public String deactivate(@PathVariable Long id){
        Utilisateur utilisateur = utilisateurRepository.findByIdPersonne(id);
        utilisateur.setActive(false);
        utilisateurRepository.save(utilisateur);
        return "redirect:/Gestion_Laboratoire_EMMAUS/user/list";
    }

    @GetMapping("user/activateUser/{id}")
    public String activate(@PathVariable Long id){
        Utilisateur utilisateur = utilisateurRepository.findByIdPersonne(id);
        utilisateur.setActive(true);
        utilisateurRepository.save(utilisateur);
        return "redirect:/Gestion_Laboratoire_EMMAUS/user/list";
    }
}
