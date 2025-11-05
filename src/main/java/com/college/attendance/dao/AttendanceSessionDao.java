package com.college.attendance.dao;

import com.college.attendance.model.AttendanceSession;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


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

    /**
     * Finds all active (non-expired) attendance sessions that match a list of provided codes.
     * @param codes A List of session codes submitted by the student.
     * @return A List of valid and active AttendanceSession objects. The list will be empty if no codes are valid.
     */
    public List<AttendanceSession> getValidSessionsByCodes(List<String> codes) {
        if (codes == null || codes.isEmpty()) {
            return Collections.emptyList();
        }

        List<AttendanceSession> validSessions = new ArrayList<>();

        // We need to build the SQL with the correct number of question marks (?) for the IN clause.
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * FROM AttendanceSessions WHERE session_code IN (");
        for (int i = 0; i < codes.size(); i++) {
            sqlBuilder.append("?");
            if (i < codes.size() - 1) {
                sqlBuilder.append(",");
            }
        }
        sqlBuilder.append(") AND expires_at > CURRENT_TIMESTAMP");

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlBuilder.toString())) {

            // Set the value for each question mark in the IN clause.
            for (int i = 0; i < codes.size(); i++) {
                pstmt.setString(i + 1, codes.get(i));
            }

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                AttendanceSession session = new AttendanceSession();
                session.setSessionId(rs.getInt("session_id"));
                session.setCourseId(rs.getInt("course_id"));
                session.setSessionCode(rs.getString("session_code"));
                session.setCreatedAt(rs.getTimestamp("created_at"));
                session.setExpiresAt(rs.getTimestamp("expires_at"));
                validSessions.add(session);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return validSessions;
    }
}