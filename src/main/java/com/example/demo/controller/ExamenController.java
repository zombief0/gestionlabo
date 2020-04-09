package com.example.demo.controller;

import com.example.demo.entities.Examen;
import com.example.demo.repositories.ExamenRepository;
import com.example.demo.repositories.TypeExamenRepository;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/Gestion_Laboratoire_EMMAUS/examen")
public class ExamenController {
    private ExamenRepository examenRepository;
    private TypeExamenRepository typeExamenRepository;

    public ExamenController(ExamenRepository examenRepository, TypeExamenRepository typeExamenRepository) {
        this.examenRepository = examenRepository;
        this.typeExamenRepository = typeExamenRepository;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder){
        binder.registerCustomEditor(String.class,new StringTrimmerEditor(true));
    }

    @GetMapping("/listExamen")
    public String listeExamen(Model model){
        List<Examen> examens = examenRepository.findAll();
        examens.sort(Examen.examenComparator);
        model.addAttribute("examens",examens);
        return "examen/list-examen";
    }

    @GetMapping("/ajout-examen")
    public String ajoutExamenForm(Model model){
        Examen examen = new Examen();
        model.addAttribute("examen",examen);
        return "examen/add-examen";
    }

    @PostMapping("/enregistrer")
    public String enregistrerExamen(@Valid Examen examen, BindingResult result,Model model){
        if(result.hasErrors()){
            return "/examen/add-examen";
        }

        examen.setDateAjout(new Date());
        if(examen.getDescription() ==null){
            examen.setDescription("");
        }
        examenRepository.save(examen);
        return "redirect:/Gestion_Laboratoire_EMMAUS/examen/listExamen";
    }

    @GetMapping("/editer/{id}")
    public String editerFormExamen(@PathVariable Long id,Model model){
        Examen examen = examenRepository.findByIdExamen(id);
        model.addAttribute("examen",examen);
        return "examen/modifier-examen";
    }

    @PostMapping("/modifier/{id}")
    public String modifierExamen(@PathVariable Long id,@Valid Examen examen,BindingResult result,Model model){
        examen.setIdExamen(id);
        if(result.hasErrors()){
            model.addAttribute("examen",examen);
            return "examen/modifier-examen";
        }
        Examen examen1 = examenRepository.findByIdExamen(id);
        examen.setDateAjout(examen1.getDateAjout());
        examenRepository.save(examen);
        return "redirect:/Gestion_Laboratoire_EMMAUS/examen/listExamen";
    }

    @GetMapping("/delete/{id}")
    public String deleteExamen(@PathVariable Long id){
        examenRepository.deleteById(id);
        return "redirect:/Gestion_Laboratoire_EMMAUS/examen/listExamen";
    }
}
