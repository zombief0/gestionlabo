package com.example.demo.controller;


import com.example.demo.entities.ExamenSouscrit;
import com.example.demo.entities.Facture;
import com.example.demo.entities.Patient;
import com.example.demo.entities.Utilisateur;
import com.example.demo.repositories.ExamenSouscritRepository;
import com.example.demo.repositories.FactureRepository;
import com.example.demo.repositories.PatientRepository;
import com.example.demo.repositories.UtilisateurRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
    public String creerPdfFacture(@PathVariable Long idFacture, HttpServletResponse response) throws IOException, SQLException, InterruptedException, JRException {
        successMessage = "pdf généré! consultez le dossier Documents";

        //
        Facture facture = factureRepository.findByIdFacture(idFacture);
        facture.setDateCreationSecondaire(new Date());
        factureRepository.save(facture);
        JasperDesign jasperDesign = JRXmlLoader.load(resourceLoader.getResource("classpath:static/etat.jrxml").getInputStream());

        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("idFacture", idFacture );

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters, dataSource.getConnection());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        response.setContentType("application/pdf");
        /*SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String myDocumentDirectory = System.getProperty("user.home") + "//Documents//facture_" + patient.getNom()+"_"
                +patient.getPrenom()+"_"+ format.format(facture.getDateCreationSecondaire()) +"_"+format.format(facture.getDateCreationOriginale())+".pdf";
        JasperExportManager.exportReportToPdfFile(jasperPrint,myDocumentDirectory);
        String[] cmdOpenPdf = {"cmd","/C", myDocumentDirectory};

        Runtime runtime = Runtime.getRuntime();
         runtime.exec(cmdOpenPdf);*/
        JasperExportManager.exportReportToPdfStream(jasperPrint,byteArrayOutputStream);
        response.setContentLength(byteArrayOutputStream.size());
        ServletOutputStream svl = response.getOutputStream();
        byteArrayOutputStream.writeTo(svl);
        svl.flush();


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
