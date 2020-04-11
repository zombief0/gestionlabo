package com.example.demo.controller;

import com.example.demo.entities.Laboratoire;
import com.example.demo.repositories.LaboratoireRepository;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/Gestion_Laboratoire_EMMAUS/laboratoire")
public class LaboratoireController {

    private LaboratoireRepository laboratoireRepository;

    public LaboratoireController(LaboratoireRepository laboratoireRepository) {
        this.laboratoireRepository = laboratoireRepository;
    }

    @GetMapping("/listLaboratoire")
    public String listelabo(Model model){
        List<Laboratoire> laboratoires = laboratoireRepository.findAll();
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

        laboratoireRepository.save(laboratoire);
        return "redirect:/Gestion_Laboratoire_EMMAUS/laboratoire/listLaboratoire";
    }

    @GetMapping("/editer/{id}")
    public String editerFormLabo(@PathVariable Long id, Model model){
        Laboratoire laboratoire = laboratoireRepository.findByIdLaboratoire(id);
        model.addAttribute("laboratoire",laboratoire);
        return "laboratoire/modifier-laboratoire";
    }

    @PostMapping("/modifier/{id}")
    public String modifierLaboratoire(@PathVariable Long id,
                                      @Valid Laboratoire laboratoire,
                                      BindingResult result, Model model){
        laboratoire.setIdLaboratoire(id);
        if(result.hasErrors()){
            model.addAttribute("laboratoire",laboratoire);
            return "laboratoire/modifier-laboratoire";
        }

        laboratoireRepository.save(laboratoire);
        return "redirect:/Gestion_Laboratoire_EMMAUS/laboratoire/listLaboratoire";
    }

    @GetMapping("/delete/{id}")
    public String deleteLaboratoire(@PathVariable Long id){
        laboratoireRepository.deleteById(id);
        return "redirect:/Gestion_Laboratoire_EMMAUS/laboratoire/listLaboratoire";
    }

}
