package com.norman.labo.security;

import com.norman.labo.repositories.UtilisateurRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

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
//        System.out.println(bCryptPasswordEncoder.encode("1234"));
        /*if(utilisateurRepository.findAll().size() == 0) {
            Utilisateur admin = utilisateurRepository.findByIdPersonne(1L);
            admin.setMdp(bCryptPasswordEncoder.encode("myTimeIndeed$985"));
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
