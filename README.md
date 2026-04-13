# concurrent-cab-booking-system

## 📌 Overview

The **Concurrent Cab Booking System** is a Java-based application that simulates real-world cab booking while handling multiple users simultaneously. The system ensures that no two users are assigned the same cab at the same time by managing concurrency and database-level consistency.

This project demonstrates concepts like **multithreading, synchronization (via database operations), and real-time booking logic**.

---

## 🚀 Features

* ✅ Book cabs based on availability
* ✅ Prevents double-booking using concurrent execution
* ✅ Real-time cab allocation
* ✅ Booking success/failure handling
* ✅ Database-driven system using MySQL
* ✅ Simple GUI for interaction

---

## 🛠️ Tech Stack

* **Language:** Java
* **Database:** MySQL
* **Connectivity:** JDBC
* **Concepts Used:**

  * Multithreading
  * Concurrency handling
  * Database transactions
  * Atomic updates

---

## 🧠 Key Concept (Important)

The core challenge solved in this project is:

> **Ensuring that multiple users (threads) do not book the same cab simultaneously.**

Instead of manual locks, this system relies on:

* Database consistency
* Atomic updates
* Controlled query execution

This ensures **safe concurrent access without race conditions**.

---

## ⚙️ Setup Instructions

### 1. Clone the repository

```
git clone https://github.com/your-username/concurrent-cab-booking-system.git
cd concurrent-cab-booking-system
```

### 2. Setup MySQL Database

* Open MySQL
* Run the `cabs.sql` file

### 3. Configure Database Connection

Update credentials in:

```
DatabaseConnection.java
```

### 4. Run the Application

* Compile and run `CabBookingGUI.java`

---

## 📸 Screenshots

* A User Booking
<img width="712" height="612" alt="Screenshot 2026-04-13 at 11 49 44 AM" src="https://github.com/user-attachments/assets/cd567112-1dfb-4d21-8afd-e6ddce83c8b5" />

* Booking Success
  <img width="712" height="612" alt="Screenshot 2026-04-13 at 11 49 44 AM" src="https://github.com/user-attachments/assets/ffb61a85-16f5-4bee-9f9c-c5467a238f11" />

* Simulating multiple user bookings
  <img width="712" height="612" alt="Screenshot 2026-04-13 at 11 50 30 AM" src="https://github.com/user-attachments/assets/ddcd341c-55b6-456b-aab3-aadeee174420" />

* Booking failed for user with unvailable cab
  <img width="712" height="612" alt="Screenshot 2026-04-13 at 11 50 38 AM" src="https://github.com/user-attachments/assets/18db468c-cd1b-42c4-8546-1020b07930dd" />
---

## 📊 Sample Cab Data

| Licence Number | Current Location | Status    |
|---------------|------------------|-----------|
| AP09AB1234    | Madhapur         | available |
| AP09CD9101    | Mehdipatnam      | busy      |
| AP10IJ3456    | Banjara Hills    | available |
| AP12MN4567    | Jubilee Hills    | available |
| AP14QR6789    | Gachibowli       | available |
| TS07XY5678    | Madhapur         | busy      |
| TS08EF2345    | Kondapur         | available |
| TS09GH6789    | Hitech City      | busy      |
| TS11KL7890    | Begumpet         | available |
| TS13OP8901    | Secunderabad     | available |

The system dynamically updates cab availability based on booking status using transactional queries, ensuring consistency under concurrent requests.

---

## 💡 Future Improvements

* Add user authentication
* Implement REST API backend
* Improve UI/UX
* Add real-time tracking
* Deploy as a web application

---

## 🎯 Why This Project Matters

This project goes beyond basic CRUD operations and demonstrates:

* Handling **real-world concurrency problems**
* Understanding of **thread safety**
* Practical use of **database-driven synchronization**

---

## 👩‍💻 Author

**Radha Mayuri Devireddy**

---

