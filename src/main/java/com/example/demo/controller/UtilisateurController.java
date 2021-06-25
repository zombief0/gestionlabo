package com.example.demo.controller;

import com.example.demo.DTO.UtilisateurDTO;
import com.example.demo.entities.Utilisateur;
import com.example.demo.repositories.UtilisateurRepository;
import com.example.demo.services.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UtilisateurController {

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private final UtilisateurService utilisateurService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping("/list")
    public String listUser(Model model) {

        model.addAttribute("utilisateurs", utilisateurService.fetchAll());
        return "utilisateur/list-user";
    }

    @GetMapping("/add-user")
    public String adduserForm(Model model) {
        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        model.addAttribute("utilisateur", utilisateurDTO);
        return "utilisateur/add-user";
    }

    @PostMapping("/enregistrer")
    public String enregistrerUser(@ModelAttribute("utilisateur") @Valid UtilisateurDTO utilisateur, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "utilisateur/add-user";
        }

        utilisateurService.save(utilisateur);
        return "redirect:/user/list";
    }

    @GetMapping("/editer/{id}")
    public String modifierUserForm(@PathVariable Long id, Model model) {
        model.addAttribute("utilisateur", utilisateurService.fetchById(id));
        return "utilisateur/modifier-user";
    }

    @GetMapping("/editerAdmin/{id}")
    public String modifierAdminForm(@PathVariable Long id, Model model) {
        UtilisateurDTO utilisateurDTO = utilisateurService.fetchById(id);
        model.addAttribute("utilisateur", utilisateurDTO);
        return "utilisateur/modifier-admin";
    }

    @PostMapping("/modifierAdmin/{id}")
    public String updateAdminUser(@PathVariable Long id,
                                  @ModelAttribute("utilisateur") @Valid UtilisateurDTO utilisateur,
                                  BindingResult bindingResult, Model model) {
        if (utilisateur.getOldPassword() == null || bindingResult.hasErrors()) {
            utilisateur.setId(id);
            utilisateur.setOldPassword(null);
            model.addAttribute("utilisateur", utilisateur);
            return "utilisateur/modifier-admin";
        }

        if (utilisateurService.updateAdmin(id, utilisateur)) {
            return "redirect:/user/list";
        }
        utilisateur.setId(id);
        utilisateur.setOldPassword(null);
        model.addAttribute("utilisateur", utilisateur);
        return "utilisateur/modifier-admin";

    }

    @PostMapping("/modifier/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute("utilisateur") @Valid UtilisateurDTO utilisateur, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            utilisateur.setId(id);
            model.addAttribute("utilisateur", utilisateur);
            return "utilisateur/modifier-user";
        }

        utilisateurService.updateUser(id, utilisateur);
        return "redirect:/user/list";


    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id) {
        utilisateurService.deleteUser(id);
        return "redirect:/user/list";
    }

    @GetMapping("/activateDeactivateUser/{id}")
    public String activateDeactivate(@PathVariable Long id) {
        /*Utilisateur utilisateur = utilisateurRepository.findByIdPersonne(id);
        utilisateur.setActive(false);
        utilisateurRepository.save(utilisateur);*/
        utilisateurService.activateDeactivateUser(id);
        return "redirect:/user/list";
    }

}
