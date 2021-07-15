package com.norman.labo.controller;

import com.norman.labo.services.ConsultationService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/consultation")
@RequiredArgsConstructor
public class ConsultationController {

    private final ConsultationService consultationService;

    @PostMapping("/enregistrer/{idPatient}")
    public String enregistrerConsultation(@PathVariable Long idPatient,
                                          HttpServletRequest request,
                                          Authentication authentication) {

        consultationService.saveConsultation(authentication.getName(), idPatient, request.getParameter("prescripteur"));

        return "redirect:/examenSouscrit/profil-patient/" + idPatient;
    }

    @GetMapping("/delete/{idConsultation}")
    public String delete(@PathVariable Long idConsultation) {
        Long idPatient = consultationService.deleteConsultationById(idConsultation);
        return "redirect:/examenSouscrit/profil-patient/" + idPatient;

    }

    @GetMapping("/terminerConsultation/{idConsultation}")
    public String terminer(@PathVariable Long idConsultation) {
        Long idPatient = consultationService.terminerConsultation(idConsultation);
        return "redirect:/examenSouscrit/profil-patient/" + idPatient;

    }

    @GetMapping("/activerConsultation/{idConsultation}")
    public String activer(@PathVariable Long idConsultation) {
        Long idPatient = consultationService.activerConsultation(idConsultation);
        return "redirect:/examenSouscrit/profil-patient/" + idPatient;

    }
}
