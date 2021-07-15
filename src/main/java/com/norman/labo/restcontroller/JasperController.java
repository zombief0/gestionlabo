package com.norman.labo.restcontroller;

import com.norman.labo.controller.ConsultationController;
import com.norman.labo.services.FactureService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class JasperController {
    private final ResourceLoader resourceLoader;
    private final DataSource dataSource;
    private final FactureService factureService;

    @GetMapping("/consultation/print-resultat/{idConsultation}")
    public ResponseEntity<InputStreamResource> imprimerResultat(@PathVariable Long idConsultation) throws JRException, IOException, SQLException {

        JasperDesign jasperDesign = JRXmlLoader.load(resourceLoader.getResource("classpath:static/resultat.jrxml").getInputStream());

        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("idCons", idConsultation);
        BufferedImage image = ImageIO.read(resourceLoader.getResource("classpath:static/logo1.png").getInputStream());
        parameters.put("logo", image);
        Connection connection = dataSource.getConnection();
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
        InputStream inputStream = new ByteArrayInputStream(JasperExportManager.exportReportToPdf(jasperPrint));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(new InputStreamResource(inputStream));

    }

    @GetMapping("/facture/print-facture/{idFacture}")
    public ResponseEntity<InputStreamResource> creerPdfFacture(@PathVariable Long idFacture) throws IOException, SQLException, JRException {

        //
        factureService.updateFacture(idFacture);
        JasperDesign jasperDesign = JRXmlLoader.load(resourceLoader.getResource("classpath:static/etat.jrxml").getInputStream());

        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("idFacture", idFacture);
        BufferedImage image = ImageIO.read(resourceLoader.getResource("classpath:static/logo1.png").getInputStream());
        parameters.put("logo", image);
        Connection connection = dataSource.getConnection();
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
        InputStream inputStream = new ByteArrayInputStream(JasperExportManager.exportReportToPdf(jasperPrint));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(new InputStreamResource(inputStream));
    }
}
