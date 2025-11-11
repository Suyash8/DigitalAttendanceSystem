package com.college.attendance.util;

import com.college.attendance.dao.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseInspector {

    public static void main(String[] args) {
        System.out.println("--- Starting Database State Inspection ---");
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            printTable(stmt, "Users");
            printTable(stmt, "Courses");
            printTable(stmt, "Enrollments");
            printTable(stmt, "AttendanceRecords");

        } catch (SQLException e) {
            System.err.println("An error occurred during database inspection.");
            e.printStackTrace();
        }
        System.out.println("--- Database State Inspection Complete ---");
    }

    /**
     * A helper method to print the contents of a given table.
     * @param stmt The SQL Statement object.
     * @param tableName The name of the table to print.
     * @throws SQLException if a database access error occurs.
     */
    private static void printTable(Statement stmt, String tableName) throws SQLException {
        System.out.println("\n--- Table: " + tableName + " ---");
        
        try (ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName)) {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Print header
            for (int i = 1; i <= columnCount; i++) {
                System.out.printf("%-20s | ", metaData.getColumnName(i));
            }
            System.out.println();
            System.out.println("-".repeat(23 * columnCount));

            // Print rows
            if (!rs.isBeforeFirst()) {
                System.out.println("No data in this table.");
                return;
            }
            
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.printf("%-20s | ", rs.getObject(i));
                }
                System.out.println();
            }
        }
    }
}