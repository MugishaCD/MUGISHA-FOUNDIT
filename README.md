# FOUNDIT - Lost and Found Management System

FOUNDIT is a robust, end-to-end web application designed to help communities manage lost and found items efficiently. The system features automated matching, real-time notifications, and secure user authentication.

## 🚀 Final Architecture & Features

- **GitHub Repository:** [https://github.com/MugishaCD/FOUNDIT](https://github.com/MugishaCD/FOUNDIT)

### Core Technology Stack
- **Backend:** Spring Boot 3.2.3 (Java 21)
- **Security:** Spring Security + JWT (Stateless)
- **Database:** MySQL
- **Frontend:** HTML5, Vanilla CSS (Glassmorphism), Vanilla JavaScript
- **API Interactivity:** Centralized `api.js` for JWT injection and request handling

### Key System Logic
1.  **Automated Matching Engine:** Uses a JPA-backed comparison algorithm to link lost and found items based on categories, colors, and locations.
2.  **Real-time Notifications:** Users receive instant system notifications when matches are found or claims are processed.
3.  **Claim Workflow:** Secure claim submission and administrative approval process that updates item statuses automatically.
4.  **Optimized Search:** JPA Specifications allow for dynamic, multi-criteria filtering.

---

## 🛠️ Setup & Installation

### Backend (Spring Boot)
1.  **Database Configuration:**
    - Ensure MySQL is running and a database named `foundit` is created.
    - Update `src/main/resources/application.properties` with your database credentials.
2.  **Build and Run:**
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```
    *The API will be available at `http://localhost:8081`.*

### Unified Access (Recommended)
1.  **Run the Backend:** `mvn spring-boot:run`
2.  **Access the Application:** Open your browser to `http://localhost:8081/index.html`

The backend is configured to serve the frontend assets directly for a seamless experience.

### Manual Frontend Access
Alternatively, you can open `frontend/index.html` directly in a browser. Ensure the `API_BASE_URL` in `frontend/js/api.js` is set correctly.

---

## 📦 Deployment (Docker)

The project is fully containerized for easy deployment.

### Using Docker Compose
1.  Ensure Docker and Docker Compose are installed.
2.  Run the following command in the root directory:
    ```bash
    docker-compose up --build
    ```
    This will spin up both the Spring Boot backend and the MySQL database.

---

## 📚 API Overview

| Feature | Base Endpoint |
| :--- | :--- |
| **Auth** | `/api/v1/auth` |
| **Users** | `/api/users` |
| **Lost Items** | `/api/lost-items` |
| **Found Items** | `/api/found-items` |
| **Claims** | `/api/claims` |
| **Notifications** | `/api/notifications` |

---

## ✅ Phase 11 Accomplishments: Finalization
- **Code Optimization:** Removed technical debt (Lombok removal complete) and verified package consistency.
- **Documentation:** Comprehensive `ARCHITECTURE.md` and `README.md` updates.
- **Containerization:** Provided `Dockerfile` and `docker-compose.yml` for production-ready deployment.
- **UI Polishing:** Refined dashboard interactions and glassmorphism styling.

*Developed with care for the FoundIt community by Mugisha.*
