/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.api.exceptions;

import com.smartcampus.api.models.ErrorMessage;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author G.M.K.T.Thaksara
 */

// Maps DuplicateResourceException to HTTP 409 Conflict response in JSON format
@Provider
public class DuplicateResourceExceptionMapper implements ExceptionMapper<DuplicateResourceException> {
    @Override
    public Response toResponse(DuplicateResourceException ex) {
        return Response.status(Response.Status.CONFLICT)
                
                .entity(new ErrorMessage(ex.getMessage(), 409))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
