package com.norman.labo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UtilisateurDetailService utilisateurDetailService;

    public SecurityConfiguration(UtilisateurDetailService utilisateurDetailService) {
        this.utilisateurDetailService = utilisateurDetailService;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http
               .authorizeRequests()
               .antMatchers("/").authenticated()
               .antMatchers("/examenSouscrit/**").authenticated()
               .antMatchers("/patient/**").hasAnyRole("ADMIN","UTILISATEUR")
               .antMatchers("/facture/**").hasAnyRole("ADMIN","UTILISATEUR")
               .antMatchers("/user/**").hasRole("ADMIN")
               .antMatchers("/examen/listExamen/*").authenticated()
               .antMatchers("/laboratoire/**").authenticated()
               .antMatchers("/consultation/**").authenticated()
               .antMatchers("/examen/ajout-examen/**").hasRole("ADMIN")
               .antMatchers("/examen/enregistrer/**").hasRole("ADMIN")
               .antMatchers("/examen/editer/**").hasRole("ADMIN")
               .antMatchers("/examen/modifier/**").hasRole("ADMIN")
               .antMatchers("/examen/delete/**").hasRole("ADMIN")
               .and()
               .formLogin()
               .usernameParameter("email")
               .loginPage("/login").permitAll()
               .and()
               .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login");
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(utilisateurDetailService);
        return daoAuthenticationProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();
    }
}
