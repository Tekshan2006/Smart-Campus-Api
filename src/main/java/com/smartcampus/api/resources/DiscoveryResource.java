/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.api.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author G.M.K.T.Thaksara
 */

// This resource provides API discovery information and available endpoints
@Path("/")
public class DiscoveryResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDiscoveryInfo(@Context UriInfo uriInfo) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("version", "1.2 Updated at 2026/04/22");
        metadata.put("Name", "G.M.K.T.Thaksara");
        metadata.put("Student ID", "W2120715");
        metadata.put("contact", "w2120715@westminster.ac.uk");
        
        // Dynamically build absolute URLs based on the server environment
        String baseUri = uriInfo.getBaseUri().toString();
        
        // Returns API metadata and links for navigation
        Map<String, String> links = new HashMap<>();
        links.put("rooms", baseUri + "rooms");
        links.put("sensors", baseUri + "sensors");
        
        metadata.put("_links", links);

        return Response.ok(metadata).build();
    }
}