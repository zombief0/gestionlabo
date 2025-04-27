package com.norman.labo.controller;

import com.norman.labo.entities.Examen;
import com.norman.labo.entities.Laboratoire;
import com.norman.labo.services.ExamenService;
import com.norman.labo.services.LaboratoireService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/examen")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ExamenController {
    private final ExamenService examenService;
    private final LaboratoireService laboratoireService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping("/listExamen/{idLaboratoire}")
    public String listeExamen(@PathVariable Long idLaboratoire, Model model) {
        List<Examen> examens = examenService.getAllByLaboratoire(idLaboratoire);

        model.addAttribute("laboratoire", laboratoireService.fetchById(idLaboratoire));
        model.addAttribute("examens", examens);
        return "examen/list-examen";
    }

    @GetMapping("/ajout-examen/{idLaboratoire}")
    public String ajoutExamenForm(@PathVariable Long idLaboratoire, Model model) {
        Examen examen = new Examen();
        Laboratoire laboratoire = laboratoireService.fetchById(idLaboratoire);
        model.addAttribute("laboratoire", laboratoire);
        model.addAttribute("examen", examen);
        return "examen/add-examen";
    }

    @PostMapping("/enregistrer/{idLaboratoire}")
    public String enregistrerExamen(@PathVariable Long idLaboratoire,
                                    @Valid Examen examen,
                                    BindingResult result, Model model) {
        Laboratoire laboratoire = laboratoireService.fetchById(idLaboratoire);

        if (result.hasErrors()) {
            model.addAttribute("laboratoire", laboratoire);
            return "examen/add-examen";
        }

        examenService.saveExamen(examen, laboratoire);
        return "redirect:/examen/listExamen/" + laboratoire.getIdLaboratoire();
    }

    @GetMapping("/editer/{id}")
    public String editerFormExamen(@PathVariable Long id, Model model, Authentication authentication) {
        Examen examen = examenService.fetchExamById(id);
        Laboratoire laboratoire = examen.getLaboratoire();
        model.addAttribute("laboratoire", laboratoire);
        model.addAttribute("examen", examen);
        return "examen/modifier-examen";
    }

    @PostMapping("/modifier/{id}")
    public String modifierExamen(@PathVariable Long id, @Valid Examen examen, BindingResult result, Model model) {
        Examen lastValueExam = examenService.fetchExamById(id);
        examen.setIdExamen(id);
        examen.setLaboratoire(lastValueExam.getLaboratoire());
        if (result.hasErrors()) {
            model.addAttribute("examen", examen);
            model.addAttribute("laboratoire", examen.getLaboratoire());
            return "examen/modifier-examen";
        }
        examenService.updateExam(examen, lastValueExam);
        return "redirect:/examen/listExamen/" + examen.getLaboratoire().getIdLaboratoire();
    }

    @GetMapping("/delete/{id}")
    public String deleteExamen(@PathVariable Long id) {
        Long idLaboratoire = examenService.deleteExamenById(id);
        return "redirect:/examen/listExamen/" + idLaboratoire;
    }
}
