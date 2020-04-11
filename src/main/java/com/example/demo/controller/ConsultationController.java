package com.example.demo.controller;

import com.example.demo.entities.Consultation;
import com.example.demo.entities.Patient;
import com.example.demo.entities.Utilisateur;
import com.example.demo.repositories.ConsultationRepository;
import com.example.demo.repositories.PatientRepository;
import com.example.demo.repositories.UtilisateurRepository;
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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/Gestion_Laboratoire_EMMAUS/consultation")
public class ConsultationController {
    private ConsultationRepository consultationRepository;
    private PatientRepository patientRepository;
    private UtilisateurRepository utilisateurRepository;
    private ResourceLoader resourceLoader;
    private DataSource dataSource;



    public ConsultationController(ConsultationRepository consultationRepository, PatientRepository patientRepository, UtilisateurRepository utilisateurRepository, ResourceLoader resourceLoader, DataSource dataSource) {
        this.consultationRepository = consultationRepository;
        this.patientRepository = patientRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.resourceLoader = resourceLoader;
        this.dataSource = dataSource;
    }

    @GetMapping("/print-resultat/{idPatient}/{idConsultation}")
    public String imprimerResultat(@PathVariable Long idConsultation,
                                   @PathVariable Long idPatient,
                                   HttpServletResponse response) throws JRException, IOException, SQLException {

        response.setContentType("application/pdf");
        JasperDesign jasperDesign = JRXmlLoader.load(resourceLoader.getResource("classpath:static/resultat.jrxml").getInputStream());

        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("idCons", idConsultation );
        URL r = resourceLoader.getResource("classpath:static/logo1.png").getURL();
        printState(response, jasperReport, parameters, r, dataSource);

        return "redirect:/Gestion_Laboratoire_EMMAUS/examenSouscrit/profil-patient/" + idPatient;

    }

    static void printState(HttpServletResponse response, JasperReport jasperReport, Map<String, Object> parameters, URL r, DataSource dataSource) throws SQLException, JRException, IOException {
        parameters.put("logo",r.getPath());
        Connection connection = dataSource.getConnection();
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters, connection);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint,byteArrayOutputStream);
        response.setContentLength(byteArrayOutputStream.size());
        ServletOutputStream svl = response.getOutputStream();
        byteArrayOutputStream.writeTo(svl);
        svl.flush();
        connection.close();
    }

    @PostMapping("/enregistrer/{idPatient}")
    public String enregistrerConsultation(@PathVariable Long idPatient,
                                          HttpServletRequest request,
                                          Authentication authentication){

        Utilisateur medecin = utilisateurRepository.findByEmail(authentication.getName());
        Patient patient = patientRepository.findByIdPersonne(idPatient);
        if(!medecin.getRole().equals("MEDECIN")) {
            String prescripteur = request.getParameter("prescripteur");
            Consultation consultation = new Consultation(prescripteur, patient, new Date());
            consultation.setStatut("EN_COURS");
            consultationRepository.save(consultation);
        }else {
            Consultation consultation = new Consultation(medecin.getNom(), patient, new Date());
            consultation.setStatut("EN_COURS");

            consultationRepository.save(consultation);
        }

        return "redirect:/Gestion_Laboratoire_EMMAUS/examenSouscrit/profil-patient/" + idPatient;
    }

    @GetMapping("/delete/{idConsultation}/{idPatient}")
    public String delete(@PathVariable Long idConsultation,
                         @PathVariable Long idPatient){
        consultationRepository.deleteById(idConsultation);
        return "redirect:/Gestion_Laboratoire_EMMAUS/examenSouscrit/profil-patient/" + idPatient;

    }

    @GetMapping("/terminerConsultation/{idConsultation}/{idPatient}")
    public String terminer(@PathVariable Long idConsultation,
                           @PathVariable Long idPatient){
        Consultation consultation = consultationRepository.findByIdConsultation(idConsultation);
        consultation.setStatut("TERMINE");
        consultationRepository.save(consultation);
        return "redirect:/Gestion_Laboratoire_EMMAUS/examenSouscrit/profil-patient/" + idPatient;

    }

    @GetMapping("/activerConsultation/{idConsultation}/{idPatient}")
    public String activer(@PathVariable Long idConsultation,
                          @PathVariable Long idPatient){
        Consultation consultation = consultationRepository.findByIdConsultation(idConsultation);
        consultation.setStatut("EN_COURS");
        consultationRepository.save(consultation);
        return "redirect:/Gestion_Laboratoire_EMMAUS/examenSouscrit/profil-patient/" + idPatient;

    }
}
