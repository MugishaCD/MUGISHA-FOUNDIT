# FOUNDIT: Lost & Found Management System

## Final Project Document

**Prepared By:** Mugisha
**Date:** April 24, 2026
**Project:** FOUNDIT

---

### 1. Executive Summary
FOUNDIT is a robust, end-to-end web application designed to help communities manage lost and found items efficiently. The system replaces fragmented processes with centralized reporting, automated matching algorithms, real-time notifications, and secure user authentication.

### 2. Objectives & Scope
The primary objective is to facilitate the rapid recovery of lost items by connecting finders with losers through a structured platform.
*   **Target Audience:** Students, campus security, office administrators, and general public within a defined community.
*   **Key Capabilities:** Secure User Registration, JWT Authentication, Lost/Found Item Reporting, Automated Categorical Matching, Claim Submission, and Claim Approval Workflows.

### 3. Final Architecture & Technology Stack
The application is built on a modern decoupled architecture:
*   **Backend:** Spring Boot 3.2.3 (Java 21)
    *   *ORM:* Spring Data JPA / Hibernate
    *   *Security:* Spring Security + JWT Tokens (Stateless)
*   **Frontend:** Vanilla HTML5, CSS3, and JavaScript
    *   *Design Language:* "Nexus Glass" (Glassmorphism UI, Responsive)
    *   *Interactivity:* Fetch API with centralized JWT interceptor logic (`api.js`)
*   **Database:** MySQL Server
*   **Containerization:** Docker & Docker Compose (`Dockerfile`, `docker-compose.yml`)

### 4. Core System Logic & Workflows

#### 4.1 Reporting Items
Users can submit detailed descriptions of lost or found articles. The system requires structured data (Category, Location, Date, Color) to power the matching engine.

#### 4.2 Automated Matching Engine
FOUNDIT employs an internal comparison algorithm that automatically suggests potential links between newly reported lost items and existing found items (or vice versa). 
*   **Criteria:** Matches are weighed based on `Category` and `Location`. 
*   **Outcome:** If a high-probability match is logged, both parties receive a notification in their dashboard.

#### 4.3 Claiming Process
When a user sees their lost item on the dashboard (or via a match notification), they can submit a **Claim**.
*   The claim mandates proof of ownership (description, reference text).
*   The system puts the claim in a `PENDING` state until validated by a system administrator or the original finder.
*   Upon `APPROVED` status, the item is marked as `RESOLVED`.

#### 4.4 Data Optimization & Error Handling
*   **Error Handling:** A robust `GlobalExceptionHandler` ensures secure and structured HTTP error responses (400, 401, 403, 409) rather than stack traces.
*   **Performance:** Strategic `@Index` annotations on columns like `status`, `category`, and `email` ensure the database can handle heavy sorting and search filters without full-table loops.

### 5. Final Evaluation Readiness

The system has successfully passed all technical milestones:
*   **Phase 1-8:** Backend entities, DTOs, controllers, and JWT deployment.
*   **Phase 9:** Frontend integration and premium UI overhauls. 
*   **Phase 10:** Integration testing, exception hardening, and performance indexing.
*   **Phase 11/12:** Final packaging, Docker support, and eLearning deliverables preparation.

**Testing Evidence:** API endpoints validated via automated NodeJS scripts (`test_api_v2.js`) and REST structural checks stored in `FOUNDIT_Postman_Collection.json`. All Authentication, Error Handling, and Search criteria perform natively as expected.

### 6. Installation & Execution Guide

**Local Execution (Maven):**
1. Ensure MySQL is running on `locahost:3306` with an empty `foundit` database.
2. In the project root, run:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
3. Open a browser to `http://localhost:8081/index.html`. 

**Docker Containerization:**
1. In the project root, run:
   ```bash
   docker-compose up --build
   ```
2. The complete environment (Database + Backend API + Frontend Static Assets) mounts automatically.

---
**Status:** Completed and ready for Final elearning submission.
