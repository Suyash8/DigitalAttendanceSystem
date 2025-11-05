package com.college.attendance.dao;

import com.college.attendance.model.AttendanceSession;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class AttendanceSessionDao {

    /**
     * Creates a new attendance session record in the database.
     * @param session The AttendanceSession object to save.
     * @return true if creation was successful, false otherwise.
     */
    public boolean createSession(AttendanceSession session) {
        String sql = "INSERT INTO AttendanceSessions (course_id, session_code, created_at, expires_at) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, session.getCourseId());
            pstmt.setString(2, session.getSessionCode());
            pstmt.setTimestamp(3, session.getCreatedAt());
            pstmt.setTimestamp(4, session.getExpiresAt());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // We will add a method here later to validate a code:
    // public AttendanceSession getValidSessionByCode(String code) { ... }
}