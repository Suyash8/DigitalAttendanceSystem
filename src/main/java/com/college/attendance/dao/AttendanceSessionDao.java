package com.college.attendance.dao;

import com.college.attendance.model.AttendanceSession;

import java.sql.*;

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

    /**
     * Finds an active (non-expired) attendance session by its unique code.
     * @param code The session code submitted by the student.
     * @return An AttendanceSession object if a valid, active session is found; otherwise, null.
     */
    public AttendanceSession getValidSessionByCode(String code) {
        // The SQL checks that the current time is before the session's expiration time.
        String sql = "SELECT * FROM AttendanceSessions WHERE session_code = ? AND expires_at > CURRENT_TIMESTAMP";
        AttendanceSession session = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, code);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                session = new AttendanceSession();
                session.setSessionId(rs.getInt("session_id"));
                session.setCourseId(rs.getInt("course_id"));
                session.setSessionCode(rs.getString("session_code"));
                session.setCreatedAt(rs.getTimestamp("created_at"));
                session.setExpiresAt(rs.getTimestamp("expires_at"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return session;
    }
}