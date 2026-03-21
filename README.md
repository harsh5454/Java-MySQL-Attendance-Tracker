# 📊 Java & MySQL Attendance Tracker

A console-based backend application designed to manage student attendance records, featuring real-time percentage calculations and automated eligibility reporting. 

## 🛠️ Tech Stack
* **Language:** Java (Core)
* **Database:** MySQL
* **Connectivity:** JDBC (MySQL Connector/J)
* **IDE:** Visual Studio Code

## ✨ Key Features
* **Student Registration:** Registers new users and auto-generates unique IDs.
* **Daily Logging:** Records daily attendance with automated timestamps using `CURDATE()`.
* **Analytical Reporting:** Calculates attendance percentages dynamically using complex SQL `LEFT JOIN` queries.
* **Eligibility Tracking:** Flags users automatically if attendance falls below the 75% threshold.
* **Data Security:** Implemented `PreparedStatement` to prevent SQL injection attacks.

## 📸 Output Screenshots

**1. Generating the Analytical Report**
*(Drag and drop your report.png screenshot here on the GitHub website to upload it)*

**2. Database Persistence**
*(Drag and drop your MySQL table screenshot here)*

## 🚀 How to Run Locally
1. Ensure MySQL Server is running.
2. Run the SQL setup script (provided in the code comments) to create the `attendance_db` database.
3. Update the `USER` and `PASS` variables in `AttendanceApp.java` with your local MySQL credentials.
4. Compile and run using the MySQL JDBC Driver:
   ```bash
   javac -cp "mysql-connector-j-9.6.0.jar" AttendanceApp.java
   java -cp ".;mysql-connector-j-9.6.0.jar" AttendanceApp