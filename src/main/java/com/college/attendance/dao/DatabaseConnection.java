package com.college.attendance.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DATABASE_URL = "jdbc:h2:./data/attendance_db;DATABASE_TO_UPPER=FALSE";
    private static final String DATABASE_USER = "sa"; // Default H2 user
    private static final String DATABASE_PASSWORD = ""; // Default H2 password is blank
    private static final String JDBC_DRIVER = "org.h2.Driver";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found.");
            e.printStackTrace();
            throw new RuntimeException("JDBC Driver not found.", e);
        } catch (SQLException e) {
            System.err.println("Database connection failed.");
            e.printStackTrace();
            throw new RuntimeException("Could not connect to the database.", e);
        }
        return connection;
    }

    public static void main(String[] args) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn != null) {
                System.out.println("H2 Database connection test successful!");
                conn.close();
            } else {
                System.out.println("H2 Database connection test failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}