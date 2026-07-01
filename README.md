# 🔐 Authify Backend

Authify Backend is a secure REST API built using **Spring Boot** that provides authentication and user management features using JWT, MySQL, and the Brevo Email API.

## 🚀 Features

- User Registration
- User Login
- JWT Authentication
- HTTP-Only Cookie Authentication
- User Profile API
- Forgot Password
- OTP Generation
- Password Reset
- Email Verification
- Secure Password Encryption (BCrypt)
- Brevo Email API Integration
- CORS Configuration
- Global Exception Handling

## 🛠️ Tech Stack

- Java 21
- Spring Boot 3
- Spring Security
- Spring Data JPA
- MySQL
- JWT
- Brevo Email API
- Maven

## 📂 Project Structure

```
src
└── main
    ├── controller
    ├── service
    ├── repository
    ├── entity
    ├── filter
    ├── config
    ├── io
    └── util
```

## ⚙️ Environment Variables

Create a `.env` file or configure environment variables.

```env
DB_URL=
DB_USERNAME=
DB_PASSWORD=

JWT_SECRET=

BREVO_API_KEY=

MAIL_FROM=
```

## 📌 API Endpoints

### Authentication

| Method | Endpoint | Description |
|---------|----------|-------------|
| POST | /api/register | Register User |
| POST | /api/login | Login |
| POST | /api/logout | Logout |
| GET | /api/is-authenticated | Check Authentication |

### Password

| Method | Endpoint |
|---------|----------|
| POST | /api/send-reset-otp |
| POST | /api/reset-password |

### Account Verification

| Method | Endpoint |
|---------|----------|
| POST | /api/send-otp |
| POST | /api/verify-otp |

### User

| Method | Endpoint |
|---------|----------|
| GET | /api/profile |

## 🚀 Running Locally

Clone the repository

```bash
git clone https://github.com/Dhina1432/Authify-backend.git
```

Navigate into the project

```bash
cd Authify-backend
```

Build

```bash
mvn clean install
```

Run

```bash
mvn spring-boot:run
```

The backend will start at

```
http://localhost:8080/api
```

## 🔒 Security

- BCrypt Password Encoding
- JWT Authentication
- HTTP-Only Cookies
- CORS Protection
- Stateless Session Management

## 📧 Email Service

The application uses the **Brevo Email API** to send:

- Welcome Emails
- Password Reset OTP
- Email Verification OTP

## 🌍 Deployment

Frontend

- Vercel

Backend

- Railway

Database

- MySQL

## 👨‍💻 Author

**Dhinagaraj**

GitHub:
https://github.com/Dhina1432