package com.example.demo.controller;

import com.example.demo.entities.Consultation;
import com.example.demo.entities.Examen;
import com.example.demo.entities.ExamenSouscrit;
import com.example.demo.entities.Patient;
import com.example.demo.services.ConsultationService;
import com.example.demo.services.ExamenService;
import com.example.demo.services.ExamenSouscritService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/examenSouscrit")
@RequiredArgsConstructor
public class ExamenSouscritController {

    private final ConsultationService consultationService;
    private final ExamenSouscritService examenSouscritService;
    private final ExamenService examenService;

    @GetMapping("/profil-patient/{id}")
    public String montrerProfilPatient(@PathVariable Long id, Model model){
        List<Consultation> consultations = consultationService.fetchAllByIdPatient(id);
        if (consultations.size() != 0){
            if (consultations.get(0).getPatient() != null){
                model.addAttribute("patient",consultations.get(0).getPatient());
            }
        }
        model.addAttribute("consultations",consultations);
        FactureController.successMessage = "null";
        return "examenSouscrit/profil-patient";
    }

    @GetMapping("/addValeurNormale/{idExamSouscrit}")
    public String addValeurNormaleForm(@PathVariable Long idExamSouscrit,
                                       Model model){
        ExamenSouscrit examenSouscrit = examenSouscritService.fetchById(idExamSouscrit);
        Patient patient = examenSouscrit.getPatient();
        model.addAttribute("patient",patient);
        model.addAttribute("examenSouscrit",examenSouscrit);
        return "examenSouscrit/add-valeur-normale";
    }

    @GetMapping("/addExamenSouscrit/{idPatient}/{idConsultation}")
    public String addExamSouscritForm(@PathVariable Long idPatient,
                                      @PathVariable Long idConsultation,
                                      Model model){
        List<Examen> examenList = examenService.fetchAll();
        model.addAttribute("examens",examenList);
        model.addAttribute("patient",idPatient);
        model.addAttribute("consultation",idConsultation);
        model.addAttribute("examen",new Examen());

        return "examenSouscrit/add-examen-souscrit";
    }

    @PostMapping("/enregistrerValeurNormale/{idExamSouscrit}")
    public String enregistrerValeurResultat(@PathVariable Long idExamSouscrit,
                                           HttpServletRequest request){

        double valeur = Double.parseDouble(request.getParameter("valeur"));
        String unite = request.getParameter("unite");
        Long idPatient = examenSouscritService.updateExamenSouscrit(idExamSouscrit, valeur, unite);
        return "redirect:/examenSouscrit/profil-patient/" + idPatient;
    }

    @PostMapping("/enregistrer/{idConsultation}")
    public String saveExamenSouscrit(@PathVariable Long idConsultation,
                                     @Valid Examen examen){

        Long idPatient = examenSouscritService.saveExamSouscrit(idConsultation, examen);

        return "redirect:/examenSouscrit/profil-patient/" + idPatient;
    }

    @GetMapping("/delete/{idExamen}")
    public String delete(@PathVariable Long idExamen){
        Long idPatient = examenSouscritService.deleteById(idExamen);
        return "redirect:/examenSouscrit/profil-patient/" + idPatient;

    }
}
