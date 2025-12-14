# 🔐 JWT Authentication Service

Spring Boot backend with JWT authentication, email verification (Redis), and PostgreSQL.

## 📋 Quick Overview

**How It Works:**
1. User registers → Account created (disabled) → Verification email sent
2. User clicks email link → Account enabled
3. User logs in → Receives JWT access token (24h) + refresh token (7d)
4. User accesses protected endpoints → JWT validated on each request
5. Access token expires → Use refresh token to get new access token

**Tech Stack:** Spring Boot 3.2 • Spring Security • JWT • PostgreSQL • Redis • BCrypt

---

## 🚀 Quick Start

### Prerequisites
```bash
# Install and verify
java -version     # Java 17+
mvn -version      # Maven
psql --version    # PostgreSQL
redis-cli ping    # Redis (should return PONG)
```

**Installation links:** [Java 17](https://adoptium.net/) • [Maven](https://maven.apache.org/download.cgi) • [PostgreSQL](https://www.postgresql.org/download/) • [Redis](https://redis.io/download)

### Setup (5 steps)

**1. Start Services**
```bash
# PostgreSQL
net start postgresql-x64-14              # Windows
sudo systemctl start postgresql          # Linux/Mac

# Redis
redis-server                             # Windows/Mac
sudo systemctl start redis-server        # Linux
```

**2. Create Database**
```bash
psql -U postgres
CREATE DATABASE authdb;
\q
```

**3. Configure Application**

Edit `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/authdb
    username: postgres
    password: YOUR_POSTGRES_PASSWORD          # ⚠️ Change this
  
  mail:
    username: your-email@gmail.com            # ⚠️ Change this
    password: your-gmail-app-password         # ⚠️ Use App Password, not regular password

jwt:
  secret: YOUR_GENERATED_SECRET               # ⚠️ Generate: openssl rand -base64 32

email:
  verification:
    from: noreply@yourapp.com                 # ⚠️ Change this
```

**Gmail App Password:** Google Account → Security → 2-Step Verification → App passwords → Generate

**4. Build & Run**
```bash
mvn clean install
mvn spring-boot:run
```

**5. Verify**
```bash
curl http://localhost:8080/api/auth/test
# Response: {"message":"Authentication service is running!"}
```

---

## 📡 API Usage

### 1. Register
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","email":"john@example.com","password":"pass123"}'
```

### 2. Verify Email
Check your email → Click link or copy token
```bash
curl http://localhost:8080/api/auth/verify-email?token=TOKEN_FROM_EMAIL
```

### 3. Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"john@example.com","password":"pass123"}'
```
**Save the `accessToken` from response**

### 4. Access Protected Endpoint
```bash
curl http://localhost:8080/api/user/profile \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

### 5. Refresh Token (when access token expires)
```bash
curl -X POST http://localhost:8080/api/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{"refreshToken":"YOUR_REFRESH_TOKEN"}'
```

---

## 🏗️ Architecture

```
┌─────────────┐
│   Client    │
└──────┬──────┘
       │ 1. POST /register {email, password}
       ▼
┌─────────────────────────────────────┐
│      AuthController                 │
└──────┬──────────────────────────────┘
       │ 2. Calls AuthService
       ▼
┌─────────────────────────────────────┐
│      AuthService                    │
│  • Hash password (BCrypt)           │
│  • Save user (disabled)             │
│  • Generate verification token      │
└──────┬──────────────────────────────┘
       │ 3. User saved to PostgreSQL
       ▼
┌──────────────┐    ┌─────────────┐
│  PostgreSQL  │    │    Redis    │
│  users table │    │  token:TTL  │
└──────────────┘    └──────┬──────┘
                           │
       ┌───────────────────┘
       │ 4. Send verification email
       ▼
┌─────────────────────────────────────┐
│      EmailService                   │
│  • Store token in Redis (15min TTL)│
│  • Send email via SMTP              │
└─────────────────────────────────────┘

User clicks link → Token verified → User enabled

┌─────────────┐
│   Client    │ 5. POST /login {email, password}
└──────┬──────┘
       ▼
┌─────────────────────────────────────┐
│  Spring Security Filter Chain       │
│  • Validates credentials            │
│  • Checks if user enabled           │
└──────┬──────────────────────────────┘
       │ 6. Generate JWT tokens
       ▼
┌─────────────────────────────────────┐
│      JwtService                     │
│  • Create access token (24h)        │
│  • Create refresh token (7d)        │
│  • Sign with secret key             │
└─────────────────────────────────────┘

Future requests with JWT:

┌─────────────┐
│   Client    │ GET /api/user/profile
│ Header:     │ Authorization: Bearer <JWT>
└──────┬──────┘
       ▼
┌─────────────────────────────────────┐
│  JwtAuthenticationFilter            │
│  • Extract JWT from header          │
│  • Validate signature               │
│  • Check expiration                 │
│  • Load user from database          │
│  • Set authentication context       │
└──────┬──────────────────────────────┘
       │ ✅ Valid JWT
       ▼
┌─────────────────────────────────────┐
│  Protected Endpoint                 │
│  • Access granted                   │
└─────────────────────────────────────┘
```

**Key Components:**
- **JwtService**: Creates & validates JWT tokens
- **AuthService**: Handles registration/login logic
- **EmailService**: Manages verification emails + Redis caching
- **JwtAuthenticationFilter**: Intercepts requests, validates JWT
- **SecurityConfiguration**: Configures Spring Security (BCrypt, endpoints)

---

## 🗂️ Project Structure

```
src/main/java/com/example/auth/
├── JwtAuthApplication.java          # Entry point
├── config/
│   ├── SecurityConfiguration.java   # Spring Security + BCrypt
│   ├── RedisConfiguration.java      # Redis setup
│   └── ApplicationConfiguration.java # UserDetailsService
├── controller/
│   └── AuthController.java          # REST endpoints
├── dto/
│   ├── RegisterRequest.java         # Registration payload
│   ├── AuthRequest.java             # Login payload
│   ├── AuthResponse.java            # JWT response
│   └── RefreshTokenRequest.java     # Refresh payload
├── entity/
│   ├── User.java                    # User entity (implements UserDetails)
│   └── EmailVerificationToken.java  # Verification token entity
├── repository/
│   ├── UserRepository.java          # User CRUD
│   └── EmailVerificationTokenRepository.java
├── security/
│   └── JwtAuthenticationFilter.java # JWT validation filter
└── service/
    ├── JwtService.java              # JWT creation/validation
    ├── AuthService.java             # Business logic
    └── EmailService.java            # Email + Redis
```

---

---

## 🔍 Verify Setup

**Check Database:**
```bash
psql -U postgres -d authdb
SELECT * FROM users;
\q
```

**Check Redis:**
```bash
redis-cli
KEYS email_verification:*
exit
```

