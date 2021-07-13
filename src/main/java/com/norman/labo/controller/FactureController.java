package com.norman.labo.controller;


import com.norman.labo.entities.ExamenSouscrit;
import com.norman.labo.entities.Facture;
import com.norman.labo.entities.Utilisateur;
import com.norman.labo.repositories.ExamenSouscritRepository;
import com.norman.labo.repositories.FactureRepository;
import com.norman.labo.repositories.PatientRepository;
import com.norman.labo.repositories.UtilisateurRepository;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/Gestion_Laboratoire_EMMAUS/facture")
public class FactureController {

    private FactureRepository factureRepository;
    private UtilisateurRepository utilisateurRepository;
    private PatientRepository patientRepository;
    private ExamenSouscritRepository examenSo;
    private DataSource dataSource;
    private ResourceLoader resourceLoader;
    public static String successMessage = "null";

    public FactureController(FactureRepository factureRepository, UtilisateurRepository utilisateurRepository, PatientRepository patientRepository, ExamenSouscritRepository examenSo, DataSource dataSource, ResourceLoader resourceLoader) {
        this.factureRepository = factureRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.patientRepository = patientRepository;
        this.examenSo = examenSo;
        this.dataSource = dataSource;
        this.resourceLoader = resourceLoader;
    }

    @GetMapping("/ajout-facture/{idPatient}/{codeFacture}")
    public String ajoutFactureForm(@PathVariable Long idPatient,
                                    @PathVariable Long codeFacture,
                                    Model model){

        List<ExamenSouscrit> examenSouscrits = examenSo.findAllByPatientAndFactureIsNull(
                patientRepository.findByIdPersonne(idPatient)
        );
        if(examenSouscrits.size() != 0) {
            ExamenSouscrit examenSouscrit = new ExamenSouscrit();
            Facture facture = factureRepository.findByIdFacture(codeFacture);
            model.addAttribute("examenSouscrits", examenSouscrits);
            model.addAttribute("facture", facture);
            model.addAttribute("message", successMessage);
            model.addAttribute("examenSouscrit", examenSouscrit);
            return "facture/add-facture";
        }else {
            return "redirect:/Gestion_Laboratoire_EMMAUS/facture/list-facture";
        }
    }

    @PostMapping("/enregistrer/{idPatient}/{codeFacture}")
    public String enregistrerFacture(@PathVariable Long idPatient,
                                     @PathVariable Long codeFacture,
                                     @Valid ExamenSouscrit examenSouscrit,
                                     BindingResult result,
                                     Model model,
                                     Authentication authentication){

        successMessage = "enregistrement réussi";
        if(codeFacture == 0) {
            Utilisateur utilisateur = utilisateurRepository.findByEmail(authentication.getName());
            Facture facture = new Facture(new Date(),new Date(),utilisateur,examenSouscrit.getExamen().getPrix());
            factureRepository.save(facture);
            examenSouscrit.setFacture(facture);
            examenSo.save(examenSouscrit);
            return "redirect:/Gestion_Laboratoire_EMMAUS/facture/ajout-facture/"+ idPatient+"/" + facture.getIdFacture();
        }else {
            Facture facture = factureRepository.findByIdFacture(codeFacture);
            double so = facture.getSomme();
            so +=examenSouscrit.getExamen().getPrix();
            facture.setSomme(so);
            factureRepository.save(facture);
            examenSouscrit.setFacture(facture);
            examenSo.save(examenSouscrit);
            return "redirect:/Gestion_Laboratoire_EMMAUS/facture/ajout-facture/" + idPatient + "/" + codeFacture;
        }
    }

    @GetMapping("/list-facture")
    public String listFacture(Model model){
        if(!successMessage.equals("null")) {
            successMessage = "null";
        }
        List<Facture> factures = factureRepository.findAll();
        factures.sort(Facture.FactureComparator);
        model.addAttribute("factures",factures);
        model.addAttribute("message",successMessage);
        return "facture/list-facture";
    }

    @GetMapping("/print-facture/{idFacture}")
    public String creerPdfFacture(@PathVariable Long idFacture, HttpServletResponse response) throws IOException, SQLException, JRException {
        successMessage = "pdf généré! consultez le dossier Documents";

        //
        response.setContentType("application/pdf");
        Facture facture = factureRepository.findByIdFacture(idFacture);
        facture.setDateCreationSecondaire(new Date());
        factureRepository.save(facture);
        JasperDesign jasperDesign = JRXmlLoader.load(resourceLoader.getResource("classpath:static/etat.jrxml").getInputStream());

        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("idFacture", idFacture );
        BufferedImage image = ImageIO.read(resourceLoader.getResource("classpath:static/logo1.png").getInputStream());
        parameters.put("logo",image);
        ConsultationController.printState(response, jasperReport, parameters, dataSource);

        return "redirect:/Gestion_Laboratoire_EMMAUS/facture/list-facture";
    }

    @GetMapping("/detail-facture/{idFacture}")
    public String afficherDetail(@PathVariable Long idFacture,Model model){
        Facture facture = factureRepository.findByIdFacture(idFacture);
        List<ExamenSouscrit> examenSouscrits = examenSo.findAllByFacture(facture);
        model.addAttribute("examenSouscrits",examenSouscrits);
        model.addAttribute("facture",facture);
        return "facture/detail-facture";
    }

    @GetMapping("/delete/{idFacture}")
    public String deleteFacture(@PathVariable Long idFacture){
        List<ExamenSouscrit> examenSouscrits = examenSo.findAllByFacture(
                factureRepository.findByIdFacture(idFacture)
        );

        for( ExamenSouscrit examen : examenSouscrits){
            examen.setFacture(null);
            examenSo.save(examen);
        }

        factureRepository.deleteById(idFacture);
        successMessage = "null";
        return "redirect:/Gestion_Laboratoire_EMMAUS/facture/list-facture";
    }
}
