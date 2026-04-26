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

// Maps InvalidPayloadException to HTTP 400 Bad Request with JSON error message
@Provider
public class InvalidPayloadExceptionMapper implements ExceptionMapper<InvalidPayloadException> {
    @Override
    public Response toResponse(InvalidPayloadException ex) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorMessage(ex.getMessage(), 400))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
