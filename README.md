# FineMyTrip Backend

A Spring Boot-based backend API for travel product management with JWT authentication.

## Project Introduction

FineMyTrip Backend is a RESTful API service that provides comprehensive travel product management functionality. The application features secure JWT-based authentication, allowing users to manage travel products, main slides, and user accounts through a well-structured API.

## Key Features

### 1. Main Slide Management
- Data processing for main slides
- Slide image files upload support
- Slide URL, title, and headline management

### 2. Travel Product Management
- Data processing for travel products
- Product image file upload support
- Product type, title, information groups management
- Previous and current price reflecting discount rate
- Purchase count and review rate tracking

### 3. User Management (JWT Authentication)
- User registration, deletion
- User login with JWT token issuance
- User logout with token blacklisting

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
│   │   ├── WebConfig.java
│   │   └── DataLoader.java
│   ├── controller/             # REST API controllers
│   │   ├── MemberController.java
│   │   ├── ProductController.java
│   │   └── MainSlideController.java
│   ├── service/                # Business logic services
│   │   ├── MemberService.java
│   │   ├── ProductService.java
│   │   ├── MainSlideService.java
│   │   ├── FileUploadService.java
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
│   │   ├── MemberRegisterRequestDto.java
│   │   ├── MemberLoginRequestDto.java
│   │   ├── LoginResponseDto.java
│   │   ├── MemberResponseDto.java
│   │   ├── ProductRequestDto.java
│   │   ├── ProductResponseDto.java
│   │   ├── MainSlideRequestDto.java
│   │   └── MainSlideResponseDto.java
│   ├── util/                   # Utility classes
│   │   ├── JwtUtil.java
│   │   └── TokenBlacklist.java
│   └── exception/              # Exception handling
│       └── GlobalExceptionHandler.java
└── src/main/resources/
│   ├── application.properties  # Application configuration
│   ├── application-dev.properties
│   └── application-prod.properties
├── uploads/                    # File upload directory (auto-created)
├── build.gradle                # Gradle build configuration
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
- `DELETE /api/members/{id}` - User deletion by ID (requires JWT token)
- `DELETE /api/members/email/{email}` - User deletion by email (requires JWT token)

### User Management (Public Read, Authenticated Write)
- `GET /api/members` - Get all users (public)
- `GET /api/members/{id}` - Get user by ID (public)
- `GET /api/members/email/{email}` - Get user by email (public)
- `POST /api/members/logout` - User logout (requires JWT token)

### Main Slides (Public Read, Authenticated Write)
- `GET /api/main-slides` - Get all slides (public)
- `GET /api/main-slides/{id}` - Get slide by ID (public)
- `POST /api/main-slides` - Create slide (multipart/form-data, requires JWT token)
- `PUT /api/main-slides/{id}` - Update slide (multipart/form-data, requires JWT token)
- `DELETE /api/main-slides/{id}` - Delete slide (requires JWT token)

### Products (Public Read, Authenticated Write)
- `GET /api/products` - Get all products (public)
- `GET /api/products/{id}` - Get product by ID (public)
- `POST /api/products` - Create product (multipart/form-data, requires JWT token)
- `PUT /api/products/{id}` - Update product (multipart/form-data, requires JWT token)
- `DELETE /api/products/{id}` - Delete product (requires JWT token)

### File Access (Public)
- `GET /uploads/{filename}` - Access uploaded files (public)

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

## File Upload System

### Supported Features
- **File Types**: All common image formats (JPG, PNG, GIF, etc.)
- **File Size**: Maximum 10MB per file
- **Storage**: Local file system
- **Access**: Static resource serving via `/uploads/{filename}`

### Environment-specific Behavior
- **Development**  
  - When the application starts, the local H2 database files (e.g., `finemytrip.mv.db`) are stored in the `./data` folder at the project root.  
  - The `./data` folder is created automatically; include it in version control or add it to `.gitignore` as needed.  
- **Uploads**  
  - Files are stored in the `./uploads/` folder at the project root.

## Configuration Files

Since all `application*.properties` files are excluded from Git for security reasons, you need to create your own configuration files locally.

### Required Configuration Files

#### 1. `src/main/resources/application.properties`
```properties
# Application Configuration
spring.application.name=backend

# Profile Configuration
spring.profiles.active=dev

# Basic JWT Configuration
jwt.secret.key=${JWT_SECRET_KEY:YOUR_SECRET_HERE}
jwt.expiration.time=86400000

# File Upload Configuration
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
file.upload.path=${user.dir}/uploads
file.upload.url-prefix=/uploads

# CORS Configuration
cors.allowed-origins=${CORS_ALLOWED_ORIGINS}
```

#### 2. `src/main/resources/application-dev.properties`
```properties
# Development Environment Configuration

# JWT Configuration
jwt.secret.key=${JWT_SECRET_KEY:YOUR_SECRET_HERE}
jwt.expiration.time=86400000

# Database Configuration
spring.datasource.url=jdbc:h2:file:./data/finemytrip
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA Configuration
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Logging Configuration
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE

# File Upload Configuration
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
file.upload.path=${user.dir}/uploads
file.upload.url-prefix=/uploads

# CORS Configuration (Development)
cors.allowed-origins=${CORS_ALLOWED_ORIGINS}

# H2 Console (Development only)
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

#### 3. `src/main/resources/application-prod.properties`
```properties
# Production Environment Configuration

# JWT Configuration
jwt.secret.key=${JWT_SECRET_KEY:YOUR_SECRET_HERE}
jwt.expiration.time=86400000

# Database Configuration (PostgreSQL)
spring.datasource.url=jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:finemytrip}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD:postgrespassword}
spring.datasource.driver-class-name=org.postgresql.Driver
# Optional: HikariCP tuning
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5

# JPA Configuration
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false

# Logging Configuration (Minimal logging in production)
logging.level.org.hibernate.SQL=WARN
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=WARN

# File Upload Configuration
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
file.upload.path=/var/finemytrip/uploads
file.upload.url-prefix=/uploads

# CORS Configuration (Production)
cors.allowed-origins=${CORS_ALLOWED_ORIGINS}

# H2 Console Disabled
spring.h2.console.enabled=false

# Security Configuration
spring.security.require-ssl=true

# Logging Configuration
logging.level.root=WARN
logging.level.finemytrip.backend=INFO
logging.level.org.springframework.security=WARN
```

### Security Best Practices
1. **Never commit sensitive information** like secret keys or passwords
2. **Use environment variables** for all sensitive configuration
3. **Disable H2 console** in production environment
4. **Use HTTPS** in production
5. **Regularly rotate JWT secret keys**
6. **Monitor application logs** for security events

### Environment Variables

#### Development Environment
```bash
# Set JWT secret key (optional for development)
export JWT_SECRET_KEY=your-secret-key-here

# Run with dev profile
./gradlew bootRun --args='--spring.profiles.active=dev'
```

#### Production Environment
```bash
# Required environment variables
export JWT_SECRET_KEY=your-secure-secret-key
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=finemytrip
export DB_USERNAME=your-username
export DB_PASSWORD=your-password
export CORS_ALLOWED_ORIGINS=https://your.domain.com
export FILE_UPLOAD_PATH=/var/uploads
export FILE_UPLOAD_URL_PREFIX=/uploads

# Run with prod profile
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

### Database Access (Development)
- **H2 Console**: http://localhost:8080/h2-console
- **JDBC URL**: jdbc:h2:file:./data/finemytrip
- **Username**: sa
- **Password**: (empty)

## Development

The application runs on port 8080 by default. All API endpoints are prefixed with `/api/` and follow RESTful conventions.

### Testing Authentication
1. Register a new user
2. Login to get JWT token
3. Use the token in Authorization header for protected endpoints
4. Logout to invalidate the token 