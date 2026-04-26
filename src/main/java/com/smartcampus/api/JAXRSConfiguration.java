package com.smartcampus.api;

import com.smartcampus.api.exceptions.*;
import com.smartcampus.api.filters.ApiLoggingFilter;
import com.smartcampus.api.resources.DiscoveryResource;
import com.smartcampus.api.resources.RoomResource;
import com.smartcampus.api.resources.SensorResource;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Configures JAX-RS for the application.
 * @author Juneau
 */

// This class configures JAX-RS and registers all resources, exception mappers, and filters
@ApplicationPath("/api/v1")
public class JAXRSConfiguration extends Application {
    
    // Registers all API endpoints, exception handlers, and filters for the application
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();

        // Register API resource classes
        resources.add(DiscoveryResource.class);
        resources.add(RoomResource.class);
        resources.add(SensorResource.class);

        // Register custom exception mappers for error handling
        resources.add(GlobalExceptionMapper.class);
        resources.add(RoomNotEmptyExceptionMapper.class);
        resources.add(LinkedResourceNotFoundExceptionMapper.class);
        resources.add(SensorUnavailableExceptionMapper.class);
        resources.add(ResourceNotFoundExceptionMapper.class);
        resources.add(InvalidPayloadExceptionMapper.class);
        resources.add(DuplicateResourceExceptionMapper.class);

        // Register logging filter for request and response tracking
        resources.add(ApiLoggingFilter.class);

        return resources;
    }
}