package com.scm.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizationSuccessHandler;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.scm.entities.Provider;
import com.scm.entities.User;
import com.scm.helpers.AppConstant;
import com.scm.repositories.UserRepo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthorizationSuccessHandler implements AuthenticationSuccessHandler{

    Logger logger = LoggerFactory.getLogger(OAuth2AuthorizationSuccessHandler.class);
    
    @Autowired
    private UserRepo userRepo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
                logger.info("OauthenticationSuccessHandler");

                var oauth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;
                String authurizedClientRegistrationId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();
                logger.info(authurizedClientRegistrationId);
                DefaultOAuth2User user = (DefaultOAuth2User)authentication.getPrincipal(); 
                User user1 = new User();
                user1.setId(UUID.randomUUID().toString());
                user1.setRoleList(List.of(AppConstant.ROLE_USER));
                user1.setEnabled(true);
                user1.setEmailVerified(true);
                String email = "";
                if(authurizedClientRegistrationId.equalsIgnoreCase("google")){
                    email = user.getAttribute("email");
                    user1.setEmail(user.getAttribute("email"));
                    user1.setName(user.getAttribute("name"));
                    user1.setProfilePic(user.getAttribute("picture"));
                    user1.setPassword("password");
                    user1.setProvider(Provider.GOOGLE);
                    user1.setAbout("This account is created using Google");
                    user1.setProviderUserId(user.getName());

                }
                else if(authurizedClientRegistrationId.equalsIgnoreCase("github")){
                    email = user.getAttribute("email") != null ? user.getAttribute("email").toString() : user.getAttribute("login").toString() + "@gmail.com";
                    user1.setEmail(email);
                    user1.setProfilePic(user.getAttribute("avatar_url").toString());
                    user1.setName(user.getAttribute("login"));
                    user1.setProviderUserId(user.getName());
                    user1.setProvider(Provider.GITHUB);
                    user1.setAbout("This account is created using Github");




                }
                else{
                    logger.info("Unknown Provider");
                }

            
                // logger.info(user.getName());
                // user.getAttributes().forEach((key,value) -> {
                //     logger.info("{} => {}", key, value);
                // });
                // logger.info(user.getAuthorities().toString());

                User user2 = userRepo.findByEmail(email).orElse(null);
                if(user2 == null){
                    userRepo.save(user1);
                    logger.info("User saved " + email);
                }

                new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");
    }
    
    
}
