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

---

## Phase 8: Advanced Features & Business Logic

Implementation of complex business rules, automated matching, and advanced filtering.

### Key Logic & Business Rules
- **Automated Matching Engine**: 
    - Every time a new Lost or Found item is posted, the `MatchService` is automatically triggered.
    - Items are compared based on **category**, **name**, **color**, and **location**.
    - If a similarity threshold (0.6) is met, a `Match` record is created, and both users are instantly notified.
- **Advanced Filtering**: 
    - Flexible searching for items using JPA Specifications.
    - Search by category, name, location, and status combined.
- **Claim Approval Workflow**:
    - When a claim is **Approved**:
        - The `FoundItem` is marked as `RETURNED`.
        - The associated `LostItem` is marked as `CLAIMED`.
        - The claimant receives a real-time notification.
    - When a claim is **Rejected**, the claimant is notified to take further action.

### Search API Endpoints

| Method | Endpoint | Description |
| :----- | :------- | :---------- |
| `GET` | `/api/lost-items/search` | Dynamic filtering for lost items. |
| `GET` | `/api/found-items/search` | Dynamic filtering for found items. |

---

## Phase 9: Frontend Integration

Seamless integration of the backend REST APIs with a fully responsive, custom-built HTML, CSS, and Vanilla JavaScript frontend interface.

### Key Logic & Features
- **Centralized API Wrapper (`api.js`)**: Robust fetch layer that handles dynamic local configurations, JSON parsing, error handling, and fully automated **JWT Injection** for secure requests.
- **Cross-Origin Configuration**: Spring Security and Tomcat configurations securely bypass CORS limitations across local execution ports (Frontend: `3000`, Backend: `8081`).
- **Complete End-to-End Workflow Views**:
    - **Authentication**: `login.html` and `register.html` communicating effortlessly with `AuthenticationController`, utilizing the `bcrypt` password encoder.
    - **Item & Claim Interfaces**: Responsive dashboards mapped seamlessly to Spring Boot mapping nodes to display real-time FoundIt algorithms.
- **Micro-interactions & UX**: Modern glassmorphism UI, interactive toasts for server error bridging, and automatic unauthenticated routing boundaries.

---

## Phase 10: Testing & Debugging

Comprehensive API testing, enhanced error handling, and performance optimizations.

### Key Enhancements & Testing
- **Granular Error Handling**: `GlobalExceptionHandler` expanded to catch validation errors (`400 Bad Request`), DB integrity violations (`409 Conflict`), and bad credentials (`401 Unauthorized`) for a graceful frontend experience.
- **Database Optimizations**: Added JPA `@Index` on high-traffic variables (`User.email`, `LostItem.status`, `FoundItem.status`, `Item.category`) yielding faster searches.
- **Test Evidence**:
    - **`FOUNDIT_Postman_Collection.json`**: An export of the primary API paths for UI debugging.
    - **`test_api_v2.js`**: An automated end-to-end integration checklist that guarantees JWT workflow success.

---

Developed with care for the FoundIt community.
