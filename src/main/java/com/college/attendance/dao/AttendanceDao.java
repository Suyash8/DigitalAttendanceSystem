package com.college.attendance.dao;

import com.college.attendance.model.AttendanceRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class AttendanceDao {

    /**
     * Saves a batch of attendance records using a robust delete-then-insert pattern
     * within a single transaction.
     * @param records A List of AttendanceRecord objects to save.
     * @return true if the batch save was successful, false otherwise.
     */
    public boolean saveBatchAttendance(List<AttendanceRecord> records) {
        // We can't proceed if there's nothing to save.
        if (records == null || records.isEmpty()) {
            return true; // Technically, nothing failed.
        }

        // We only need the course ID and date once for the DELETE statement.
        int courseId = records.get(0).getCourseId();
        java.sql.Date lectureDate = records.get(0).getLectureDate();

        String deleteSql = "DELETE FROM AttendanceRecords WHERE course_id = ? AND lecture_date = ?";
        String insertSql = "INSERT INTO AttendanceRecords (student_id, course_id, lecture_date, status) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            // Start a transaction
            conn.setAutoCommit(false);

            // Step 1: Delete any old records for this course on this day
            try (PreparedStatement deletePstmt = conn.prepareStatement(deleteSql)) {
                deletePstmt.setInt(1, courseId);
                deletePstmt.setDate(2, lectureDate);
                deletePstmt.executeUpdate();
            }

            // Step 2: Insert all the new records in a batch
            try (PreparedStatement insertPstmt = conn.prepareStatement(insertSql)) {
                for (AttendanceRecord record : records) {
                    insertPstmt.setInt(1, record.getStudentId());
                    insertPstmt.setInt(2, record.getCourseId());
                    insertPstmt.setDate(3, record.getLectureDate());
                    insertPstmt.setString(4, record.getStatus());
                    insertPstmt.addBatch();
                }
                insertPstmt.executeBatch();
            }

            conn.commit(); // Commit the transaction if both steps succeeded
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback(); // On ANY error, roll back the entire transaction
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Always restore default behavior
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}