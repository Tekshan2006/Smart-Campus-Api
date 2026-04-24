/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.api.utils;

import com.smartcampus.api.exceptions.DuplicateResourceException;
import com.smartcampus.api.exceptions.InvalidPayloadException;
import com.smartcampus.api.exceptions.LinkedResourceNotFoundException;
import com.smartcampus.api.exceptions.ResourceNotFoundException;

/**
 *
 * @author G.M.K.T.Thaksara
 */
public class ValidationUtils {

    public static void requireNonEmpty(String value, String errorMessage) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidPayloadException(errorMessage);
        }
    }

    public static void requirePositive(double value, String errorMessage) {
        if (value <= 0) {
            throw new InvalidPayloadException(errorMessage);
        }
    }

    public static void requireUnique(boolean alreadyExists, String errorMessage) {
        if (alreadyExists) {
            throw new DuplicateResourceException(errorMessage);
        }
    }

    public static void requireFound(boolean exists, String errorMessage) {
        if (!exists) {
            throw new ResourceNotFoundException(errorMessage);
        }
    }

    public static void requireLinkedFound(boolean exists, String errorMessage) {
        if (!exists) {
            throw new LinkedResourceNotFoundException(errorMessage);
        }
    }
    
    public static void requireNotNull(Object obj, String errorMessage) {
        if (obj == null) {
            throw new InvalidPayloadException(errorMessage);
        }
    }
}
