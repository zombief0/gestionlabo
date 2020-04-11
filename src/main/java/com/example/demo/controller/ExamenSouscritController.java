package com.example.demo.controller;

import com.example.demo.entities.*;
import com.example.demo.repositories.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/Gestion_Laboratoire_EMMAUS/examenSouscrit")
public class ExamenSouscritController {

    private PatientRepository patientRepository;
    private UtilisateurRepository medecinRepository;
    private ExamenRepository examenRepository;
    private ExamenSouscritRepository examenSo;
    private ConsultationRepository consultationRepository;

    public ExamenSouscritController(PatientRepository patientRepository, UtilisateurRepository medecinRepository, ExamenRepository examenRepository, ExamenSouscritRepository examenSo, ConsultationRepository consultationRepository) {
        this.patientRepository = patientRepository;
        this.medecinRepository = medecinRepository;
        this.examenRepository = examenRepository;
        this.examenSo = examenSo;
        this.consultationRepository = consultationRepository;
    }

    @GetMapping("/profil-patient/{id}")
    public String montrerProfilPatient(@PathVariable Long id, Model model){
        initiateView(id, model);
        FactureController.successMessage = "null";
        return "examenSouscrit/profil-patient";
    }

    @GetMapping("/addValeurNormale/{idPatient}/{idExamSouscrit}")
    public String addValeurNormaleForm(@PathVariable Long idPatient,
                                       @PathVariable Long idExamSouscrit,
                                       Model model){
        Patient patient = patientRepository.findByIdPersonne(idPatient);
        ExamenSouscrit examenSouscrit = examenSo.findByIdExamenPasser(idExamSouscrit);
        model.addAttribute("patient",patient);
        model.addAttribute("examenSouscrit",examenSouscrit);
        return "examenSouscrit/add-valeur-normale";
    }

    @GetMapping("/addExamenSouscrit/{idPatient}/{idConsultation}")
    public String addExamSouscritForm(@PathVariable Long idPatient,
                                      @PathVariable Long idConsultation,
                                      Model model){
        List<Examen> examenList = examenRepository.findAll();
        Examen selectedExamen = new Examen();
        examenList.sort(Examen.examenComparator);
        model.addAttribute("examens",examenList);
        model.addAttribute("patient",idPatient);
        model.addAttribute("consultation",idConsultation);
        model.addAttribute("examen",selectedExamen);

        return "examenSouscrit/add-examen-souscrit";
    }

    @PostMapping("/enregistrerValeurNormale/{idPatient}/{idExamSouscrit}")
    public String enregistrerValeurResultat(@PathVariable Long idPatient,
                                           @PathVariable Long idExamSouscrit,
                                           HttpServletRequest request){

        ExamenSouscrit examenSouscrit = examenSo.findByIdExamenPasser(idExamSouscrit);
        double valeur = Double.parseDouble(request.getParameter("valeur"));
        String unite = request.getParameter("unite");
        examenSouscrit.setValeurNormalePatient(valeur);
        examenSouscrit.setUnite(unite);
        examenSo.save(examenSouscrit);
        return "redirect:/Gestion_Laboratoire_EMMAUS/examenSouscrit/profil-patient/" + idPatient;
    }

    @PostMapping("/enregistrer/{idPatient}/{idConsultation}")
    public String saveExamenSouscrit(@PathVariable Long idPatient,
                                     @PathVariable Long idConsultation,
                                     @Valid Examen examen,
                                     Model model,
                                     BindingResult result){
        initiateView(idPatient, model);
        if(result.hasErrors()){
            return "examenSouscrit/add-examen-souscrit";
        }

        Patient patient = patientRepository.findByIdPersonne(idPatient);
        Consultation consultation = consultationRepository.findByIdConsultation(idConsultation);
        ExamenSouscrit examenSouscrit = new ExamenSouscrit(new Date(), examen,
                consultation, patient);
        examenSouscrit.setValeurNormalePatient(-1);
        examenSo.save(examenSouscrit);

        return "redirect:/Gestion_Laboratoire_EMMAUS/examenSouscrit/profil-patient/" + idPatient;
    }

    private void initiateView(@PathVariable Long idPatient, Model model) {
        Patient patient = patientRepository.findByIdPersonne(idPatient);

        List<Consultation> consultations = consultationRepository.findAllByPatient(patient);
        List<ExamenSouscrit> examenSouscrits = patient.getExamenSouscrits();
        consultations.sort(Consultation.ConsultationComparator);
        examenSouscrits.sort(ExamenSouscrit.ExamenSouscritComparator);
        model.addAttribute("consultations",consultations);
        model.addAttribute("examenSouscrits",examenSouscrits);
        model.addAttribute("patient",patient);
    }

    @GetMapping("/delete/{idExamen}/{idPatient}")
    public String delete(@PathVariable Long idExamen,
                         @PathVariable Long idPatient){
        examenSo.deleteById(idExamen);
        return "redirect:/Gestion_Laboratoire_EMMAUS/examenSouscrit/profil-patient/" + idPatient;

    }
}
