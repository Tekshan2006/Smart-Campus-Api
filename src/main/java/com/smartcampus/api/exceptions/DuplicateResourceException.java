/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.api.exceptions;

/**
 *
 * @author G.M.K.T.Thaksara
 */

// Maps DuplicateResourceException to HTTP 409 Conflict response in JSON format
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) 
    { 
        super(message); 
    }
}
