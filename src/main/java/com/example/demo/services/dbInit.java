package com.example.demo.services;

import com.example.demo.entities.Personne;
import com.example.demo.entities.Utilisateur;
import com.example.demo.repositories.UtilisateurRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Date;

@Service
public class dbInit implements CommandLineRunner {
    private UtilisateurRepository utilisateurRepository;
    private DataSource dataSource;
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public dbInit(UtilisateurRepository utilisateurRepository, DataSource dataSource) {
        this.utilisateurRepository = utilisateurRepository;
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) throws Exception {

        /*if(utilisateurRepository.findAll().size() == 0) {
            Utilisateur admin = new Utilisateur("", "", "root@mail.com", new Date(), 664123587, Personne.Sexe.MASCULIN, bCryptPasswordEncoder.encode("1234"), "ADMIN");

            utilisateurRepository.save(admin);
        }*/
       /* JasperDesign jasperDesign = JRXmlLoader.load(ResourceUtils.getFile("classpath:etat.jrxml"));

        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("idFacture", 7L );

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters, dataSource.getConnection());
        String myDocumentDirectory = System.getProperty("user.home") + "//Documents//test.pdf";
        JasperExportManager.exportReportToPdfFile(jasperPrint,myDocumentDirectory);*/
    }
}
