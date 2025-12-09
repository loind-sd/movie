# common (shared-kernel) module

This `common` module contains shared DTOs, enums, events (schemas), base classes and exceptions
that are safe to share among microservices **without** sharing persistence entities or repositories.

Use this artifact as a dependency in each microservice:
```xml
<dependency>
  <groupId>com.cinema</groupId>
  <artifactId>common</artifactId>
  <version>1.0.0</version>
</dependency>
```

Contents:
- base: BaseRequest, BaseResponse, AuditInfo
- dto: example DTOs (UserDto, MovieDto)
- event: canonical event classes for Kafka messages + JSON schema files under resources/kafka-schema
- enums: BookingStatus, MovieStatus
- exception: BusinessException, ErrorCode
- mapper: MapStruct interfaces examples

Design decision: **DO NOT include JPA Entities or Repositories**. Each service keeps its own data model and mapping to DTOs/events.
