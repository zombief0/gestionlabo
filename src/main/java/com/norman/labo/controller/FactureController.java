package com.norman.labo.controller;


import com.norman.labo.entities.ExamenSouscrit;
import com.norman.labo.entities.Facture;
import com.norman.labo.services.ExamenSouscritService;
import com.norman.labo.services.FactureService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/facture")
@RequiredArgsConstructor
public class FactureController {

    private final FactureService factureService;
    private final ExamenSouscritService examenSouscritService;
    public static String successMessage = "null";

    @GetMapping("/ajout-facture/{idPatient}/{codeFacture}")
    public String ajoutFactureForm(@PathVariable Long idPatient,
                                   @PathVariable Long codeFacture,
                                   Model model) {

        List<ExamenSouscrit> examenSouscrits = examenSouscritService.findAllByPatientAndFactureNull(idPatient);

        if (examenSouscrits.size() != 0) {
            Facture facture = factureService.findByCode(codeFacture);
            model.addAttribute("examenSouscrits", examenSouscrits);
            model.addAttribute("facture", facture);
            model.addAttribute("message", successMessage);
            model.addAttribute("examenSouscrit", new ExamenSouscrit());
            return "facture/add-facture";
        }
        return "redirect:/facture/list-facture";
    }

    @PostMapping("/enregistrer/{idPatient}/{codeFacture}")
    public String enregistrerFacture(@PathVariable Long idPatient,
                                     @PathVariable Long codeFacture,
                                     @Valid ExamenSouscrit examenSouscrit,
                                     Authentication authentication) {

        successMessage = "enregistrement réussi";
        codeFacture = factureService.saveFacture(authentication.getName(), examenSouscrit, codeFacture);
        return "redirect:/facture/ajout-facture/" + idPatient + "/" + codeFacture;
    }

    @GetMapping("/list-facture")
    public String listFacture(Model model) {
        if (!successMessage.equals("null")) {
            successMessage = "null";
        }
        List<Facture> factures = factureService.findAll();
        model.addAttribute("factures", factures);
        model.addAttribute("message", successMessage);
        return "facture/list-facture";
    }


    @GetMapping("/detail-facture/{idFacture}")
    public String afficherDetail(@PathVariable Long idFacture, Model model) {
        Facture facture = factureService.findByCode(idFacture);
        if (facture != null) {
            List<ExamenSouscrit> examenSouscrits = facture.getExamenSouscrits();
            model.addAttribute("examenSouscrits", examenSouscrits);
        }
        model.addAttribute("facture", facture);
        return "facture/detail-facture";
    }

    @GetMapping("/delete/{idFacture}")
    public String deleteFacture(@PathVariable Long idFacture) {

        successMessage = "null";
        factureService.delete(idFacture);
        return "redirect:/facture/list-facture";
    }
}
