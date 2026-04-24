# Smart Campus Sensor & Room Management API

**Module:** 5COSC022W - Client-Server Architectures  
**Student Name:** G.M.K.T. Thaksara  
**Student ID:** W2120715  

A complete RESTful API built with **JAX-RS (Jersey)** for managing university rooms, sensors, and sensor readings. All data is stored in-memory using thread-safe collections (`ConcurrentHashMap` + `CopyOnWriteArrayList`).

**Base URL:** `http://localhost:8080/smart-campus-api/api/v1`

## API Design Overview
- Versioned under `/api/v1`
- Follows REST principles and HATEOAS
- Uses sub-resource locator pattern for nested readings
- Full custom exception handling with clean JSON error responses
- Request/response logging via JAX-RS filters
- No external database – pure in-memory storage as per specification

### Core Endpoints
| Method | Endpoint                                | Description |
|--------|-----------------------------------------|-----------|
| GET    | `/`                                     | Discovery (HATEOAS metadata) |
| GET    | `/rooms`                                | List all rooms |
| POST   | `/rooms`                                | Create room (201 + Location) |
| GET    | `/rooms/{roomId}`                       | Get single room |
| DELETE | `/rooms/{roomId}`                       | Delete room (409 if sensors exist) |
| POST   | `/sensors`                              | Create sensor (validates roomId) |
| GET    | `/sensors?type=XXX`                     | List sensors with optional filter |
| GET    | `/sensors/{sensorId}/readings`          | Get reading history |
| POST   | `/sensors/{sensorId}/readings`          | Add reading (updates parent currentValue) |

All error responses return structured JSON. No raw stack traces are ever exposed.

## How to Build and Run

### Prerequisites
- JDK 8 or higher
- Maven 3.6+
- Apache Tomcat 9 (or GlassFish/Payara)

### Steps
1. **Clone the repository**
   ```bash
   git clone https://github.com/YOUR-USERNAME/smart-campus-api.git
   cd smart-campus-api
   ```

2. **Build the project**
   ```bash
   mvn clean package
   ```

3. **Deploy to Server**
   - Open NetBeans
   - Add your server in Services → Servers
   - Right-click project → Run

**Server URL:** `http://localhost:8080/smart-campus-api/api/v1`

## Full Testing Commands (cURL)

> **Note:** The `-i` flag is included to explicitly display the HTTP Status Codes (e.g., 201 Created, 404, 409) and Headers (e.g., Location) required for the demonstration.

```bash
# 1. Discovery Endpoint
curl -i -X GET http://localhost:8080/smart-campus-api/api/v1 -H "Accept: application/json"

# 2. Create Room (Proves 201 Created + Location header)
curl -i -X POST http://localhost:8080/smart-campus-api/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d '{"id":"LIB-304","name":"Final Study Room","capacity":40}'

# 3. Get All Rooms
curl -i -X GET http://localhost:8080/smart-campus-api/api/v1/rooms

# 4. Get Single Room
curl -i -X GET http://localhost:8080/smart-campus-api/api/v1/rooms/LIB-304

# 5. Create Sensor (valid roomId)
curl -i -X POST http://localhost:8080/smart-campus-api/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{"id":"TEMP-005","type":"Temperature","status":"ACTIVE","currentValue":0.0,"roomId":"LIB-304"}'

# 6. Create Sensor with invalid roomId → Proves 422 Unprocessable Entity
curl -i -X POST http://localhost:8080/smart-campus-api/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{"id":"CO2-999","type":"CO2","status":"ACTIVE","currentValue":0.0,"roomId":"NON-EXISTENT-ROOM"}'

# 7. Filter Sensors by type
curl -i -X GET "http://localhost:8080/smart-campus-api/api/v1/sensors?type=Temperature"

# 8. Get Readings (sub-resource - initially empty)
curl -i -X GET http://localhost:8080/smart-campus-api/api/v1/sensors/TEMP-005/readings

# 9. Post Reading + update parent currentValue
curl -i -X POST http://localhost:8080/smart-campus-api/api/v1/sensors/TEMP-005/readings \
  -H "Content-Type: application/json" \
  -d '{"id":"READ-004","timestamp":1745480000000,"value":25.3}'

# 10. Verify currentValue was updated on the parent sensor
curl -i -X GET http://localhost:8080/smart-campus-api/api/v1/sensors/TEMP-005

# 11. Delete Room with active sensors → Proves 409 Conflict Mapper
curl -i -X DELETE http://localhost:8080/smart-campus-api/api/v1/rooms/LIB-304

# 12. Create Maintenance Sensor
curl -i -X POST http://localhost:8080/smart-campus-api/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{"id":"MAINT-003","type":"Occupancy","status":"MAINTENANCE","currentValue":0.0,"roomId":"LAB-101"}'

# 13. Post Reading to Maintenance Sensor → Proves 403 Forbidden Mapper
curl -i -X POST http://localhost:8080/smart-campus-api/api/v1/sensors/MAINT-003/readings \
  -H "Content-Type: application/json" \
  -d '{"id":"READ-005","timestamp":1745481000000,"value":10}'

# 14. Trigger Global 500/400 (Clean JSON Error instead of server crash)
curl -i -X POST http://localhost:8080/smart-campus-api/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d 'INVALID JSON OR CORRUPTED PAYLOAD'
```

## Coursework Report

### Part 1: Service Architecture & Setup

#### 1.1 Project & Application Configuration
The default lifecycle of a JAX-RS Resource class is **request-scoped**. A new instance is created for every incoming HTTP request and destroyed after the response is sent. Because of this, I cannot store data in instance variables. All in-memory data is held in static `ConcurrentHashMap` and `CopyOnWriteArrayList` collections. Resources and exception mappers are explicitly registered in `JAXRSConfiguration.getClasses()` to ensure they are properly discovered by Jersey.

#### 1.2 The Discovery Endpoint
HATEOAS makes the API self-describing. Clients can navigate the entire API by following the links returned in responses instead of relying on static, hardcoded documentation, significantly reducing coupling between the client and server.

### Part 2: Room Management

#### 2.1 Room Resource Implementation
Returning full objects increases payload size but reduces client round-trips. Returning only IDs saves bandwidth but requires extra requests. Full objects were chosen for better usability in this context.

#### 2.2 Room Deletion & Safety Logic
The DELETE operation is idempotent. After successful deletion, the room no longer exists. Repeated calls to delete the same resource result in a 404 Not Found, but the final state of the server remains unchanged (the room is still deleted).

### Part 3: Sensor Operations & Linking

#### 3.1 Sensor Resource & Integrity
If the client sends a wrong Content-Type, JAX-RS automatically rejects the request due to the @Consumes(MediaType.APPLICATION_JSON) annotation, ensuring data integrity before processing logic begins.

#### 3.2 Filtered Retrieval & Search
@QueryParam is superior to @PathParam for filtering collections because query strings are optional, composable, and designed to modify the representation of an existing collection without fundamentally changing the underlying endpoint's identity.

### Part 4: Deep Nesting with Sub-Resources

#### 4.1 The Sub-Resource Locator Pattern
Delegating to a separate SensorReadingResource class (using an @Path annotation without an HTTP verb) keeps the code modular, preventing the parent SensorResource from becoming cluttered with deeply nested logic.

#### 4.2 Historical Data Management
A POST request to the nested readings endpoint correctly enforces the required side-effect: appending the history log while simultaneously updating the parent sensor's currentValue.

### Part 5: Advanced Error Handling, Exception Mapping & Logging

#### Dependency Validation (422)
Returning a 422 Unprocessable Entity is semantically superior to a 404 Not Found when a client submits an invalid roomId during sensor creation. A 404 implies the /sensors endpoint itself is missing, whereas a 422 correctly indicates the payload syntax is valid, but the business instructions (the foreign key) cannot be processed.

#### Global Safety Net (500)
Exposing raw Java stack traces reveals internal routing paths, framework library versions, and database schemas that attackers can exploit to craft targeted injections. The global ExceptionMapper<Throwable> safely intercepts these crashes, logging the trace internally while returning a sanitized, generic message to the client.

#### API Logging Filters
ContainerRequestFilter and ContainerResponseFilter are the correct architectural choices for logging because they apply automatically to every request/response across the entire application, eliminating boilerplate code duplication in individual resource methods.