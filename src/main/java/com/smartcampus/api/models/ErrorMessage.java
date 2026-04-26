/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.api.models;

/**
 *
 * @author G.M.K.T.Thaksara
 */

// This class represents a standard error response returned by the API
public class ErrorMessage {
    private String error;
    private int status;

    public ErrorMessage() {}

    public ErrorMessage(String error, int status) {
        this.error = error;
        this.status = status;
    }

    public String getError() { 
        return error; 
    }
    
    public void setError(String error) { 
        this.error = error; 
    }

    public int getStatus() {
        return status; 
    }
    
    public void setStatus(int status) { 
        this.status = status; 
    }
}
