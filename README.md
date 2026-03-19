# Getting Started

### Meeting Room ms
Spring Boot 3.5 REST API for listing meeting rooms and managing bookings with overlap detection. Features H2 in-memory database, MapStruct DTO/Entity mapping, and unit tests (JUnit5 + Mockito).

#### OpenAPI 3.0 compliant with generated Java client library.
##### Features:

| Endpoint                 |     Method     |                Description |                   Response |
|:-------------------------|:--------------:|---------------------------:|---------------------------:|
| GET /v1/rooms            |   List rooms   |        Fixed meeting rooms |        200 [MeetingRoom[]] |
| POST /v1/bookings        | Create booking |         Overlap validation | 201 Booking / 409 Conflict |
| GET /v1/bookings         | List bookings  |  Filter: roomId? from? to? |            200 [Booking[]] |
| DELETE /v1/bookings/{id} | Delete booking |                          - |        204 / 404 Not Found |

**Error handling**: 400 Bad Request, 404 Not Found, 409 Conflict with JSON Error schema.

### Quick Start
``` bash
mvn spring-boot:run
```

H2 Console: http://localhost:8080/h2-console <br/>
Swagger UI: http://localhost:8080/swagger-ui.html

### Client Library
1. Generate (client project):

``` bash
mvn clean generate-sources package
```

2. Usage (pom.xml dependency):

```xml
<dependency> 
<groupId>com.example</groupId>
<artifactId>meetingroom-lib</artifactId>
<version>1.0.0</version>
</dependency>
```

Testing

```bash
mvn test                    # All tests
mvn test -Dtest=*Controller # Controllers only
mvn test -Dtest=*Service    # Services only
```

### Usage Examples

#### 1. List rooms
curl http://localhost:8080/v1/rooms

#### 2. Create booking
curl -X POST http://localhost:8080/v1/bookings \\
-H "Content-Type: application/json" \\
-d '{
"roomId": 1,
"from": "2026-03-06T12:00:00Z",
"to": "2026-03-06T13:00:00Z",
"title": "Sprint Planning"
}'

#### 3. List bookings (filtered)
curl "http://localhost:8080/v1/bookings?roomId=1"

#### 4. Delete booking
curl -X DELETE http://localhost:8080/v1/bookings/123
