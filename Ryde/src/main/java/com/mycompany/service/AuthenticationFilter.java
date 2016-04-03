/*
 * Created by Shawn Amjad on 2016.04.02  * 
 * Copyright © 2016 Shawn Amjad. All rights reserved. * 
 */
package com.mycompany.service;

import java.io.IOException;
import javax.annotation.Priority;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Shawn_000
 */
@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
    
    private UserTableFacadeREST userTableFacadeREST;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        // Get the HTTP Authorization header from the request
        String authorizationHeader = 
            requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // Check if the HTTP Authorization header is present and formatted correctly 
        if (authorizationHeader == null) {
            System.out.println("exception thrown in filter");
            throw new NotAuthorizedException("Authorization header must be provided");
        }

        // Extract the token from the HTTP Authorization header
        String token = authorizationHeader;
           
        try {

            // Validate the token
            validateToken(token);

        } catch (Exception e) {
            requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

    private void validateToken(String token) throws Exception{
        // Check if it was issued by the server and if it's not expired
        // Throw an Exception if the token is invalid
        userTableFacadeREST = new UserTableFacadeREST();
        System.out.println(token);
        String tok = userTableFacadeREST.findByToken(token).getFbTok();
        //String tok = user.getFbTok();
        System.out.println(tok);

        if (!token.equals(tok)) {
            throw new Exception();
        }
    }
}