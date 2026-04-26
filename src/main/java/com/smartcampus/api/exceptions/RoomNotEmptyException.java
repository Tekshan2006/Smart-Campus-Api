/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.api.exceptions;

/**
 *
 * @author G.M.K.T.Thaksara
 */

// Thrown when trying to delete a room that still contains sensors
public class RoomNotEmptyException extends RuntimeException {
    public RoomNotEmptyException(String message) 
    { 
        super(message); 
    }
}
