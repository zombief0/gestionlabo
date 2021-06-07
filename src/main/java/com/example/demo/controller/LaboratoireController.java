package com.example.demo.controller;

import com.example.demo.entities.Laboratoire;
import com.example.demo.repositories.LaboratoireRepository;
import com.example.demo.services.LaboratoireService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/laboratoire")
@RequiredArgsConstructor
public class LaboratoireController {

    private final LaboratoireService laboratoireService;

    @GetMapping("/listLaboratoire")
    public String listelabo(Model model){
        List<Laboratoire> laboratoires = laboratoireService.fetchAllLaboratoire();
        model.addAttribute("laboratoires",laboratoires);
        return "laboratoire/list-laboratoire";
    }

    @GetMapping("/add-laboratoire")
    public String addLaboratoireForm(Model model){
        Laboratoire laboratoire = new Laboratoire();
        model.addAttribute("laboratoire",laboratoire);
        return "laboratoire/add-laboratoire";
    }

    @PostMapping("/enregistrer")
    public String enregistrerLaboratoire(@Valid Laboratoire laboratoire,
                                         BindingResult bindingResult,
                                         Model model){
        if(bindingResult.hasErrors()){
            return "laboratoire/add-laboratoire";
        }

        laboratoireService.saveLaboratoire(laboratoire);
        return "redirect:/laboratoire/listLaboratoire";
    }

    @GetMapping("/editer/{id}")
    public String editerFormLabo(@PathVariable Long id, Model model){
        Laboratoire laboratoire = laboratoireService.fetchById(id);
        model.addAttribute("laboratoire",laboratoire);
        return "laboratoire/modifier-laboratoire";
    }

    @PostMapping("/modifier/{id}")
    public String modifierLaboratoire(@PathVariable Long id,
                                      @Valid Laboratoire laboratoire,
                                      BindingResult result, Model model){
        if(result.hasErrors()){
            laboratoire.setIdLaboratoire(id);
            model.addAttribute("laboratoire",laboratoire);
            return "laboratoire/modifier-laboratoire";
        }

        laboratoireService.updateLaboratoire(id, laboratoire);
        return "redirect:/laboratoire/listLaboratoire";
    }

    @GetMapping("/delete/{id}")
    public String deleteLaboratoire(@PathVariable Long id){
        laboratoireService.deleteLaboratoire(id);
        return "redirect:/laboratoire/listLaboratoire";
    }

}
