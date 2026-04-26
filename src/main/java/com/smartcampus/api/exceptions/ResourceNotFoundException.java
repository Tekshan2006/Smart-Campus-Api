/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.api.exceptions;

/**
 *
 * @author G.M.K.T.Thaksara
 */

// This exception is used when a requested resource is not found
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) 
    { 
        super(message); 
    }
}
