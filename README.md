# FOUNDIT - Lost and Found Management System

Backend API for a comprehensive Lost and Found management platform.

## Phase 7: Authentication & Security

This phase implements a robust security layer using **Spring Security** and **JWT (JSON Web Tokens)** for stateless authentication.

### Key Features
- **JWT Authentication**: Secure, token-based authentication for all API endpoints.
- **Role-Based Access Control**: Standard `USER` and `ADMIN` roles.
- **Security Photo Submission**: Identification verification through personal security photos.
- **Password Protection**: Secure hashing using BCrypt.

## Authentication API Endpoints

All authentication endpoints are prefixed with `/api/v1/auth`.

| Method | Endpoint | Description |
| :----- | :------- | :---------- |
| `POST` | `/register` | Register a new user and receive a token. |
| `POST` | `/login` | Validate credentials and receive a token. |

### Registration Payload
```json
{
  "fullName": "Name",
  "email": "email@example.com",
  "password": "yourPassword",
  "phone": "07xxxxxxxx"
}
```

### Login Payload
```json
{
  "email": "email@example.com",
  "password": "yourPassword"
}
```

## Security Photo API

| Method | Endpoint | Description |
| :----- | :------- | :---------- |
| `PUT` | `/api/users/security-photo` | Submit a verification photo (requires JWT). |

---

## How to Authenticate

To access secured resources, include the JWT token in the `Authorization` header of your HTTP requests:

```http
Authorization: Bearer <your_jwt_token>
```

Secure your application and protect user data!
