# ⚡HarmoniQ Music Streaming Web App

![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)
![Java](https://img.shields.io/badge/Language-Java-orange?style=for-the-badge)
![Spring Boot](https://img.shields.io/badge/Framework-Spring%20Boot-green?style=for-the-badge)

A robust, enterprise-grade RESTful API application developed for the Advanced API Development (AAD) module, demonstrating best practices in clean backend architecture, security, and persistence.

---

## 📑 Table of Contents
- [About the Project](#about-the-project)
- [Key Features](#key-features)
- [Tech Stack](#tech-stack)
- [Architecture & Design](#architecture--design)
- [Getting Started](#getting-started)
- [Installation & Setup](#installation--setup)
- [License](#license)

---

## 🧐 About the Project

This repository contains the final project for the **Advanced API Development** module. The application is built to serve as a high-performance backend, providing secure REST endpoints, structured payload handling, and decoupled business logic to support scalable web and mobile applications.

---

## ✨ Key Features

* **🌐 RESTful API Endpoints:** Clean URL design following standard HTTP verbs (GET, POST, PUT, DELETE).
* **🔐 Security & Authentication:** Secure resource access using industry-standard authentication patterns.
* **📂 Layered Architecture:** Decoupled Controllers, Services, Repositories, and Data Models.
* **💾 Persistence:** Relational database integration managed via JPA / Hibernate.
* **🛡️ Data Validation & Error Handling:** Centralized exception handling with clear HTTP status codes and error payloads.

---

## 🛠️ Tech Stack

| Layer | Technology |
| :--- | :--- |
| **Language** | Java 17+ |
| **Framework** | Spring Boot / Spring MVC |
| **Security** | Spring Security / JWT |
| **Persistence** | Spring Data JPA / Hibernate |
| **Database** | MySQL / PostgreSQL |
| **Build Tool** | Maven / Gradle |

---

## 🏗️ Architecture & Design

  +----------------------------------------+
  |         API / Controller Layer         |  <-- Exposes REST Endpoints & DTOs
  +-------------------++-------------------+
                      ||
  +-------------------\/-------------------+
  |        Service / Business Layer        |  <-- Executes core API logic & validations
  +-------------------++-------------------+
                      ||
  +-------------------\/-------------------+
  |      Repository / Persistence Layer    |  <-- Handles DB CRUD via JPA / ORM
  +-------------------++-------------------+
                      ||
  +-------------------\/-------------------+
  |         Database / Storage             |  <-- Relational SQL Database
  +----------------------------------------+

---

## 🚀 Getting Started

### Prerequisites

* **JDK 17+** installed
* **MySQL Server** (or your preferred SQL database)
* An IDE such as **IntelliJ IDEA**, **Eclipse**, or **VS Code**
* **Git** installed on your machine

---

## 📥 Installation & Setup

1. **Clone the Repository:**
   git clone https://github.com/ivanjayyy/AAD_final.git
   cd AAD_final

2. **Configure Database:**
   * Create a database schema in MySQL (e.g., `aad_final_db`).
   * Update your database credentials in `src/main/resources/application.properties`:

     spring.datasource.url=jdbc:mysql://localhost:3306/aad_final_db
     spring.datasource.username=YOUR_USERNAME
     spring.datasource.password=YOUR_PASSWORD

3. **Build & Run:**
   mvn clean install
   mvn spring-boot:run

---

## 📜 License

Distributed under the MIT License. See `LICENSE` for details.

---

<p align="center">
  Developed by <a href="https://github.com/ivanjayyy">Ivan Jayasooriya</a>
</p>

## 📸 Screenshots
- Sign-Up page
<img width="1919" height="908" alt="image" src="https://github.com/user-attachments/assets/b4a32293-b392-4e93-9614-a226f0c559f2" />

- Sign-In page
<img width="1919" height="906" alt="image" src="https://github.com/user-attachments/assets/6aea4191-4f21-45fb-8e84-0c1a68d43eb9" />

- Forgot Password page
<img width="1919" height="906" alt="image" src="https://github.com/user-attachments/assets/3da343fe-c635-481d-8d57-773057582b51" />

### User side
- User Navbar
<img width="1919" height="907" alt="image" src="https://github.com/user-attachments/assets/af01c17f-3dd1-485f-a539-31e5b934b728" />

- User Home page
<img width="1903" height="902" alt="image" src="https://github.com/user-attachments/assets/cd718d32-92e2-4258-b236-95ceda13610f" />

- User Liked songs
<img width="1919" height="909" alt="image" src="https://github.com/user-attachments/assets/7edd17f5-84b3-4cdb-aafc-5630c9ba4732" />

- HarmoniQ AI assistant
<img width="1902" height="902" alt="image" src="https://github.com/user-attachments/assets/ef42a328-a185-4aee-956b-0ba1e093a1ec" />

- User Music streaming
<img width="1905" height="904" alt="image" src="https://github.com/user-attachments/assets/bf9fa18f-39b0-4014-8d32-6d60b0cfc92e" />

- User Serach page
<img width="1919" height="905" alt="image" src="https://github.com/user-attachments/assets/1eb39459-7bd4-48c5-a9bf-274d78fcf17f" />

- User Artists page
<img width="1919" height="903" alt="image" src="https://github.com/user-attachments/assets/d76c87ed-08e7-405c-81b3-76c13a371039" />

- User Artist profile
<img width="1919" height="908" alt="image" src="https://github.com/user-attachments/assets/bc544b06-f677-44d1-8cb2-b43b3565f520" />

- User Playlist page
<img width="1919" height="908" alt="image" src="https://github.com/user-attachments/assets/91108179-065a-4590-80aa-92a425a44c4a" />

- User Playlist songs
<img width="1902" height="907" alt="image" src="https://github.com/user-attachments/assets/5fc63741-6454-4cbf-b530-0679331cc4c2" />

- User Profile
<img width="1919" height="906" alt="image" src="https://github.com/user-attachments/assets/cb32a123-9b30-4619-85d0-c314fc77da7d" />

### Admin side
- Admin Navbar
<img width="1919" height="903" alt="image" src="https://github.com/user-attachments/assets/1d562a62-dd37-42bc-a75c-f02e8c5a4ded" />

- Admin Music management
<img width="1919" height="906" alt="image" src="https://github.com/user-attachments/assets/3c08e6bd-61f3-4882-9a2c-ba4e278d7afe" />

- Admin Artist management
<img width="1919" height="904" alt="image" src="https://github.com/user-attachments/assets/f8e4cde2-1018-426b-8bb2-4a8f0b36f8be" />

- Admin User management
<img width="1919" height="903" alt="image" src="https://github.com/user-attachments/assets/23bdab52-3f2a-44da-9971-95cfb9c050fb" />

- Admin Email page
<img width="1919" height="906" alt="image" src="https://github.com/user-attachments/assets/9f52c143-8012-4383-b504-95f3383cd75a" />

- Admin Genre management
<img width="1919" height="905" alt="image" src="https://github.com/user-attachments/assets/eb7d328b-d29c-4b24-ae60-4a2f36dfbf58" />

- Admin profile
<img width="1919" height="901" alt="image" src="https://github.com/user-attachments/assets/6024cbf0-03b1-4550-a0f1-cbea647f13f4" />

