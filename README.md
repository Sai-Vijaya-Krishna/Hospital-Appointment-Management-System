# 🏥 City Hospital - Appointment Management System

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![JWT](https://img.shields.io/badge/JWT-0.11.5-purple.svg)](https://jwt.io/)
[![WebSocket](https://img.shields.io/badge/WebSocket-STOMP-yellow.svg)](https://stomp.github.io/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

A comprehensive full-stack **Hospital Appointment Management System** with real-time features, role-based access control, and digital prescription management. Built to demonstrate enterprise-grade Spring Boot application development with JWT authentication, WebSocket real-time updates, and MySQL database integration.

## 📸 Screenshots 
### 🏠 Homepage
(/<img width="1919" height="916" alt="Homepage" src="https://github.com/user-attachments/assets/7db4716e-b973-4564-9fa3-c49c6efc05e6" />
)

### 👨‍⚕️ Doctor Dashboard
![Doctor](screenshots/doctor-dashboard.png)

### 📊 Admin Dashboard
![Admin](screenshots/admin-dashboard.png)

### 📅 Appointment Booking
![Booking](screenshots/booking.png)

## ✨ Key Features

### 🔐 **Three-Role Authentication System**
- **Patient**: Register, search doctors, book appointments, track queue, view prescriptions
- **Doctor**: View daily queue, mark appointments complete, write digital prescriptions
- **Admin**: Manage doctors, departments, view analytics with Chart.js visualizations

### 🚀 **Real-Time Capabilities**
- **Live Token Queue**: WebSocket + STOMP protocol for instant queue updates
- **Bank-Style Token System**: Sequential token assignment per doctor per day
- **No Refresh Required**: Patients see status changes in real-time

### 📅 **Smart Appointment Booking**
- **Calendar Interface**: Flatpickr.js for intuitive date/time selection
- **Dynamic Slot Loading**: Only available slots shown based on doctor's schedule
- **Double-Booking Prevention**: Database-level uniqueness validation
- **Email Notifications**: Instant booking confirmations via Spring Boot Mail

### 💊 **Digital Prescription Management**
- **Structured Prescriptions**: Diagnosis, medicines, dosage, frequency, duration
- **Multi-Item Support**: Multiple medicines per prescription
- **Patient Access**: View prescriptions anytime online
- **Follow-up Tracking**: Scheduled follow-up dates

### 📊 **Admin Analytics**
- **Dashboard Charts**: Bar and Doughnut charts using Chart.js
- **Department-wise Stats**: Appointment distribution across specialties
- **Status Tracking**: Pending, Completed, Cancelled appointment analytics

---

## 🛠️ Technology Stack

| Category | Technologies |
|----------|-------------|
| **Backend** | Java 17, Spring Boot 3.2.0 |
| **Security** | Spring Security, JWT (jjwt 0.11.5) |
| **Database** | MySQL 8.x with Spring Data JPA |
| **Real-Time** | WebSocket, STOMP, SockJS 1.6.1 |
| **Frontend** | HTML5, CSS3, Vanilla JavaScript |
| **Calendar** | Flatpickr.js |
| **Charts** | Chart.js 4.4.1 |
| **Email** | Spring Boot Mail (Gmail SMTP) |
| **Build Tool** | Maven |
| **IDE** | Spring Tool Suite (STS) |

---

## 🏗️ System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        Frontend (HTML/CSS/JS)                │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐   │
│  │ Patient  │  │  Doctor  │  │  Admin   │  │WebSocket │   │
│  │   UI     │  │    UI    │  │    UI    │  │  Client  │   │
│  └────┬─────┘  └────┬─────┘  └────┬─────┘  └────┬─────┘   │
└───────┼─────────────┼─────────────┼─────────────┼──────────┘
        │             │             │             │
        │   REST API  │   JWT Auth  │   STOMP     │
        ▼             ▼             ▼             ▼
┌─────────────────────────────────────────────────────────────┐
│                    Spring Boot Backend                       │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐     │
│  │ Controllers  │  │  Services    │  │ Repositories │     │
│  │ (REST APIs)  │  │ (Business)   │  │   (JPA)      │     │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘     │
│         │                  │                  │              │
│  ┌──────┴───────┐  ┌──────┴───────┐  ┌──────┴───────┐     │
│  │ JWT Filter   │  │ WebSocket    │  │   Entities   │     │
│  │ (Security)   │  │   Handler    │  │   (Models)   │     │
│  └──────────────┘  └──────────────┘  └──────┬───────┘     │
└─────────────────────────────────────────────┼──────────────┘
                                              │
                                              ▼
                                    ┌──────────────────┐
                                    │  MySQL Database  │
                                    │   (6 Tables)     │
                                    └──────────────────┘
```

---

## 📦 Database Schema

**6 Core Tables:**

1. **users** - Base table for all 3 roles (Patient, Doctor, Admin)
2. **departments** - Medical departments (Cardiology, Neurology, etc.)
3. **doctors** - Doctor profiles linked to users and departments
4. **appointments** - Booking records with token assignment
5. **prescriptions** - Medical prescriptions (1:1 with appointments)
6. **prescription_items** - Medicine details (N:1 with prescriptions)

**Key Relationships:**
- User ↔ Doctor (1:1)
- Department ↔ Doctor (1:N)
- Doctor ↔ Appointments (1:N)
- Appointment ↔ Prescription (1:1)
- Prescription ↔ PrescriptionItems (1:N)

---

## 🚀 Getting Started

### Prerequisites

- **Java Development Kit (JDK)** 17 or higher
- **MySQL** 8.0 or higher
- **Maven** 3.6+ (or use Maven wrapper)
- **Git** for cloning the repository

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/Sai-Vijaya-Krishna/City-Hospital.git
   cd City-Hospital
   ```

2. **Create MySQL Database**
   ```sql
   CREATE DATABASE hospital_db;
   ```

3. **Configure Database & Email**
   
   Open `src/main/resources/application.properties` and update:
   ```properties
   # Database Configuration
   spring.datasource.url=jdbc:mysql://localhost:3306/hospital_db
   spring.datasource.username=root
   spring.datasource.password=YOUR_MYSQL_PASSWORD
   
   # Email Configuration (Gmail)
   spring.mail.username=YOUR_GMAIL@gmail.com
   spring.mail.password=YOUR_16_CHAR_APP_PASSWORD
   ```
   
   > **Note:** For Gmail, enable 2FA and generate an [App Password](https://support.google.com/accounts/answer/185833)

4. **Build and Run**
   ```bash
   # Using Maven
   mvn clean install
   mvn spring-boot:run
   
   # Or using Maven Wrapper
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```

5. **Access the Application**
   ```
   http://localhost:8080
   ```

---

## 🔑 Login Credentials

### Admin Account
```
Email: admin@hospital.com
Password: admin123
URL: http://localhost:8080/admin/dashboard.html
```

### Pre-seeded Doctors (Password: `doctor123`)

| Doctor | Email | Specialization | Department |
|--------|-------|---------------|------------|
| Dr. Ravi Kumar | ravi@hospital.com | Cardiologist | Cardiology |
| Dr. Priya Sharma | priya@hospital.com | Orthopedic Surgeon | Orthopedics |
| Dr. Anil Reddy | anil@hospital.com | Neurologist | Neurology |
| Dr. Sunita Patel | sunita@hospital.com | Dermatologist | Dermatology |
| Dr. Kiran Babu | kiran@hospital.com | Pediatrician | Pediatrics |
| Dr. Meera Nair | meera@hospital.com | ENT Specialist | ENT |

### Patient Account
Patients must self-register at:
```
http://localhost:8080/auth/register.html
```

> **Auto-Seeding:** Admin and all 6 doctors are automatically created by `DataSeeder.java` on first run

---

## 📡 API Documentation

### Authentication APIs
```http
POST /api/auth/register    # Register new patient
POST /api/auth/login       # Login for all roles
```

### Public APIs (No Auth Required)
```http
GET  /api/public/departments              # List all departments
GET  /api/public/doctors                  # List all active doctors
GET  /api/public/slots/{doctorId}?date=   # Get available slots
```

### Patient APIs (Requires PATIENT role)
```http
POST /api/appointments/book               # Book appointment
GET  /api/appointments/my                 # My appointments
PUT  /api/appointments/{id}/status        # Cancel appointment
```

### Doctor APIs (Requires DOCTOR role)
```http
GET  /api/doctor/queue                    # Today's patient queue
PUT  /api/doctor/appointment/{id}/status  # Mark appointment complete
POST /api/doctor/prescription             # Write prescription
GET  /api/doctor/prescription/{apptId}    # View prescription
```

### Admin APIs (Requires ADMIN role)
```http
GET  /api/admin/dashboard                 # Dashboard stats + charts
GET  /api/admin/appointments              # All appointments
GET  /api/admin/patients                  # All patients
POST /api/admin/doctors                   # Add new doctor
PUT  /api/admin/doctors/{id}/toggle       # Activate/Deactivate doctor
```

---

## 🔄 How It Works

### Patient Booking Flow
```
1. Patient searches doctors by department
2. Selects doctor and clicks "Book Now"
3. Calendar opens showing available dates (30 days ahead)
4. Patient picks date → System loads only free time slots
5. Patient selects slot, enters symptoms, and books
6. System validates no double-booking
7. Token number assigned sequentially
8. Email confirmation sent instantly
9. Patient can track queue in real-time via WebSocket
```

### Real-Time Queue System
```
1. Doctor marks patient as COMPLETE
2. Backend updates database status
3. SimpMessagingTemplate broadcasts to /topic/queue/{doctorId}
4. All connected patients subscribed to that doctor's queue
5. Frontend receives WebSocket message
6. Queue display updates instantly without page refresh
```

### JWT Authentication Flow
```
1. User logs in with email/password
2. Backend validates credentials (BCrypt)
3. JwtUtil generates token with role claim (24-hour expiry)
4. Token stored in localStorage
5. Every API request includes: Authorization: Bearer <token>
6. JwtFilter validates token and sets SecurityContext
7. Spring Security enforces role-based access
```

---

## 🐛 Common Issues & Solutions

| Issue | Cause | Fix |
|-------|-------|-----|
| Dashboard shows dashes | JSON serialization crash | Return `Map<String,Object>` instead of raw entities |
| Port 8080 in use | Another app running | Change to `server.port=8081` in application.properties |
| 403 Access Denied | Role mismatch | Check JWT token has correct role claim |
| Email not sending | Wrong App Password | Use 16-char Gmail App Password (not regular password) |
| Double booking allowed | No uniqueness check | Already fixed with `existsByDoctorAndAppointmentDateAndTimeSlot()` |

---

## 🎯 Future Enhancements

- [ ] **Payment Integration**: Razorpay for consultation fee payment
- [ ] **Doctor Availability**: Doctors set custom weekly schedules
- [ ] **PDF Prescriptions**: Download prescriptions as PDF using iText
- [ ] **Rating System**: Patients rate/review doctors post-appointment
- [ ] **SMS Notifications**: Twilio integration for appointment reminders
- [ ] **Unit Testing**: JUnit 5 + Mockito for comprehensive test coverage
- [ ] **Dockerization**: Docker + Docker Compose for easy deployment
- [ ] **Cloud Deployment**: Deploy on AWS/Azure/Render
- [ ] **Mobile App**: React Native companion app

---

## 🧪 Running Tests

```bash
# Run all tests
mvn test

# Run with coverage
mvn clean test jacoco:report
```

---

## 📚 Key Learning Points

This project demonstrates proficiency in:

- ✅ **Spring Boot 3** - Auto-configuration, REST API development
- ✅ **Spring Security** - JWT authentication, role-based access control
- ✅ **Spring Data JPA** - Entity relationships, repository patterns
- ✅ **WebSocket + STOMP** - Real-time bidirectional communication
- ✅ **BCrypt** - Secure password hashing
- ✅ **MySQL** - Relational database design with foreign keys
- ✅ **RESTful Design** - Proper HTTP methods and status codes
- ✅ **Session Management** - Stateless JWT vs traditional sessions
- ✅ **Email Integration** - SMTP with Spring Boot Mail

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

**Sai Vijaya Krishna**

</div>
