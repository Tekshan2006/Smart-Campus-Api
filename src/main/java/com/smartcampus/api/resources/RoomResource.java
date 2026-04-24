/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.api.resources;

import com.smartcampus.api.exceptions.RoomNotEmptyException;
import com.smartcampus.api.models.Room;
import com.smartcampus.api.utils.ValidationUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author G.M.K.T.Thaksara
 */

@Path("/rooms")
public class RoomResource {
    
    // Upgraded to ConcurrentHashMap for Thread-Safety
    private static Map<String, Room> roomDatabase = new ConcurrentHashMap<>();

    // Static block runs ONLY ONCE when the server starts, solving the Request-Scope issue
    static {
        roomDatabase.put("LIB-301", new Room("LIB-301", "Library Quiet Study", 50));
        roomDatabase.put("LAB-101", new Room("LAB-101", "Computer Lab", 30));
    }

    public RoomResource() {
        // Constructor is now safely empty
    }

    public static Map<String, Room> getRoomDatabase() { return roomDatabase; }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRooms() {
        return Response.ok(roomDatabase.values()).build();
    }

    @GET
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoom(@PathParam("roomId") String roomId) {
        ValidationUtils.requireFound(roomDatabase.containsKey(roomId), "Room with ID '" + roomId + "' was not found.");
        return Response.ok(roomDatabase.get(roomId)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addRoom(Room newRoom, @Context UriInfo uriInfo) {
        ValidationUtils.requireFound(newRoom != null, "Request body cannot be empty.");
        ValidationUtils.requireNonEmpty(newRoom.getId(), "Room ID cannot be empty.");
        ValidationUtils.requireNonEmpty(newRoom.getName(), "Room Name cannot be empty.");
        ValidationUtils.requirePositive(newRoom.getCapacity(), "Room capacity must be greater than zero.");
        ValidationUtils.requireUnique(roomDatabase.containsKey(newRoom.getId()), "A room with ID '" + newRoom.getId() + "' already exists.");

        roomDatabase.put(newRoom.getId(), newRoom);
        URI location = uriInfo.getAbsolutePathBuilder().path(newRoom.getId()).build();
        return Response.created(location).entity(newRoom).build();
    }

    @DELETE
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        ValidationUtils.requireFound(roomDatabase.containsKey(roomId), "Cannot delete: Room with ID '" + roomId + "' does not exist.");
        Room room = roomDatabase.get(roomId);
        
        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException("Cannot delete room with active sensors.");
        }
        
        roomDatabase.remove(roomId);
        return Response.noContent().build();
    }
}