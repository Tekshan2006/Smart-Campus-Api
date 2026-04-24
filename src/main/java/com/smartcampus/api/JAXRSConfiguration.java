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
@ApplicationPath("/api/v1")
public class JAXRSConfiguration extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();

        // Resources
        resources.add(DiscoveryResource.class);
        resources.add(RoomResource.class);
        resources.add(SensorResource.class);

        // All Exception Mappers
        resources.add(GlobalExceptionMapper.class);
        resources.add(RoomNotEmptyExceptionMapper.class);
        resources.add(LinkedResourceNotFoundExceptionMapper.class);
        resources.add(SensorUnavailableExceptionMapper.class);
        resources.add(ResourceNotFoundExceptionMapper.class);
        resources.add(InvalidPayloadExceptionMapper.class);
        resources.add(DuplicateResourceExceptionMapper.class);

        // Logging Filter
        resources.add(ApiLoggingFilter.class);

        return resources;
    }
}