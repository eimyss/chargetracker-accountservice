package de.eimantas.eimantasbackend.service;


import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class SecurityService {


    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    public boolean isAllowedToReadAcc(Principal authentication, long accID) {

        if (authentication == null)
            throw new SecurityException("Authentication is null");


        logger.debug("checking access for account from user: " + authentication.getName());

        // for now is okay
        return true;

    }

    public long getOrCreateUserFromPrincipal(KeycloakAuthenticationToken authentication) {
        return 0L;

    }

    public long getUserIdFromPrincipal(KeycloakAuthenticationToken user) {
        return 0L;
    }
}
