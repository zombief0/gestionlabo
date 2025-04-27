package com.norman.labo.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final UtilisateurDetailService utilisateurDetailService;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authenticationProvider())
                .build();
    }


    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(antMatcher("/")).authenticated()
                            .requestMatchers(antMatcher("/examenSouscrit/**")).authenticated()
                            .requestMatchers(antMatcher("/patient/**")).hasAnyRole("ADMIN", "UTILISATEUR")
                            .requestMatchers(antMatcher("/facture/**")).hasAnyRole("ADMIN", "UTILISATEUR")
                            .requestMatchers(antMatcher("/user/**")).hasRole("ADMIN")
                            .requestMatchers(antMatcher("/examen/listExamen/*")).authenticated()
                            .requestMatchers(antMatcher("/laboratoire/**")).authenticated()
                            .requestMatchers(antMatcher("/consultation/**")).authenticated()
                            .requestMatchers(antMatcher("/examen/ajout-examen/**")).hasRole("ADMIN")
                            .requestMatchers(antMatcher("/examen/enregistrer/**")).hasRole("ADMIN")
                            .requestMatchers(antMatcher("/examen/editer/**")).hasRole("ADMIN")
                            .requestMatchers(antMatcher("/examen/modifier/**")).hasRole("ADMIN")
                            .requestMatchers(antMatcher("/examen/delete/**")).hasRole("ADMIN")
                            .requestMatchers(antMatcher("/examen/delete/**")).hasRole("ADMIN")
                            .requestMatchers(antMatcher("/webjars/**")).permitAll()
                            .requestMatchers(antMatcher("/css/**")).permitAll()
                            .requestMatchers(antMatcher("/js/**")).permitAll()
                            .requestMatchers(antMatcher("/pug/**")).permitAll()
                            .requestMatchers(antMatcher("/scss/**")).permitAll()
                            .requestMatchers(antMatcher("/vendor/**")).permitAll()
                            .requestMatchers(antMatcher("/style.css")).permitAll();

                }).formLogin()
                .usernameParameter("email")
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/")
                .and()
                .logout( httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer.logoutSuccessUrl("/login"));
        return http.build();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(utilisateurDetailService);
        return daoAuthenticationProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
