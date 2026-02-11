<div align="center">

# 💰 Tijori – Personal Expense Tracker

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Flutter](https://img.shields.io/badge/Flutter-3.x-blue.svg)](https://flutter.dev/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Demo](https://img.shields.io/badge/Demo-YouTube-red.svg)](https://youtu.be/wlA9ZibAPj8)

**A modern, secure, and scalable expense management platform**

</div>


---

**Tijori** (means *"vault"* in Hindi) is a smart personal expense tracking app that makes managing money simple and efficient.  
The app automatically reads bank SMS messages and processes them into structured financial records.

Built with **Flutter** for the mobile application and **Spring Boot** for the backend, Tijori is a complete full-stack project designed with security, performance, and scalability in mind.

---

<div align="center">

## What This Project Does

Tijori helps you track where your money goes by:
- Reading bank transaction SMS automatically
- Sorting expenses into categories (food, transport, shopping, etc.)
- Showing you budget reports and spending patterns
- Sending alerts when you're close to your budget limit

**Tech Stack:**
- **Frontend**: Flutter (works on Android & iOS)
- **Backend**: Spring Boot 3.5 with Java 21
- **Database**: MySQL 8.0
- **Monitoring**: Spring Boot Admin Dashboard

---


## Screenshots

| ![Register](Resources/Register.jpg) | ![Dashboard](Resources/Dashboard.jpg) | ![Income](Resources/Income.jpg) | ![Profile](Resources/Profile.jpg) |
|---|---|---|---|

---

### Demo
https://youtu.be/wlA9ZibAPj8

---

## 🚀 Backend Features

The backend is built with Spring Boot 3.5 and uses several production-ready tools and practices:

### What Makes the Backend Good

**Authentication & Security**
- JWT tokens for secure login (no sessions needed)
- Rate limiting to prevent API spam (using Bucket4j)
- Spring Security with custom filters
- CORS configuration for frontend access
- Password encryption with BCrypt

**Smart Transaction Handling**
- Parses bank SMS messages automatically
- Categories: Food, Transport, Shopping, Healthcare, Entertainment, etc.
- Bulk save operations for multiple transactions
- Real-time balance calculation
- Monthly expense reports

**Performance Features**
- Caffeine cache to speed up repeated queries
- HikariCP connection pool for database
- Async email sending (doesn't block API calls)
- Request/Response logging for debugging

**Email & Notifications**
- OTP verification for email (6-digit code with expiry)
- Budget alert emails when you overspend
- HTML email templates
- Works with Gmail SMTP

**Spring Boot Admin Dashboard**
We've added a separate Admin Server that monitors the main application in real-time:
- See app health status and metrics
- Check memory usage and thread counts
- View HTTP request statistics
- Change log levels without restarting
- Access at http://localhost:8081 (username: admin, password: admin)

This is a separate Spring Boot app that runs alongside the main backend!

**API Documentation**
- Swagger UI for testing APIs in browser
- OpenAPI 3.0 specification
- Try all endpoints at http://localhost:8080/swagger-ui.html

**Developer Friendly**
- Three profiles: dev, prod, test
- Docker setup included
- Maven wrapper (no need to install Maven)
- Clean error messages with custom exceptions

### Tech Used

| What | Technology |
|------|---------|
| Language | Java 21 |
| Framework | Spring Boot 3.5.10 |
| Security | Spring Security + JWT |
| Database | MySQL 8.0 + JPA |
| Caching | Caffeine |
| Rate Limiting | Bucket4j 8.10.1 |
| API Docs | Swagger |
| Monitoring | Spring Boot Admin 3.5.7 |
| Metrics | Micrometer |
| Build Tool | Maven |
| Container | Docker + Docker Compose |

### Design Patterns Used

- **Layered Architecture**: Controller → Service → Repository (clean separation)
- **DTO Pattern**: Separate objects for requests/responses
- **Dependency Injection**: Constructor injection with Lombok
- **Repository Pattern**: Spring Data JPA
- **AOP**: For rate limiting with annotations

---

## How It Works

Here's the architecture diagram showing how everything connects:

![System Architecture](Resources/ArchitectureDiagram.png)

**The Flow:**
1. User uses the Flutter mobile app
2. App talks to Firebase for OTP and SMS reading
3. App sends requests to our Spring Boot backend (REST APIs)
4. Backend handles auth using JWT filter
5. Business logic in Service layer processes requests
6. Repository layer talks to MySQL database
7. Spring Boot Admin monitors everything

---

## Transaction Flow

Here's what happens when you add an expense:

![Sequence Diagram](Resources/SequenceDiagram.png)

**Step by Step:**
1. You open the app
2. App reads your SMS messages
3. Finds bank transaction messages
4. Extracts amount, date, and merchant name
5. Sends this data to backend API
6. Backend validates and saves to database
7. Calculates your new balance
8. If you're over budget, sends you an alert
9. App shows updated dashboard

---

## Project Structure

```
Tijori/
│
├── Backend/      # Enterprise-grade Spring Boot 3.5 backend
│   ├── src/      # Source code with layered architecture
│   ├── pom.xml   # Maven dependencies and build configuration
│   ├── Dockerfile # Container configuration
│   ├── docker-compose.yml # Multi-service orchestration
│   ├── AdminServer/ # Spring Boot Admin monitoring dashboard
│   └── README.md # Comprehensive setup and API documentation
│
└── Frontend/     # Flutter cross-platform mobile app
    ├── lib/      # Dart source code
    ├── pubspec.yaml # Flutter dependencies
    ├── android/  # Android-specific configuration
    ├── ios/      # iOS-specific configuration
    └── assets/   # Images, animations, and resources
```

---

## ✨ Main Features

### Mobile App (Flutter)
- Automatically reads bank SMS and extracts transactions
- Beautiful charts to visualize your spending
- Categories like Food, Transport, Shopping, etc.
- Set budget limits and get alerts
- Secure storage for your data
- Works on Android and iOS
- Smooth animations using Lottie

### Backend (Spring Boot)
- RESTful APIs for all operations
- JWT authentication (no password storage in app)
- Email OTP verification
- Real-time balance calculation
- Category-wise budget tracking
- Monthly expense reports
- Fully documented with Swagger
- Docker ready for easy deployment

### Admin Dashboard
- Separate Spring Boot Admin app
- Monitor backend health in real-time
- See memory and CPU usage
- Track API response times
- View and change log levels
- Check database connection status

Check the detailed [Backend README](Backend/README.md) for complete API docs and setup instructions.

---

## 🚀 Quick Start

### What You Need

- Java 21 or higher
- Maven 3.8+ (or just use the Maven wrapper included)
- MySQL 8.0 (or use Docker)
- Flutter SDK 3.0+
- Docker & Docker Compose (easiest way)

### Fastest Way: Use Docker

This starts everything - backend, database, AND admin server:

```bash
# Clone the repo
git clone https://github.com/ManishPatidar806/Tijori.git
cd Tijori/Backend

# Start all services
docker-compose up -d

# Check if everything is running
docker-compose ps
```

**Now you can access:**
- Main Backend API: http://localhost:8080
- Swagger UI (test APIs): http://localhost:8080/swagger-ui.html  
- **Admin Dashboard: http://localhost:8081** (login: admin / admin)
- MySQL Database: localhost:3306

### Run Backend Locally

If you want to run without Docker:

```bash
cd Backend

# Set up environment variables
export DATABASE_URL="jdbc:mysql://localhost:3306/expenses_db"
export DATABASE_USERNAME="expenses_user"
export DATABASE_PASSWORD="your_password"
export JWT_SECRET="make-this-at-least-64-characters-long-for-security"
export MAIL_USERNAME="your-email@gmail.com"
export MAIL_PASSWORD="your-gmail-app-password"

# Run it
./mvnw spring-boot:run
```

**To run Admin Server separately:**
```bash
cd AdminServer
./mvnw spring-boot:run
```
Access at http://localhost:8081

### Run Flutter App

```bash
cd Frontend

# Get dependencies
flutter pub get

# Run on connected device
flutter run

# Or build APK for Android
flutter build apk
```

For complete setup details, database configuration, and troubleshooting, see [Backend/README.md](Backend/README.md)

---

## 📚 API Endpoints

Once backend is running, open Swagger UI to test all APIs:
**http://localhost:8080/swagger-ui.html**

### Main API Routes

**User/Auth** (`/v1/api/users`)
- `POST /signup` - Register new user
- `POST /signin` - Login (returns JWT token)
- `GET /profile` - Get your profile (needs token)
- `PUT /profile` - Update profile
- `DELETE /profile` - Delete account

**Transactions** (`/v1/api/transactions`)
- `POST /save` - Save SMS transactions
- `GET /categorization` - Get uncategorized transactions
- `GET /category?category=FOOD` - Filter by category
- `POST /categorization` - Update categories

**Email Verification** (`/v1/api/emailVerification`)
- `POST /sendOtp?email=user@example.com` - Send OTP
- `POST /verifyOtp?email=user@example.com&otp=123456` - Verify OTP

**Account Balance** (`/v1/api/account-balance`)
- `POST /income?income=50000&saving=10000` - Set income & savings
- `GET /` - Get current balance

**Category Budgets** (`/v1/api/category-budgets`)
- `GET /amounts` - Get spending by category

### How to Use APIs

```bash
# 1. Login first
curl -X POST http://localhost:8080/v1/api/users/signin \
  -H "Content-Type: application/json" \
  -d '{"mobileNo": "9876543210"}'

# You'll get a token in response

# 2. Use token for other APIs
curl -X GET http://localhost:8080/v1/api/transactions/categorization \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

Full API documentation with examples: [Backend/README.md](Backend/README.md)

---

## 🛠️ Technologies Used

### Backend Stack
- **Java 21** - Latest LTS version
- **Spring Boot 3.5.10** - Main framework
- **MySQL 8.0** - Database
- **Spring Security + JWT** - Authentication
- **Caffeine** - Caching layer
- **Bucket4j** - Rate limiting
- **Spring Boot Admin 3.5.7** - Monitoring dashboard
- **Swagger/OpenAPI** - API documentation
- **Docker** - Containerization

### Frontend Stack
- **Flutter 3.x** - Mobile framework
- **Dart** - Programming language
- **fl_chart** - For graphs and charts
- **Lottie** - Animations
- **flutter_secure_storage** - Secure data storage
- **http** - API calls

---

## 🎯 Key Features

### SMS Transaction Parser
The app reads your bank SMS and extracts:
- How much was spent
- When it was spent
- Whether it's debit or credit


### Budget Management
You can set monthly budgets for different categories:
- **Food**: Restaurants, groceries, food delivery
- **Transport**: Fuel, cab, metro, bus
- **Shopping**: Clothes, electronics, online shopping
- **Healthcare**: Doctor, medicines, hospital bills
- **Entertainment**: Movies, Netflix, games
- **Utilities**: Rent, electricity, water, internet
- **Education**: Books, courses, fees
- **Other**: Everything else

### Budget Alerts
The system watches your spending and notifies you when:
- You're close to your budget limit for a category
- You've spent more than your monthly target
- There's unusual spending activity

### Spring Boot Admin Dashboard
A complete monitoring dashboard that shows:
- Is the app running properly?
- How much memory is being used?
- How many requests per minute?
- Database connection status
- Recent errors and logs
- You can even change log levels without restart!

---

## 📊 Performance Features

We've added several optimizations to make the backend fast:

- **Caching**: Frequently used data is cached with Caffeine (faster response times)
- **Connection Pool**: HikariCP manages database connections efficiently
- **Async Processing**: Emails are sent in background (doesn't slow down API)
- **Database Indexes**: Queries are optimized with proper indexes
- **Rate Limiting**: Prevents too many requests from same user
- **Response Compression**: API responses are compressed (smaller data transfer)

---

## 🔒 Security

**What We've Implemented:**
- **JWT Tokens**: Secure login without storing passwords in app
- **Token Expiry**: Tokens expire after 24 hours
- **Rate Limiting**: Max 100 requests per minute per user
- **CORS**: Only allowed origins can access API
- **Input Validation**: All inputs are validated before processing
- **SQL Injection Protection**: Using JPA parameterized queries
- **Password Hashing**: BCrypt for password encryption
- **HTTPS Ready**: Can be configured for SSL/TLS

---

## 🐳 Docker Setup

The `docker-compose.yml` file starts three containers:
1. **Main App** - Spring Boot backend on port 8080
2. **MySQL Database** - Database on port 3306
3. **Admin Server** - Monitoring dashboard on port 8081

```bash
# Start everything
docker-compose up -d

# Check status
docker-compose ps

# View logs
docker-compose logs -f

# Stop everything
docker-compose down
```

Each container has health checks built-in, so they start in the right order.

---

## 📈 Monitoring

### Actuator Endpoints
- `/actuator/health` - Check if app is running
- `/actuator/metrics` - See performance metrics
- `/actuator/info` - Build and version info

### Custom Metrics We Track
- How many users registered
- How many logins happened
- How many transactions saved
- API response times
- Cache hit/miss ratios

### Logs
- Different log levels for dev and production
- All API requests are logged with response time
- Errors are logged with full details

---

## 📱 Frontend Features

- **Cross-Platform**: One codebase runs on Android and iOS
- **Nice UI**: Material Design with smooth animations
- **Lottie Animations**: Beautiful loading and success animations
- **Secure Storage**: Sensitive data is encrypted locally
- **Charts**: Interactive graphs showing your spending
- **Offline Ready**: Works even without internet (uses local cache)
- **SMS Reading**: Automatically reads bank transaction SMS

---

## 📄 License

MIT License - see [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Developer

Built by **Manish Patidar**

- Watch the demo: https://youtu.be/wlA9ZibAPj8
- GitHub: https://github.com/ManishPatidar806
- Report issues: https://github.com/ManishPatidar806/Tijori/issues

⭐ Star this repo if you find it useful!

---
