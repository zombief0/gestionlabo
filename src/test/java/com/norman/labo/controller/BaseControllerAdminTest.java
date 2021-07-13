package com.norman.labo.controller;

import com.norman.labo.entities.Utilisateur;
import com.norman.labo.security.UtilisateurDetail;
import com.norman.labo.security.UtilisateurDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

public abstract class BaseControllerAdminTest {
    protected MockMvc mockMvc;

    @Autowired
    WebApplicationContext wac;

    @MockBean(name = "utilisateurDetailService")
    UtilisateurDetailService utilisateurDetailService;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity()).build();
    }

    @PostConstruct
    public void setUsers(){
        Utilisateur admin = new Utilisateur();
        admin.setEmail("adminTest@mail.com");
        admin.setMdp("1235");
        admin.setRole("ADMIN");
        given(utilisateurDetailService.loadUserByUsername("adminTest@mail.com"))
                .willReturn(new UtilisateurDetail(admin));
    }
}
