package com.scm.helpers;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

public class Helper {

    public static String getEmailOfLoggedInUser(Authentication authentication){
        String username = "";
        if(authentication instanceof OAuth2AuthenticationToken){
            var aOauth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;
            var clientId = aOauth2AuthenticationToken.getAuthorizedClientRegistrationId();
            DefaultOAuth2User user = (DefaultOAuth2User)authentication.getPrincipal(); 
            
            if(clientId.equalsIgnoreCase("google")){
                username = user.getAttribute("email").toString();
                System.out.println("email google "  + username );
            }
            else if(clientId.equalsIgnoreCase("github")){
                username = user.getAttribute("email") != null ? user.getAttribute("email").toString() : user.getAttribute("login").toString() + "@gmail.com";
                System.out.println("email github" + username);
            }
        }

        else{
            System.out.println("getting from local db");
            return authentication.getName();
        }

        return username;


    }
    
}
