/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.api.filters;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.logging.Logger;
/**
 *
 * @author G.M.K.T.Thaksara
 */

// This filter logs all incoming requests and outgoing responses for debugging and monitoring
@Provider
public class ApiLoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger logger = Logger.getLogger(ApiLoggingFilter.class.getName());
    
    // Logs request details such as method and URI
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        logger.info(">>> Incoming Request");
        logger.info(">>> Method: " + requestContext.getMethod());
        logger.info(">>> URI: " + requestContext.getUriInfo().getAbsolutePath());
    }
    
    // Logs response status after request is processed
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        logger.info("<<< Outgoing Response");
        logger.info("<<< Status: " + responseContext.getStatus());
    }
}
