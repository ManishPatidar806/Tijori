# Tijori Admin Server

A dedicated Spring Boot Admin Server for monitoring and managing the Tijori expense tracking application.

## Overview

This is a standalone Spring Boot application that provides a web-based administrative interface for monitoring the Tijori application using Spring Boot Admin by Codecentric.

## Features

- 🔍 **Real-time Monitoring** - Live application status and metrics
- 📊 **Metrics Dashboard** - JVM, HTTP, custom business metrics
- 🏥 **Health Checks** - Detailed health information for all components
- 📝 **Log Management** - View and change log levels at runtime
- 🔧 **Environment Inspector** - View configuration and environment variables
- 🧵 **Thread Analysis** - Thread dumps and heap dumps
- 🔐 **Secure** - Protected with Spring Security
- 📧 **Notifications** - Configurable alerts for status changes

## Quick Start

### Using Docker (Recommended)

```bash
# From the Backend directory
docker-compose up -d admin-server

# View logs
docker-compose logs -f admin-server
```

### Standalone

```bash
cd admin-server
mvn clean install
mvn spring-boot:run
```

## Access

- **URL**: http://localhost:8081
- **Username**: `admin`
- **Password**: `admin`

## Configuration

### Security

Default credentials are configured in `SecurityConfig.java`:
- Admin: admin/admin
- User: user/user123

**⚠️ Important**: Change these credentials for production!

### Monitoring

The admin server checks registered applications every 10 seconds (configurable in `application.properties`):

```properties
spring.boot.admin.monitor.period=10000
```

### Notifications

By default, notifications are logged to the console. To enable email notifications:

1. Uncomment the mail configuration in `application.properties`
2. Uncomment the `MailNotifier` bean in `NotifierConfig.java`
3. Configure your SMTP settings
4. Rebuild and restart

## Architecture

```
admin-server/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/financialapplication/tijori/admin/
│       │       ├── AdminServerApplication.java
│       │       └── config/
│       │           ├── SecurityConfig.java
│       │           ├── NotifierConfig.java
│       │           ├── GlobalExceptionHandler.java
│       │           └── CustomErrorController.java
│       └── resources/
│           ├── application.properties
│           └── logback-spring.xml
├── Dockerfile
└── pom.xml
```

## Dependencies

- Spring Boot 3.2.3
- Spring Boot Admin 3.2.3
- Spring Security
- Spring Actuator
- Spring Mail (optional)

## Production Deployment
## Monitoring Clients

To register an application with this admin server, the client application needs:

1. Add dependency:
```xml
<dependency>
    <groupId>de.codecentric</groupId>
    <artifactId>spring-boot-admin-starter-client</artifactId>
</dependency>
```

2. Configure in `application.properties`:
```properties
spring.boot.admin.client.url=http://localhost:8081
spring.boot.admin.client.username=admin
spring.boot.admin.client.password=admin
management.endpoints.web.exposure.include=*
```

## Troubleshooting

### Broken Pipe Errors

If you see "Broken pipe" `IOException` errors in the logs, don't worry - these are **harmless** and expected. They occur when:

- Clients disconnect before the server finishes sending Server-Sent Events (SSE) data
- Browser tabs are closed while streaming metrics
- Network connections are interrupted

The application now includes:
- **GlobalExceptionHandler**: Catches and suppresses broken pipe errors at application level
- **CustomErrorController**: Prevents error page rendering for broken pipe exceptions
- **Logback configuration**: Filters out broken pipe errors from Tomcat logs
- **Application properties**: Configured to suppress these errors at the servlet container level

These errors do not affect the functionality of the admin server.

### Application Not Showing in Dashboard

1. Check admin server is running: `curl http://localhost:8081/actuator/health`
2. Verify client configuration
3. Check network connectivity
4. Review logs: `docker-compose logs admin-server`

### Authentication Issues

1. Verify credentials
2. Clear browser cache
3. Check CSRF token in browser console
4. Review security configuration

## License

This project is proprietary.

## Author

Manish Patidar

