<div align="center">

# HarmoniQ

**A Full-Stack Music Streaming & Management Platform**

Built with Spring Boot, Spring Security, and JWT — featuring role-based access, real-time chat, an AI assistant, and a complete admin management suite.

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![JWT](https://img.shields.io/badge/Auth-JWT-black?style=for-the-badge&logo=jsonwebtokens&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)

[Overview](#-overview) •
[Features](#-features) •
[Tech Stack](#-tech-stack) •
[Architecture](#-architecture) •
[Getting Started](#-getting-started) •
[Screenshots](#-screenshots)

</div>

---

## 📖 Overview

**HarmoniQ** is a full-stack music streaming and management application built as a final coursework project for the Advanced API Development (AAD) module at IJSE. It pairs a Spring Boot REST API with a vanilla HTML/CSS/JavaScript frontend to deliver a complete music platform experience — separate user and administrator interfaces, secure authentication, playlist and library management, and an integrated AI chat assistant.

The backend follows a clean, layered architecture (Controller → Service → Repository → Entity) with DTO-based request/response contracts, centralized exception handling, and JWT-secured endpoints.

---

## ✨ Features

### 🎧 For Users
- Browse and stream music by track, artist, or genre
- Create, edit, and manage personal playlists
- Like songs and automatically track recently played history
- Follow artists and view dedicated artist profiles
- Full-text search across the music catalog
- Manage profile details and profile picture
- Secure sign-up / sign-in with OTP-based password recovery (email delivery)
- Chat with an integrated AI assistant for music recommendations and help

### 🛠️ For Administrators
- Manage the music catalog (create, update, delete tracks)
- Manage artists and genres, including track-count analytics per genre
- Manage platform users and roles
- Send platform emails directly from the admin dashboard
- Dedicated admin dashboard and profile management

### 🔐 Platform-Wide
- JWT-based authentication and role-based authorization (User / Admin)
- Real-time features via WebSocket
- OAuth2 client integration
- Centralized validation and structured error responses via a global exception handler

---

## 🧰 Tech Stack

| Layer | Technology |
| :--- | :--- |
| **Language** | Java 17 |
| **Framework** | Spring Boot, Spring MVC, Spring WebFlux |
| **Security** | Spring Security, JWT (`jjwt`), OAuth2 Client |
| **Persistence** | Spring Data JPA / Hibernate |
| **Database** | MySQL |
| **Real-Time** | Spring WebSocket |
| **Email** | Spring Mail (OTP delivery) |
| **Mapping** | ModelMapper |
| **Build Tool** | Maven |
| **Frontend** | HTML5, CSS3, Vanilla JavaScript |

---

## 🏗️ Architecture

The backend follows a standard layered architecture, keeping API contracts, business logic, and persistence cleanly separated:

```
┌──────────────────────────────────────────────┐
│              Controller Layer                 │  REST endpoints, request/response DTOs
├──────────────────────────────────────────────┤
│                Service Layer                  │  Business logic, validation, orchestration
├──────────────────────────────────────────────┤
│               Repository Layer                 │  Spring Data JPA repositories
├──────────────────────────────────────────────┤
│                  Entity Layer                  │  JPA-mapped domain models
├──────────────────────────────────────────────┤
│                MySQL Database                  │  Relational persistence
└──────────────────────────────────────────────┘
```

**Key modules:** Auth · User · Music · Artist · Genre · Playlist · Playlist Songs · Liked Songs · Recent Songs · Followed Artists · Email/OTP · AI Chat

Cross-cutting concerns — JWT authentication, OAuth2, WebSocket configuration, and global exception handling — live in dedicated `config` and `exception` packages, keeping controllers thin and focused on request handling.

---

## 🚀 Getting Started

### Prerequisites

- **JDK 17+**
- **MySQL Server**
- **Maven** (or use the included `mvnw` wrapper)
- An IDE such as **IntelliJ IDEA**, **Eclipse**, or **VS Code**
- **Git**

### Installation & Setup

**1. Clone the repository**
```bash
git clone https://github.com/ivanjayyy/AAD_final.git
cd AAD_final
```

**2. Configure the database**

Create a MySQL schema:
```sql
CREATE DATABASE harmoniq_db;
```

Update `HarmoniQ_Backend/src/main/resources/application.properties` with your credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/harmoniq_db
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

**3. Build and run the backend**
```bash
cd HarmoniQ_Backend
mvn clean install
mvn spring-boot:run
```

**4. Run the frontend**

Open any file in the `Frontend/` directory (e.g. `sign-in.html`) in your browser, or serve the folder with a lightweight static server (such as the VS Code Live Server extension).

---

## 📁 Project Structure

```
AAD_final/
├── HarmoniQ_Backend/         # Spring Boot REST API
│   └── src/main/java/com/ijse/gdse73/harmoniq_backend/
│       ├── config/           # Security, WebSocket, application config
│       ├── controller/       # REST controllers
│       ├── dto/               # Request/response contracts
│       ├── entity/            # JPA entities
│       ├── exception/         # Global exception handling
│       ├── repo/              # Spring Data JPA repositories
│       └── service/           # Business logic (+ AI, email sub-packages)
└── Frontend/                  # HTML, CSS, and JavaScript client
    ├── css/
    └── js/
```

---

## 📜 License

Distributed under the MIT License. See `LICENSE` for details.

---

## 📸 Screenshots

### Authentication
| Sign-Up | Sign-In | Forgot Password |
| :---: | :---: | :---: |
| <img src="https://github.com/user-attachments/assets/b4a32293-b392-4e93-9614-a226f0c559f2" width="280"> | <img src="https://github.com/user-attachments/assets/6aea4191-4f21-45fb-8e84-0c1a68d43eb9" width="280"> | <img src="https://github.com/user-attachments/assets/3da343fe-c635-481d-8d57-773057582b51" width="280"> |

### User Side
| Navbar | Home | Liked Songs |
| :---: | :---: | :---: |
| <img src="https://github.com/user-attachments/assets/af01c17f-3dd1-485f-a539-31e5b934b728" width="280"> | <img src="https://github.com/user-attachments/assets/cd718d32-92e2-4258-b236-95ceda13610f" width="280"> | <img src="https://github.com/user-attachments/assets/7edd17f5-84b3-4cdb-aafc-5630c9ba4732" width="280"> |

| AI Assistant | Music Streaming | Search |
| :---: | :---: | :---: |
| <img src="https://github.com/user-attachments/assets/ef42a328-a185-4aee-956b-0ba1e093a1ec" width="280"> | <img src="https://github.com/user-attachments/assets/bf9fa18f-39b0-4014-8d32-6d60b0cfc92e" width="280"> | <img src="https://github.com/user-attachments/assets/1eb39459-7bd4-48c5-a9bf-274d78fcf17f" width="280"> |

| Artists | Artist Profile | Playlists |
| :---: | :---: | :---: |
| <img src="https://github.com/user-attachments/assets/d76c87ed-08e7-405c-81b3-76c13a371039" width="280"> | <img src="https://github.com/user-attachments/assets/bc544b06-f677-44d1-8cb2-b43b3565f520" width="280"> | <img src="https://github.com/user-attachments/assets/91108179-065a-4590-80aa-92a425a44c4a" width="280"> |

| Playlist Songs | Profile |
| :---: | :---: |
| <img src="https://github.com/user-attachments/assets/5fc63741-6454-4cbf-b530-0679331cc4c2" width="280"> | <img src="https://github.com/user-attachments/assets/cb32a123-9b30-4619-85d0-c314fc77da7d" width="280"> |

### Admin Side
| Navbar | Music Management | Artist Management |
| :---: | :---: | :---: |
| <img src="https://github.com/user-attachments/assets/1d562a62-dd37-42bc-a75c-f02e8c5a4ded" width="280"> | <img src="https://github.com/user-attachments/assets/3c08e6bd-61f3-4882-9a2c-ba4e278d7afe" width="280"> | <img src="https://github.com/user-attachments/assets/f8e4cde2-1018-426b-8bb2-4a8f0b36f8be" width="280"> |

| User Management | Email | Genre Management |
| :---: | :---: | :---: |
| <img src="https://github.com/user-attachments/assets/23bdab52-3f2a-44da-9971-95cfb9c050fb" width="280"> | <img src="https://github.com/user-attachments/assets/9f52c143-8012-4383-b504-95f3383cd75a" width="280"> | <img src="https://github.com/user-attachments/assets/eb7d328b-d29c-4b24-ae60-4a2f36dfbf58" width="280"> |

| Admin Profile |
| :---: |
| <img src="https://github.com/user-attachments/assets/6024cbf0-03b1-4550-a0f1-cbea647f13f4" width="280"> |

---

<div align="center">

Developed by **[Ivan Jayasooriya](https://github.com/ivanjayyy)**

</div>
