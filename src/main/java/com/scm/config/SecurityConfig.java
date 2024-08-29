package com.scm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
// import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
// import org.springframework.security.core.userdetails.User;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.scm.service.serviceImpl.SecurityCustomUserDetailService;

@Configuration
public class SecurityConfig {

    @Autowired
    private SecurityCustomUserDetailService securityCustomUserDetailService;

    // @Bean
    // public UserDetailsService userDetailsService(){
        // UserDetails user = User
        // .withDefaultPasswordEncoder()
        // .username("admin123")
        // .password("password")
        // .roles("ADMIN", "USER")
        // .build();

        // UserDetails user1 = User
        // .withUsername("gurleen")
        // .password("password")
        // .roles("ADMIN", "USER")
        // .build();

        // var inMemoryUserDetailsmanager = new InMemoryUserDetailsManager(user, user1);
        // return inMemoryUserDetailsmanager;
        @Autowired
        private AuthorizationSuccessHandler handler;

        @Bean
        public DaoAuthenticationProvider authenticationProvider(){
            DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
            daoAuthenticationProvider.setUserDetailsService(securityCustomUserDetailService);
            daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
            return daoAuthenticationProvider;
        }

        @Bean
        public PasswordEncoder passwordEncoder(){
            return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{

            httpSecurity.authorizeHttpRequests(authorize -> {
                authorize.requestMatchers("/user/**").authenticated();
                authorize.anyRequest().permitAll();
            });

            httpSecurity.formLogin(formLogin -> {
                formLogin.loginPage("/login");
                formLogin.loginProcessingUrl("/authenticate");
                formLogin.successForwardUrl("/user/profile");
                formLogin.failureForwardUrl("/login?error=true");
                formLogin.usernameParameter("email");
                formLogin.passwordParameter("password");
            });

            httpSecurity.csrf(AbstractHttpConfigurer::disable);
            httpSecurity.logout(logout -> {
                logout.logoutUrl("/logout");
                logout.logoutSuccessUrl("/login?logout=true");
                logout.invalidateHttpSession(true);
                logout.clearAuthentication(true);
            });

            httpSecurity.oauth2Login(oauth -> {
                oauth.loginPage("/login");
                oauth.successHandler(handler);
            }); 

            return httpSecurity.build();
        }
    
}
