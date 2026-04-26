/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.api.resources;

import com.smartcampus.api.models.Sensor;
import com.smartcampus.api.utils.ValidationUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author G.M.K.T.Thaksara
 */

// This resource handles operations related to sensors
@Path("/sensors")
public class SensorResource {
    
    // In-memory storage for sensors
    private static Map<String, Sensor> sensorDatabase = new ConcurrentHashMap<>();
    
    public static Map<String, Sensor> getSensorDatabase() { 
        return sensorDatabase; 
    
    }

    // Returns all sensors or filters by type if provided
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSensors(@QueryParam("type") String type) {
        if (type == null) {
            
            return Response.ok(sensorDatabase.values()).build();
        }
        
        ValidationUtils.requireNonEmpty(type, "Type query parameter cannot be blank if provided.");

        List<Sensor> filteredList = new ArrayList<>();
        for (Sensor sensor : sensorDatabase.values()) 
        {
            if (sensor.getType().equalsIgnoreCase(type.trim())) {
                
                filteredList.add(sensor);
            }
        }
        
        return Response.ok(filteredList).build();
    }

    // Creates a new sensor and links it to a room
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSensor(Sensor newSensor, @Context UriInfo uriInfo) {
        ValidationUtils.requireFound(newSensor != null, "Request body cannot be empty.");
        ValidationUtils.requireNonEmpty(newSensor.getId(), "Sensor ID is required.");
        ValidationUtils.requireNonEmpty(newSensor.getType(), "Sensor Type is required.");
        ValidationUtils.requireNonEmpty(newSensor.getStatus(), "Sensor Status is required.");
        ValidationUtils.requireUnique(sensorDatabase.containsKey(newSensor.getId()), "A sensor with ID '" + newSensor.getId() + "' already exists.");
        
        ValidationUtils.requireLinkedFound(
                
            newSensor.getRoomId() != null && RoomResource.getRoomDatabase().containsKey(newSensor.getRoomId()), 
            "Linked Room not found. Cannot create sensor."
        );

        sensorDatabase.put(newSensor.getId(), newSensor);
        
        RoomResource.getRoomDatabase().get(newSensor.getRoomId()).getSensorIds().add(newSensor.getId());

        URI location = uriInfo.getAbsolutePathBuilder().path(newSensor.getId()).build();
        return Response.created(location).entity(newSensor).build();
        
    }
    
    // Returns a specific sensor by ID
    @GET
    @Path("/{sensorId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSensor(@PathParam("sensorId") String sensorId) {
        
        ValidationUtils.requireFound(
            sensorDatabase.containsKey(sensorId), 
            "Sensor with ID '" + sensorId + "' was not found."
                
        );
        return Response.ok(sensorDatabase.get(sensorId)).build();
        
    }
    
    // Provides access to sensor readings as a sub-resource
    @Path("/{sensorId}/readings")
    public SensorReadingResource getSensorReadingResource(@PathParam("sensorId") String sensorId) {
        
        return new SensorReadingResource(sensorId);
    }
}