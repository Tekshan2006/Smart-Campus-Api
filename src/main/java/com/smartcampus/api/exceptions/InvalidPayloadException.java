/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.api.exceptions;

/**
 *
 * @author G.M.K.T.Thaksara
 */

// This exception is thrown when the request body contains invalid or missing data
public class InvalidPayloadException extends RuntimeException {
    public InvalidPayloadException(String message) 
    { 
        super(message); 
    }
}
