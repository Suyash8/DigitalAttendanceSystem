package com.college.attendance.util;

import com.college.attendance.dao.DatabaseConnection; // Use your existing class
import java.sql.Connection;
import java.sql.Statement;

public class SchemaInitializer {

    public static void main(String[] args) {

        // 1. Create Users Table
        String createUsersTableSQL = "CREATE TABLE IF NOT EXISTS Users ("
                + "user_id INT AUTO_INCREMENT PRIMARY KEY,"
                + "first_name VARCHAR(100),"
                + "last_name VARCHAR(100),"
                + "email VARCHAR(255) NOT NULL UNIQUE,"
                + "password_hash VARCHAR(255) NOT NULL,"
                + "role VARCHAR(50) NOT NULL,"
                + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                + ");";

        // 2. Create Courses Table
        String createCoursesTableSQL = "CREATE TABLE IF NOT EXISTS Courses ("
                + "course_id INT AUTO_INCREMENT PRIMARY KEY,"
                + "course_code VARCHAR(50) NOT NULL UNIQUE,"
                + "course_name VARCHAR(255) NOT NULL,"
                + "instructor_id INT NOT NULL,"
                + "FOREIGN KEY (instructor_id) REFERENCES Users(user_id)"
                + ");";

        // 3. Create Enrollments Table (linking students to courses)
        String createEnrollmentsTableSQL = "CREATE TABLE IF NOT EXISTS Enrollments ("
                + "enrollment_id INT AUTO_INCREMENT PRIMARY KEY,"
                + "student_id INT NOT NULL,"
                + "course_id INT NOT NULL,"
                + "enrollment_date DATE,"
                + "FOREIGN KEY (student_id) REFERENCES Users(user_id),"
                + "FOREIGN KEY (course_id) REFERENCES Courses(course_id),"
                + "UNIQUE(student_id, course_id)"
                + ");";

        // 4. Create AttendanceRecords Table (for later)
        String createAttendanceTableSQL = "CREATE TABLE IF NOT EXISTS AttendanceRecords ("
                + "record_id INT AUTO_INCREMENT PRIMARY KEY,"
                + "course_id INT NOT NULL,"
                + "student_id INT NOT NULL,"
                + "session_date DATE NOT NULL,"
                + "status VARCHAR(20) NOT NULL, " // e.g., 'Present', 'Absent'
                + "FOREIGN KEY (course_id) REFERENCES Courses(course_id),"
                + "FOREIGN KEY (student_id) REFERENCES Users(user_id)"
                + ");";

        try (Connection conn = DatabaseConnection.getConnection(); // Use your connection
             Statement stmt = conn.createStatement()) {

            // Execute all create table statements
            stmt.execute(createUsersTableSQL);
            System.out.println("✅ SchemaInitializer: Table 'Users' is ready.");

            stmt.execute(createCoursesTableSQL);
            System.out.println("✅ SchemaInitializer: Table 'Courses' is ready.");

            stmt.execute(createEnrollmentsTableSQL);
            System.out.println("✅ SchemaInitializer: Table 'Enrollments' is ready.");

            stmt.execute(createAttendanceTableSQL);
            System.out.println("✅ SchemaInitializer: Table 'AttendanceRecords' is ready.");

            System.out.println("\nAll tables created successfully!");

        } catch (Exception e) {
            System.err.println("❌ Error during schema initialization:");
            e.printStackTrace();
        }
    }
}