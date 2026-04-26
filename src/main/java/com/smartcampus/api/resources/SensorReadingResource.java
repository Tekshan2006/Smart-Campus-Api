/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.api.resources;


import com.smartcampus.api.exceptions.SensorUnavailableException;
import com.smartcampus.api.models.Sensor;
import com.smartcampus.api.models.SensorReading;
import com.smartcampus.api.utils.ValidationUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author G.M.K.T.Thaksara
 */

// This sub-resource manages sensor readings for a specific sensor
public class SensorReadingResource {
    private String sensorId;
    
    // In-memory storage for sensor readings
    private static Map<String, List<SensorReading>> readingDatabase = new ConcurrentHashMap<>();

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
        
    }

    // Returns all readings for a given sensor
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReadings() {
        ValidationUtils.requireFound(SensorResource.getSensorDatabase().containsKey(sensorId), "Cannot fetch readings: Sensor '" + sensorId + "' does not exist.");
        List<SensorReading> readings = readingDatabase.getOrDefault(sensorId, new CopyOnWriteArrayList<>());
        
        return Response.ok(readings).build();
    }

    // Adds a new reading and updates the sensor's current value
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addReading(SensorReading newReading) {
        ValidationUtils.requireFound(newReading != null, "Request body cannot be empty.");
        ValidationUtils.requireNonEmpty(newReading.getId(), "Reading ID is required.");
        ValidationUtils.requirePositive(newReading.getTimestamp(), "Reading timestamp must be a valid positive epoch time.");
        ValidationUtils.requireFound(SensorResource.getSensorDatabase().containsKey(sensorId), "Cannot add reading: Parent sensor '" + sensorId + "' not found.");

        Sensor parentSensor = SensorResource.getSensorDatabase().get(sensorId);

        if ("MAINTENANCE".equalsIgnoreCase(parentSensor.getStatus()) || "OFFLINE".equalsIgnoreCase(parentSensor.getStatus())) {
            throw new SensorUnavailableException("Sensor is currently offline or in maintenance.");
            
        }

        List<SensorReading> existingReadings = readingDatabase.getOrDefault(sensorId, new CopyOnWriteArrayList<>());
        boolean isDuplicate = existingReadings.stream().anyMatch(r -> r.getId().equals(newReading.getId()));
        ValidationUtils.requireUnique(isDuplicate, "Reading with ID '" + newReading.getId() + "' already exists for this sensor.");

        // Create a new thread-safe list if one doesn't exist
        readingDatabase.putIfAbsent(sensorId, new CopyOnWriteArrayList<>());
        readingDatabase.get(sensorId).add(newReading);
        
        parentSensor.setCurrentValue(newReading.getValue());

        return Response.status(Response.Status.CREATED).entity(newReading).build();
    }
}