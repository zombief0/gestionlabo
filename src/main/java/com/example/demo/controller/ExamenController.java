package com.example.demo.controller;

import com.example.demo.entities.Examen;
import com.example.demo.entities.Laboratoire;
import com.example.demo.repositories.ExamenRepository;
import com.example.demo.repositories.LaboratoireRepository;
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
    private LaboratoireRepository laboratoireRepository;

    public ExamenController(ExamenRepository examenRepository, LaboratoireRepository laboratoireRepository) {
        this.examenRepository = examenRepository;
        this.laboratoireRepository = laboratoireRepository;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder){
        binder.registerCustomEditor(String.class,new StringTrimmerEditor(true));
    }

    @GetMapping("/listExamen/{idLaboratoire}")
    public String listeExamen(@PathVariable Long idLaboratoire,Model model){
        Laboratoire laboratoire = laboratoireRepository.findByIdLaboratoire(idLaboratoire);
        List<Examen> examens = examenRepository.findAllByLaboratoire(
                laboratoire);
        examens.sort(Examen.examenComparator);
        model.addAttribute("examens",examens);
        model.addAttribute("laboratoire",laboratoire);
        return "examen/list-examen";
    }

    @GetMapping("/ajout-examen/{idLaboratoire}")
    public String ajoutExamenForm(@PathVariable Long idLaboratoire, Model model){
        Examen examen = new Examen();
        Laboratoire laboratoire = laboratoireRepository.findByIdLaboratoire(idLaboratoire);
        model.addAttribute("laboratoire",laboratoire);
        model.addAttribute("examen",examen);
        return "examen/add-examen";
    }

    @PostMapping("/enregistrer/{idLaboratoire}")
    public String enregistrerExamen(@Valid Examen examen, @PathVariable Long idLaboratoire, BindingResult result,Model model){
        if(result.hasErrors()){
            return "/examen/add-examen";
        }
        Laboratoire laboratoire = laboratoireRepository.findByIdLaboratoire(idLaboratoire);

        examen.setDateAjout(new Date());
        if(examen.getDescription() ==null){
            examen.setDescription("");
        }
        examen.setLaboratoire(laboratoire);
        examenRepository.save(examen);
        return "redirect:/Gestion_Laboratoire_EMMAUS/examen/listExamen/" + examen.getLaboratoire().getIdLaboratoire();
    }

    @GetMapping("/editer/{id}/{idLaboratoire}")
    public String editerFormExamen(@PathVariable Long id, @PathVariable Long idLaboratoire, Model model){
        Examen examen = examenRepository.findByIdExamen(id);
        Laboratoire laboratoire = laboratoireRepository.findByIdLaboratoire(idLaboratoire);
        model.addAttribute("laboratoire",laboratoire);
        model.addAttribute("examen",examen);
        return "examen/modifier-examen";
    }

    @PostMapping("/modifier/{id}/{idLaboratoire}")
    public String modifierExamen(@PathVariable Long id, @PathVariable Long idLaboratoire, @Valid Examen examen,BindingResult result,Model model){
        examen.setIdExamen(id);
        if(result.hasErrors()){
            model.addAttribute("examen",examen);
            return "examen/modifier-examen";
        }
        Examen examen1 = examenRepository.findByIdExamen(id);
        examen.setDateAjout(examen1.getDateAjout());
        Laboratoire laboratoire = laboratoireRepository.findByIdLaboratoire(idLaboratoire);
        examen.setLaboratoire(laboratoire);
        examenRepository.save(examen);
        return "redirect:/Gestion_Laboratoire_EMMAUS/examen/listExamen/" + examen.getLaboratoire().getIdLaboratoire();
    }

    @GetMapping("/delete/{id}")
    public String deleteExamen(@PathVariable Long id){
        Examen examen = examenRepository.findByIdExamen(id);
        examenRepository.deleteById(id);
        return "redirect:/Gestion_Laboratoire_EMMAUS/examen/listExamen/" + examen.getLaboratoire().getIdLaboratoire();
    }
}
