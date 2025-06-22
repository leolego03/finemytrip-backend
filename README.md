# FineMyTrip Backend

A Spring Boot-based backend API for travel product management with JWT authentication.

## Project Introduction

FineMyTrip Backend is a RESTful API service that provides comprehensive travel product management functionality. The application features secure JWT-based authentication, allowing users to manage travel products, main slides, and user accounts through a well-structured API.

## Key Features

### 1. Main Slide Management
- Data processing for main slides
- Image URL, title, and description management
- Active/inactive status toggle
- Sort order management

### 2. Travel Product Management
- Data processing for travel products
- Product name, price, description, and image URL management
- Discount rate and badge support
- Purchase count and review rate tracking

### 3. User Management (JWT Authentication)
- User registration
- User login with JWT token issuance
- User logout with token blacklisting
- User profile management

### 4. Security Features
- JWT-based authentication
- Token blacklisting for logout
- Role-based access control
- Secure password encoding

## Project Structure

```
finemytrip-backend/
├── src/main/java/finemytrip/backend/
│   ├── config/                 # Configuration classes
│   │   ├── SecurityConfig.java
│   │   ├── JwtAuthenticationFilter.java
│   │   └── DataLoader.java
│   ├── controller/             # REST API controllers
│   │   ├── MemberController.java
│   │   ├── ProductController.java
│   │   └── MainSlideController.java
│   ├── service/                # Business logic services
│   │   ├── MemberService.java
│   │   ├── ProductService.java
│   │   ├── MainSlideService.java
│   │   └── CustomUserDetailsService.java
│   ├── repository/             # Data access layer
│   │   ├── MemberRepository.java
│   │   ├── ProductRepository.java
│   │   └── MainSlideRepository.java
│   ├── entity/                 # JPA entities
│   │   ├── Member.java
│   │   ├── Product.java
│   │   └── MainSlide.java
│   ├── dto/                    # Data Transfer Objects
│   │   ├── MemberRequestDto.java
│   │   ├── ProductRequestDto.java
│   │   └── MainSlideRequestDto.java
│   ├── util/                   # Utility classes
│   │   ├── JwtUtil.java
│   │   └── TokenBlacklist.java
│   └── exception/              # Exception handling
│       └── GlobalExceptionHandler.java
└── src/main/resources/
    └── application.properties  # Application configuration
```

## Technology Stack

### Backend Framework
- **Spring Boot 3.x** - Main application framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Data persistence layer
- **H2 Database** - In-memory database for development

### Build & Dependency Management
- **Gradle** - Build automation tool
- **JJWT** - JWT token processing library

### Development Tools
- **Java 17+** - Programming language
- **H2 Console** - Database management interface

## API Endpoints

### Authentication (Public)
- `POST /api/members/register` - User registration
- `POST /api/members/login` - User login (returns JWT token)

### User Management (Authenticated)
- `POST /api/members/logout` - User logout (requires JWT token)
- `GET /api/members/{id}` - Get user by ID
- `GET /api/members` - Get all users

### Main Slides (Public Read, Authenticated Write)
- `GET /api/main-slides` - Get active slides
- `GET /api/main-slides/{id}` - Get slide by ID
- `POST /api/main-slides` - Create slide (requires JWT token)
- `PUT /api/main-slides/{id}` - Update slide (requires JWT token)
- `DELETE /api/main-slides/{id}` - Delete slide (requires JWT token)
- `PATCH /api/main-slides/{id}/toggle` - Toggle slide status (requires JWT token)

### Products (Public Read, Authenticated Write)
- `GET /api/products` - Get all products
- `GET /api/products/{id}` - Get product by ID
- `POST /api/products` - Create product (requires JWT token)
- `PUT /api/products/{id}` - Update product (requires JWT token)
- `DELETE /api/products/{id}` - Delete product (requires JWT token)

## JWT Authentication

### Authentication Flow
1. **Register**: `POST /api/members/register`
2. **Login**: `POST /api/members/login` → Returns JWT token
3. **API Access**: Include `Authorization: Bearer {token}` header
4. **Logout**: `POST /api/members/logout` → Adds token to blacklist

### Security Configuration
- **Public endpoints**: Registration, login, and read operations
- **Protected endpoints**: All write operations require JWT authentication
- **Token expiration**: 24 hours
- **Algorithm**: HMAC-SHA256
- **Claims**: email, memberId

## Configuration Files

Since all `application*.properties` files are excluded from Git for security reasons, you need to create your own configuration files locally.

### Required Configuration Files

#### 1. `src/main/resources/application.properties`
```properties
spring.application.name=backend

# Profile Configuration
spring.profiles.active=dev

# Basic JWT Configuration
jwt.secret.key=${JWT_SECRET_KEY:defaultSecretKey1234567890123456789012345678901234567890}
jwt.expiration.time=86400000
```

#### 2. `src/main/resources/application-dev.properties`
```properties
# Development Environment Configuration

# Database Configuration
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# H2 Console (Development only)
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA Configuration
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Logging Configuration
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE

# JWT Configuration
jwt.secret.key=${JWT_SECRET_KEY:devSecretKey1234567890123456789012345678901234567890}
jwt.expiration.time=86400000
```

#### 3. `src/main/resources/application-prod.properties`
```properties
# Production Environment Configuration

# Database Configuration (Use external database in production)
spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DATABASE_USERNAME}
spring.datasource.password=${DATABASE_PASSWORD}

# H2 Console (Disabled in production)
spring.h2.console.enabled=false

# JPA Configuration
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false

# Logging Configuration (Minimal logging in production)
logging.level.org.hibernate.SQL=WARN
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=WARN
logging.level.finemytrip.backend=INFO

# JWT Configuration
jwt.secret.key=${JWT_SECRET_KEY}
jwt.expiration.time=86400000

# Security Configuration
spring.security.require-ssl=true
```

### Security Best Practices
1. **Never commit sensitive information** like secret keys or passwords
2. **Use environment variables** for all sensitive configuration
3. **Disable H2 console** in production environment
4. **Use HTTPS** in production
5. **Regularly rotate JWT secret keys**
6. **Monitor application logs** for security events

### Profile Configuration
- **dev**: Development environment with H2 console enabled
- **prod**: Production environment with enhanced security

To run with specific profile:
```bash
./gradlew bootRun --args='--spring.profiles.active=prod'
```

## Getting Started

### Prerequisites
- Java 17 or higher
- Gradle 8.x

### Installation & Running

1. Clone the repository
```bash
git clone [repository-url]
cd finemytrip-backend
```

2. Run the application
```bash
./gradlew bootRun
```

3. Test JWT authentication
```bash
./test_jwt.sh
```

### Database Access
- **H2 Console**: http://localhost:8080/h2-console
- **JDBC URL**: jdbc:h2:mem:testdb
- **Username**: sa
- **Password**: (empty)

## Development

The application runs on port 8080 by default. All API endpoints are prefixed with `/api/` and follow RESTful conventions.

### Testing Authentication
1. Register a new user
2. Login to get JWT token
3. Use the token in Authorization header for protected endpoints
4. Logout to invalidate the token 