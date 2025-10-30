package com.college.attendance.util;

import com.college.attendance.dao.DatabaseConnection; // Use your existing class
import java.sql.Connection;
import java.sql.Statement;

public class SchemaInitializer {

    public static void main(String[] args) {

        // This SQL is based on your UserDao's mapResultSetToUser method
        String createUsersTableSQL = "CREATE TABLE IF NOT EXISTS Users ("
                + "user_id INT AUTO_INCREMENT PRIMARY KEY,"
                + "first_name VARCHAR(100),"
                + "last_name VARCHAR(100),"
                + "email VARCHAR(255) NOT NULL UNIQUE,"
                + "password_hash VARCHAR(255) NOT NULL,"
                + "role VARCHAR(50) NOT NULL,"
                + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                + ");";

        // You will add your other tables here later
        // String createCoursesTableSQL = "...";
        // String createAttendanceTableSQL = "...";

        try (Connection conn = DatabaseConnection.getConnection(); // Use your connection
             Statement stmt = conn.createStatement()) {

            // Execute the create table statement
            stmt.execute(createUsersTableSQL);
            System.out.println("✅ SchemaInitializer: Table 'Users' is ready.");

            // stmt.execute(createCoursesTableSQL);
            // System.out.println("✅ SchemaInitializer: Table 'Courses' is ready.");

            // stmt.execute(createAttendanceTableSQL);
            // System.out.println("✅ SchemaInitializer: Table 'Attendance' is ready.");

        } catch (Exception e) {
            System.err.println("❌ Error during schema initialization:");
            e.printStackTrace();
        }
    }
}