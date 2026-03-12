# 🚗 Car Rental Backend

A professional backend for a car rental application, built with **Spring Boot**, **MySQL**, **JWT Security**, and **Docker Compose**.

---

## ✨ Features
- User management with roles (CUSTOMER, EMPLOYEE, ADMIN)
- Authentication and authorization using JWT
- Car and booking management
- Automated workflow:
  - **Creation** → booking created
  - **Confirmation** → employee validates the booking
  - **Return** → car returned
  - **Maintenance** → car sent for service
- Event-based notifications
- API documentation with Swagger UI
- MySQL database containerized with Docker
- Database administration with Adminer

---

## 🛠️ Technologies
- **Java 21 / Spring Boot 3**
- **Spring Security + JWT**
- **Spring Data JPA**
- **MySQL 8**
- **Docker & Docker Compose**
- **Swagger / OpenAPI**
- **Adminer**

---

## ⚙️ Installation

### 1. Clone the project
git clone https://github.com/Anasadiouane/Car_Rental_Project.git
cd Car_Rental_Project

### 2. Run with Docker
docker-compose up --build

### 3. Access services
- API Backend → http://localhost:8081
- Swagger UI → http://localhost:8081/swagger-ui/index.html
- Adminer → http://localhost:8082

## 🔑 Authentication
### 1. Register
Users must first register with:
POST /api/users/register

Example request body:
{
  "email": "customer@test.com",
  "password": "123456",
  "fullName": "John Doe",
  "cin": "CUS-001"
}

### 2. Login
Once registered, users can log in with:
POST /api/auth/login

Example request body:
{
  "email": "customer@test.com",
  "password": "123456"
}

Example response:
{
  "token": "JWT_TOKEN",
  "role": "CUSTOMER"
}


## 👉 Use the token in the header

Authorization: Bearer JWT_TOKEN

  
## 📖 API Documentation
Swagger UI is available at:
http://localhost:8081/swagger-ui/index.html

## 🗄️ Database with Adminer
Adminer is available at:
http://localhost:8082


Connection details:
- System: MySQL
- Server: db
- Username: root
- Password: Anas1234--
- Database: carrental

## 👨‍💻 Contributor
- Anas — Backend Architect & Developer

## 📜 License
This project is licensed under the MIT License. You are free to use and modify it

