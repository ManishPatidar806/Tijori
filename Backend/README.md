# Tijori - Personal Expense Tracker Backend

<div align="center">

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.10-brightgreen?style=for-the-badge&logo=spring-boot)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql)
![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED?style=for-the-badge&logo=docker)
![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)

*A modern, intelligent expense tracking system with SMS-based transaction analysis*

[Features](#features) • [Quick Start](#quick-start) • [API Documentation](#api-documentation) • [Configuration](#configuration) • [Deployment](#deployment)

</div>

---

## 📖 Overview

**Tijori** (meaning "vault" or "safe" in Hindi) is a comprehensive personal finance management application designed to help users track expenses, categorize transactions, and maintain budget discipline. The backend is built with Spring Boot 3.5.10 and Java 21, offering robust RESTful APIs for mobile and web clients.

### Key Highlights

- 🚀 **SMS Transaction Analysis**: Automatically parse and categorize bank SMS messages
- 📊 **Category-based Budgeting**: Track expenses across multiple categories
- 🔐 **JWT Authentication**: Secure token-based authentication system
- 📧 **Email OTP Verification**: Two-factor authentication support
- 📈 **Real-time Monitoring**: Integrated Spring Boot Admin dashboard
- 🐳 **Dockerized Deployment**: Complete containerization with Docker Compose
- ⚡ **Production-Ready**: Rate limiting, caching, health checks, and metrics
- 🎯 **RESTful APIs**: Clean, documented API endpoints with OpenAPI/Swagger

---

## 🎯 Features

### 💰 Transaction Management
- **Automated Transaction Import**: Parse and save SMS-based banking transactions
- **Category Filtering**: Filter transactions by category (Food, Transport, Shopping, etc.)
- **Transaction History**: Complete audit trail of all financial activities
- **Bulk Operations**: Efficient batch processing of multiple transactions

### 👤 User Management
- **User Registration & Login**: Mobile number-based authentication
- **Profile Management**: User profile with customizable settings
- **JWT Token Security**: Secure, stateless authentication
- **Email Verification**: OTP-based email validation

### 💳 Budget & Balance Tracking
- **Income Management**: Set monthly income and savings goals
- **Category Budgets**: Individual budget limits for each expense category
- **Balance Tracking**: Real-time account balance calculation
- **Expense Limits**: Automated alerts for budget overruns
- **Monthly Reports**: Automated expense reporting system

### 📨 Communication
- **Email Notifications**: Async email notifications for important events
- **OTP Verification**: 6-digit OTP with configurable expiration
- **Usage Alerts**: Automated spending threshold notifications
- **HTML Templates**: Beautiful, responsive email templates

### 🛡️ Security & Performance
- **Rate Limiting**: Bucket4j-based API rate limiting
- **Caching**: Caffeine cache for improved performance
- **Request Logging**: Comprehensive request/response logging
- **CORS Configuration**: Configurable cross-origin resource sharing
- **Input Validation**: Robust validation with custom constraints

### 📊 Monitoring & Observability
- **Spring Boot Admin**: Dedicated admin server for application monitoring
- **Health Checks**: Liveness and readiness probes
- **Metrics**: Micrometer-based application metrics
- **Custom Dashboards**: Real-time monitoring of business metrics
- **Log Management**: Structured logging with Logback

---

## 🏗️ Architecture

### Technology Stack

| Component | Technology              | Version |
|-----------|-------------------------|---------|
| **Language** | Java                    | 21 |
| **Framework** | Spring Boot             | 3.5.10 |
| **Database** | MySQL                   | 8.0 |
| **Authentication** | Spring Security (JWT)   | 0.11.5 |
| **API Documentation** | SpringDoc OpenAPI       | 2.8.5 |
| **Caching** | Caffeine                | Latest |
| **Rate Limiting** | Bucket4j                | 8.10.1 |
| **Monitoring** | Spring Boot Admin       | 3.5.7 |
| **Metrics** | Micrometer              | Latest |
| **Build Tool** | Maven                   | 3.x |
| **Containerization** | Docker & Docker Compose | Latest |


---

## 🚀 Quick Start

### Prerequisites

Before you begin, ensure you have the following installed:

- **Java 21** (JDK 21 LTS)
- **Maven 3.8+** (or use the included Maven wrapper)
- **MySQL 8.0+** (or use Docker)
- **Docker & Docker Compose** (recommended for quickest setup)
- **Git**

### Option 1: Docker Setup (Recommended)

The fastest way to get up and running:

```bash
# Clone the repository
git clone https://github.com/ManishPatidar806/Tijori.git
cd Tijori/Backend

# Set up environment variables (optional - defaults are provided)
cp .env.example .env
# Edit .env with your MAIL_USERNAME and MAIL_PASSWORD

# Start all services (app + database + admin server)
docker-compose up -d

# View logs
docker-compose logs -f app

# Stop all services
docker-compose down

# Stop and remove volumes (clean slate)
docker-compose down -v
```

The application will be available at:
- **Main API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Admin Dashboard**: http://localhost:8081 (admin/admin)

### Option 2: Local Development Setup

For development with hot-reload:

```bash
# Clone the repository
git clone https://github.com/ManishPatidar806/Tijori.git
cd Tijori/Backend

# Set up MySQL database
mysql -u root -p
CREATE DATABASE expenses_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'expenses_user'@'localhost' IDENTIFIED BY 'expenses_password';
GRANT ALL PRIVILEGES ON expenses_db.* TO 'expenses_user'@'localhost';
FLUSH PRIVILEGES;
exit;

# Configure environment variables
export DATABASE_URL="jdbc:mysql://localhost:3306/expenses_db?useSSL=false&serverTimezone=UTC"
export DATABASE_USERNAME="expenses_user"
export DATABASE_PASSWORD="expenses_password"
export JWT_SECRET="your-super-secret-key-that-is-at-least-64-characters-long-for-hs512"
export MAIL_USERNAME="your-email@gmail.com"
export MAIL_PASSWORD="your-app-password"
export CORS_ALLOWED_ORIGINS="http://localhost:3000,http://localhost:5173"

# Build and run the application
./mvnw clean install
./mvnw spring-boot:run

# Or run with specific profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### Option 3: Production Build

For production deployment:

```bash
# Build the JAR file
./mvnw clean package -DskipTests

# Run the JAR
java -jar target/Tijori-0.0.1-SNAPSHOT.jar

# Or with JVM optimizations
java -XX:+UseContainerSupport \
     -XX:MaxRAMPercentage=75.0 \
     -Djava.security.egd=file:/dev/./urandom \
     -jar target/Tijori-0.0.1-SNAPSHOT.jar
```

---

## ⚙️ Configuration

### Environment Variables

The application requires the following environment variables:

| Variable | Description | Default | Required |
|----------|-------------|---------|----------|
| `DATABASE_URL` | MySQL JDBC connection URL | - | ✅ |
| `DATABASE_USERNAME` | Database username | - | ✅ |
| `DATABASE_PASSWORD` | Database password | - | ✅ |
| `JWT_SECRET` | JWT signing secret (min 64 chars) | - | ✅ |
| `MAIL_USERNAME` | SMTP email username | - | ✅ |
| `MAIL_PASSWORD` | SMTP email password/app password | - | ✅ |
| `CORS_ALLOWED_ORIGINS` | Allowed CORS origins (comma-separated) | `http://localhost:3000` | ❌ |
| `SPRING_PROFILES_ACTIVE` | Active Spring profile | `dev` | ❌ |
| `SPRING_BOOT_ADMIN_URL` | Admin server URL | `http://localhost:8081` | ❌ |
| `SPRING_BOOT_ADMIN_USERNAME` | Admin server username | `admin` | ❌ |
| `SPRING_BOOT_ADMIN_PASSWORD` | Admin server password | `admin` | ❌ |

### Email Configuration (Gmail)

To enable email functionality with Gmail:

1. **Enable 2-Factor Authentication** in your Google account
2. **Generate an App Password**:
   - Go to: https://myaccount.google.com/apppasswords
   - Select "Mail" and your device
   - Copy the generated 16-character password
3. **Set environment variables**:
   ```bash
   export MAIL_USERNAME="your-email@gmail.com"
   export MAIL_PASSWORD="your-16-char-app-password"
   ```

### Application Profiles

The application supports multiple profiles:

- **`dev`**: Development mode with detailed logging
- **`prod`**: Production mode with optimized settings
- **`test`**: Testing mode with H2 in-memory database

Activate a profile:
```bash
# Via environment variable
export SPRING_PROFILES_ACTIVE=prod

# Via command line
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod

# Via application properties
spring.profiles.active=prod
```

### Database Configuration

#### MySQL Setup

```sql
-- Create database with UTF-8 support
CREATE DATABASE expenses_db 
  CHARACTER SET utf8mb4 
  COLLATE utf8mb4_unicode_ci;

-- Create user and grant privileges
CREATE USER 'expenses_user'@'%' IDENTIFIED BY 'your_secure_password';
GRANT ALL PRIVILEGES ON expenses_db.* TO 'expenses_user'@'%';
FLUSH PRIVILEGES;
```

#### Connection Pool Tuning

The application uses HikariCP with the following defaults:

```properties
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.idle-timeout=300000
spring.datasource.hikari.connection-timeout=20000
spring.datasource.hikari.max-lifetime=1200000
```

Adjust these based on your load requirements.

---

## 📡 API Documentation

### Interactive API Documentation

Once the application is running, access the interactive API documentation:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs
- **API Docs Path**: http://localhost:8080/v3/api-docs

### API Endpoints Overview

#### Authentication APIs (`/v1/api/users`)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/signup` | Register new user | ❌ |
| POST | `/signin` | Login user | ❌ |
| GET | `/profile` | Get user profile | ✅ |
| PUT | `/profile` | Update user profile | ✅ |
| DELETE | `/profile` | Delete user account | ✅ |

#### Transaction APIs (`/v1/api/transactions`)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/save` | Save SMS transactions | ✅ |
| GET | `/categorization` | Get uncategorized transactions | ✅ |
| GET | `/category?category={type}` | Get transactions by category | ✅ |
| POST | `/categorization` | Update transaction categories | ✅ |

#### Category Budget APIs (`/v1/api/category-budgets`)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/amounts` | Get budget breakdown by category | ✅ |

#### Account Balance APIs (`/v1/api/account-balance`)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/income?income={amount}&saving={amount}` | Set income & savings | ✅ |
| GET | `/` | Get current balance info | ✅ |

#### Email Verification APIs (`/v1/api/emailVerification`)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/sendOtp?email={email}` | Send OTP to email | ❌ |
| POST | `/verifyOtp?email={email}&otp={code}` | Verify email OTP | ❌ |

### Authentication

The API uses JWT Bearer token authentication. Include the token in the Authorization header:

```bash
Authorization: Bearer <your-jwt-token>
```

#### Example: Login and Access Protected Endpoint

```bash
# 1. Register/Login to get token
curl -X POST http://localhost:8080/v1/api/users/signin \
  -H "Content-Type: application/json" \
  -d '{"mobileNo": "9876543210"}'

# Response: {"status":true,"message":"Login successful","data":{"token":"eyJhbGc..."}}

# 2. Use token to access protected endpoints
curl -X GET http://localhost:8080/v1/api/transactions/categorization \
  -H "Authorization: Bearer eyJhbGc..."
```
---

## 🔧 Admin Server

The project includes a dedicated Spring Boot Admin Server for monitoring and management.

### Features

- ✅ Real-time application status monitoring
- 📊 JVM metrics and thread dumps
- 🏥 Health check dashboard
- 📝 Dynamic log level management
- 🔧 Environment and configuration inspection
- 📈 HTTP request statistics
- 🔐 Secured with Spring Security

### Access Admin Dashboard

1. **Using Docker Compose** (automatically started):
   ```bash
   docker-compose up -d
   ```
   Access: http://localhost:8081

2. **Standalone Admin Server**:
   ```bash
   cd AdminServer
   mvn spring-boot:run
   ```
   Access: http://localhost:8081

3. **Login Credentials**:
   - **Username**: `admin`
   - **Password**: `admin`

### Admin Server Configuration

Configure the admin server URL in your main application:

```properties
# Main Application (application.properties)
spring.boot.admin.client.url=http://localhost:8081
spring.boot.admin.client.username=admin
spring.boot.admin.client.password=admin
```

Change default credentials in `AdminServer/src/main/java/.../config/SecurityConfig.java`.

---

## 🐳 Docker Deployment

### Docker Compose (Full Stack)

The `docker-compose.yml` provides a complete stack with three services:

1. **app**: Main Tijori application
2. **db**: MySQL 8.0 database
3. **admin-server**: Spring Boot Admin dashboard

```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# View specific service logs
docker-compose logs -f app

# Stop all services
docker-compose down

# Rebuild and restart
docker-compose up -d --build

# Clean everything (including volumes)
docker-compose down -v
```

### Individual Docker Commands

#### Build Application Image

```bash
docker build -t tijori-backend:latest .
```

#### Run Application Container

```bash
docker run -d \
  --name tijori-app \
  -p 8080:8080 \
  -e DATABASE_URL="jdbc:mysql://host.docker.internal:3306/expenses_db" \
  -e DATABASE_USERNAME="expenses_user" \
  -e DATABASE_PASSWORD="expenses_password" \
  -e JWT_SECRET="your-secret-key" \
  -e MAIL_USERNAME="your-email@gmail.com" \
  -e MAIL_PASSWORD="your-app-password" \
  tijori-backend:latest
```

#### Build Admin Server Image

```bash
cd AdminServer
docker build -t tijori-admin-server:latest .
```

### Docker Health Checks

Both containers include health checks:

```bash
# Check container health
docker ps

# View health check logs
docker inspect --format='{{json .State.Health}}' tijori-app
```

---

## 🧪 Testing

### Manual API Testing

#### Using cURL

See [API Documentation](#api-documentation) section for detailed cURL examples.

#### Using Postman

1. Import the OpenAPI specification from http://localhost:8080/v3/api-docs
2. Create an environment with the base URL
3. Add authentication token to collection/folder

#### Using Swagger UI

Navigate to http://localhost:8080/swagger-ui.html and test directly in the browser.

---

## 📊 Monitoring & Metrics

### Actuator Endpoints

The application exposes several monitoring endpoints:

| Endpoint | Description | Authentication |
|----------|-------------|----------------|
| `/actuator/health` | Application health status | Public |
| `/actuator/info` | Application information | Public |
| `/actuator/metrics` | Application metrics | Admin only |
| `/actuator/health/liveness` | Kubernetes liveness probe | Public |
| `/actuator/health/readiness` | Kubernetes readiness probe | Public |

Access metrics:
```bash
# Health check
curl http://localhost:8080/actuator/health

# Application info
curl http://localhost:8080/actuator/info

# Available metrics
curl http://localhost:8080/actuator/metrics
```

### Custom Metrics

The application tracks custom business metrics:

- `user.registration.count` - Total user registrations
- `user.login.count` - Total login attempts
- `transaction.save.count` - Transactions saved
- `transaction.categorization.count` - Categorizations performed
- `auth.signup.time` - Registration response time
- `auth.signin.time` - Login response time

### Logging

Logs are configured using Logback with different levels per environment:

- **Development**: INFO level with detailed logging
- **Production**: ERROR/WARN level for performance

View logs:
```bash
# Docker
docker-compose logs -f app

# Local
tail -f logs/tijori-application.log
```

---

## 🔒 Security

### Security Features

1. **JWT Authentication**: Stateless token-based authentication
2. **Password Security**: BCrypt hashing (currently mobile-based auth)
3. **CORS Protection**: Configurable allowed origins
4. **Rate Limiting**: Bucket4j-based request throttling
5. **Input Validation**: Jakarta Validation constraints
6. **SQL Injection Prevention**: JPA/Hibernate parameterized queries
7. **Security Headers**: Configured via Spring Security
8. **HTTPS Ready**: SSL/TLS configuration supported

### Rate Limiting

The application implements rate limiting for critical endpoints:

- **Auth endpoints**: 5 requests per minute
- **Email OTP**: 3 requests per minute
- **General API**: 100 requests per minute

Configure rate limits in `RateLimitConfig.java`.

### Securing Production

1. **Change default credentials**:
   - Update Admin Server username/password
   - Generate strong JWT secret (min 64 characters)

2. **Enable HTTPS**:
   ```properties
   server.ssl.enabled=true
   server.ssl.key-store=classpath:keystore.p12
   server.ssl.key-store-password=your-password
   server.ssl.key-store-type=PKCS12
   ```

3. **Configure firewall rules**:
   - Only expose port 8080 (or 443 for HTTPS)
   - Restrict admin server access (port 8081)

4. **Use environment-specific configurations**:
   ```bash
   export SPRING_PROFILES_ACTIVE=prod
   ```
---

## 🛠️ Development

### Setting Up Development Environment

```bash
# Clone repository
git clone https://github.com/ManishPatidar806/Tijori.git
cd Tijori/Backend

# Install dependencies
./mvnw clean install

# Run in development mode with hot-reload
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### Code Style & Standards

The project follows standard Java conventions:

- **Naming**: CamelCase for classes, camelCase for methods/variables
- **Structure**: Layered architecture (Controller → Service → Repository)
- **Lombok**: Used for reducing boilerplate code
- **Validation**: Jakarta Validation annotations on request models

### Adding New Features

1. **Create Model/Entity**: Define in `Model/Entity/`
2. **Create Repository**: JPA repository interface
3. **Create Service**: Business logic in `Service/`
4. **Create Controller**: REST endpoints in `Controller/`
5. **Update Documentation**: Update this README and API docs

### Debugging

Enable debug logging:
```properties
# application-dev.properties
logging.level.com.financialapplication.tijori=DEBUG
spring.jpa.show-sql=true
```

Remote debugging:
```bash
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"
```

Connect your IDE debugger to `localhost:5005`.

---

## 📝 Database Schema

### Main Tables

- **user**: User account information
- **transaction**: Financial transaction records
- **category_budget**: Category-wise budget allocation
- **account_balance**: User income and savings information
- **monthly_expense_report**: Aggregated monthly reports

### Supported Category Types

- `FOOD` - Food and dining expenses
- `TRANSPORT` - Transportation costs
- `SHOPPING` - Shopping and retail
- `ENTERTAINMENT` - Entertainment and leisure
- `HEALTHCARE` - Medical expenses
- `UTILITIES` - Bills and utilities
- `EDUCATION` - Education expenses
- `OTHER` - Miscellaneous expenses

---


## 🐛 Troubleshooting

### Common Issues

#### 1. Database Connection Failed

```
Error: Could not connect to database
```

**Solution**:
- Verify MySQL is running: `systemctl status mysql`
- Check DATABASE_URL, USERNAME, and PASSWORD
- Ensure database exists: `SHOW DATABASES;`
- Check firewall rules for port 3306

#### 2. JWT Token Invalid

```
Error: Invalid JWT token
```

**Solution**:
- Ensure JWT_SECRET is at least 64 characters
- Token may have expired (default: 24 hours)
- Obtain new token by logging in again

#### 3. Email Sending Failed

```
Error: Failed to send email
```

**Solution**:
- Verify MAIL_USERNAME and MAIL_PASSWORD
- Use Google App Password, not regular password
- Enable "Less secure app access" (not recommended) or use App Password
- Check SMTP settings (port 587, TLS enabled)

#### 4. Port Already in Use

```
Error: Port 8080 is already in use
```

**Solution**:
```bash
# Find process using port 8080
lsof -i :8080

# Kill the process
kill -9 <PID>

# Or change port
server.port=8082
```

#### 5. Docker Container Fails to Start

```
Error: Container exits immediately
```

**Solution**:
```bash
# Check logs
docker-compose logs app

# Common issues:
# - Missing environment variables
# - Database not ready (wait for healthcheck)
# - Port conflict

# Restart with fresh state
docker-compose down -v
docker-compose up -d
```

---

## 📄 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

```
MIT License

Copyright (c) 2026 Manish Patidar

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
```

---

<div align="center">

**⭐ Star this repository if you find it helpful!**

Made by Manish Patidar

</div>

