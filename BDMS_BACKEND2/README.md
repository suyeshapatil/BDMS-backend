# Blood Donation Management System (BDMS) — Backend

Spring Boot 3 / Java 21 REST API backend for the CDAC Final Year Major Project
**Blood Donation Management System**, built against the SRS (Project Code `PGCP-AC-027`)
and the attached `blooddonationdb` MySQL schema.

> **Note on source-of-truth:** The SRS's ER diagram describes a data model (e.g. a
> separate `donor_availability` table, `first_name`/`last_name` columns, a
> `request_responses` table) that does **not** match the attached SQL dump exactly.
> Per your instruction, this backend was built to follow the **SQL schema exactly** —
> table names, column names, and enum values are unchanged. Where the SRS describes a
> business rule that isn't literally a column (e.g. donor eligibility windows), that
> rule is implemented in the service layer instead of the schema (see
> `DonorEligibilityUtil`).

---

## 1. Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.3.2 |
| Security | Spring Security 6 + JWT (jjwt 0.12.6) |
| Persistence | Spring Data JPA / Hibernate |
| Database | MySQL 8 |
| Docs | springdoc-openapi (Swagger UI) |
| Build | Maven |
| Boilerplate | Lombok |
| Mail | Spring Mail (JavaMailSender) |

---

## 2. Project Structure

```
BloodDonationManagementSystem_Backend/
├── pom.xml
├── README.md
├── .gitignore
├── BDMS_Postman_Collection.json
└── src/
    ├── main/
    │   ├── java/com/bdms/backend/
    │   │   ├── BdmsApplication.java
    │   │   ├── config/          # SecurityConfig, SwaggerConfig
    │   │   ├── controller/      # 11 REST controllers
    │   │   ├── dto/
    │   │   │   ├── request/     # 17 request DTOs
    │   │   │   └── response/    # 11 response DTOs
    │   │   ├── entity/          # 10 JPA entities (1:1 with SQL tables)
    │   │   ├── enums/           # Role, RequestStatus, UrgencyLevel, ResponseStatus, ApprovalStatus
    │   │   ├── exception/       # Custom exceptions + GlobalExceptionHandler
    │   │   ├── mapper/          # 9 Entity -> DTO mappers
    │   │   ├── repository/      # 10 Spring Data JPA repositories
    │   │   ├── security/        # JWT filter, JwtUtil, UserPrincipal, UserDetailsService
    │   │   ├── service/         # Interfaces
    │   │   │   └── impl/        # Implementations
    │   │   └── util/            # ApiResponse, Constants, DonorEligibilityUtil
    │   └── resources/
    │       ├── application.properties
    │       ├── application-dev.properties
    │       └── application-prod.properties
    └── test/java/com/bdms/backend/
```

---

## 3. Setup Instructions

### 3.1 Prerequisites
- JDK 21
- Maven 3.9+
- MySQL 8.x running locally (or reachable)
- An SMTP account (e.g. a Gmail App Password) if you want email notifications to actually send

### 3.2 Create the database
Run the SQL dump you already have (creates `blooddonationdb` and all 10 tables):

```bash
mysql -u root -p < blooddonationdb.sql
```

### 3.3 Configure `application-dev.properties`
Only these 5 values need to change — nothing else in the code needs to be touched:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/blooddonationdb?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=your_password

app.jwt.secret=ChangeThisSecretKeyToSomethingLongAndRandom...

spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
app.mail.from=your_email@gmail.com
```

`spring.jpa.hibernate.ddl-auto=validate` is intentional — Hibernate will **validate**
that your entities match the existing schema on startup rather than trying to alter it,
since the SQL schema is the source of truth.

### 3.4 Build & run

```bash
mvn clean install
mvn spring-boot:run
```

Or run the packaged jar:

```bash
java -jar target/bdms-backend.jar
```

The API starts on **http://localhost:8080**.

### 3.5 Import into Eclipse
`File → Import → Existing Maven Projects` → select the extracted project folder →
Finish. No further configuration needed beyond step 3.3.

### 3.6 Swagger UI
Once running: **http://localhost:8080/swagger-ui.html**
(OpenAPI JSON at `/v3/api-docs`)

---

## 4. Default Login Credentials

**None are seeded.** The database starts empty (per the SQL dump — no `INSERT`
statements are included). Create your first admin account via:

```
POST /api/auth/register
{
  "fullName": "System Admin",
  "email": "admin@bdms.local",
  "password": "Admin@123",
  "role": "ADMIN",
  "designation": "Super Admin"
}
```

Then use `/api/auth/login` to obtain a JWT and manage the system from there.

---

## 5. Authentication Flow

1. `POST /api/auth/register` — creates a `users` row plus the matching
   `donors` / `hospitals` / `admins` row based on `role`.
2. `POST /api/auth/login` — returns `accessToken` (1 hour) + `refreshToken` (7 days).
3. Send `Authorization: Bearer <accessToken>` on every subsequent request.
4. `POST /api/auth/refresh-token` — exchange a valid refresh token for a new pair.
5. `POST /api/auth/forgot-password` / `POST /api/auth/reset-password` — stateless,
   JWT-based reset tokens (15-minute expiry), emailed to the user. No extra DB table
   was added, to keep the schema untouched.
6. `POST /api/auth/logout` — JWT is stateless; logout is a client-side token discard.

Roles (`ADMIN`, `DONOR`, `HOSPITAL`) are embedded in the JWT and enforced by
`SecurityConfig` via `hasRole(...)` / `hasAnyRole(...)` per endpoint group.

---

## 6. Implemented Modules (SRS §6)

| SRS Module | Implementation |
|---|---|
| 6.1 User Authentication | `AuthController` / `AuthService` — register, login, JWT, BCrypt, refresh, forgot/reset/change password, logout |
| 6.2 Donor Management | `DonorController` / `DonorService` — profile CRUD, availability toggle, search by blood group/city |
| 6.3 Hospital Management | `HospitalController` / `BloodRequestController` — registration (via auth), profile, verified-hospital listing |
| 6.4 Blood Request Management | `BloodRequestController` / `BloodRequestService` — create, status lifecycle, donor-matching, history |
| 6.5 Notification Module | `NotificationController` + `EmailService`, triggered automatically on: blood request creation (matching, *eligible* donors only), donor registration (alerts admins), donor accept (alerts hospital), donation recorded (thank-you) |
| 6.6 Reporting & Dashboard | `ReportController`, `DashboardController` |
| 6.7 Performance Monitoring | `DashboardController#/stats` (user/donor/hospital/request/donation counts) |
| 6.8 Admin Management | `AdminUserController`, `HospitalApprovalController` |
| 6.9 Donor Response Management | `DonorResponseController` / `DonorResponseService` — accept/reject, status tracking |
| 6.10 Donation History Management | `DonationHistoryController` / `DonationHistoryService` — record, view, rollup stats |

**Out of scope (no supporting table in the provided SQL schema):** SMS notifications,
blood donation camp entities, GPS-based tracking — all listed under SRS §7.5 as
*future* requirements, not current ones.

---

## 7. Role-Based Access Summary

| Endpoint prefix | Allowed roles |
|---|---|
| `/api/auth/**` | Public |
| `/api/hospitals/verified` (GET) | Public |
| `/api/admin/**`, `/api/dashboard/**`, `/api/reports/**`, `/api/hospital-approvals/**` | ADMIN |
| `/api/donors/**` | ADMIN, DONOR, HOSPITAL |
| `/api/hospitals/**` | ADMIN, HOSPITAL |
| `/api/blood-requests/**` | ADMIN, HOSPITAL, DONOR |
| `/api/donor-responses/**` | ADMIN, DONOR, HOSPITAL |
| `/api/donation-history/**` (POST) | ADMIN, HOSPITAL |
| `/api/donation-history/**` (GET) | ADMIN, DONOR, HOSPITAL |
| `/api/notifications/**` | Any authenticated user |

---

## 8. Sample Requests

**Register a donor**
```http
POST /api/auth/register
Content-Type: application/json

{
  "fullName": "Rohan Patil",
  "email": "rohan@example.com",
  "password": "Passw0rd!",
  "role": "DONOR",
  "age": 24,
  "gender": "Male",
  "bloodGroup": "O+",
  "mobile": "9876543210",
  "city": "Pune",
  "address": "MG Road, Pune"
}
```

**Response**
```json
{
  "success": true,
  "message": "Registration successful",
  "data": {
    "userId": 1,
    "fullName": "Rohan Patil",
    "email": "rohan@example.com",
    "role": "DONOR",
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
    "tokenType": "Bearer"
  },
  "timestamp": "2026-07-28T10:15:00"
}
```

**Create a blood request (as a verified hospital)**
```http
POST /api/blood-requests
Authorization: Bearer <hospitalAccessToken>
Content-Type: application/json

{
  "patientName": "Anita Sharma",
  "bloodGroup": "O+",
  "unitsRequired": 2,
  "city": "Pune",
  "urgencyLevel": "CRITICAL"
}
```

**Validation error response (400)**
```json
{
  "success": false,
  "message": "Validation failed",
  "data": {
    "bloodGroup": "Blood group must be a valid group e.g. A+, O-, AB+"
  },
  "timestamp": "2026-07-28T10:16:00"
}
```

---

## 9. Known Design Decisions / Limitations

- **Refresh tokens are stateless** (a longer-lived signed JWT, not stored in a DB
  table) since the SQL schema has no token-storage table. This means a refresh token
  cannot be individually revoked before expiry — acceptable for an academic project,
  but call this out if asked about production hardening.
- **Password reset tokens** are likewise stateless short-lived JWTs (15 min), emailed
  to the user, for the same reason.
- No SMS gateway integration (SRS lists this as a future requirement, §7.5).
- No blood donation camp / event entity (not present in the SQL schema).
- `spring.jpa.hibernate.ddl-auto=validate` — you must run the provided SQL script
  before first boot; the app will not create tables for you.

---

## 10. Postman Collection

Import `BDMS_Postman_Collection.json`. It defines a collection variable
`{{baseUrl}}` (default `http://localhost:8080`) and `{{accessToken}}` /
`{{refreshToken}}` which are **auto-populated** by a test script on the Login and
Register requests, so you can run the folder top-to-bottom without manually copying
tokens.
