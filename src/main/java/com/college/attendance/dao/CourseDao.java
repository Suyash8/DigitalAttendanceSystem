package com.college.attendance.dao;

import com.college.attendance.model.Course;
import com.college.attendance.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDao {

    /**
     * Retrieves a specific course by its ID.
     * @param courseId The ID of the course.
     * @return A Course object, or null if not found.
     */
    public Course getCourseById(int courseId) {
        String sql = "SELECT * FROM Courses WHERE course_id = ?";
        Course course = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                course = new Course();
                course.setCourseId(rs.getInt("course_id"));
                course.setCourseCode(rs.getString("course_code"));
                course.setCourseName(rs.getString("course_name"));
                course.setInstructorId(rs.getInt("instructor_id"));
                course.setCreatedAt(rs.getTimestamp("created_at"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return course;
    }

    /**
     * Retrieves a list of all students currently enrolled in a specific course.
     * @param courseId The ID of the course.
     * @return A List of User objects (students).
     */
    public List<User> getStudentsByCourseId(int courseId) {
        List<User> students = new ArrayList<>();
        // This SQL joins Users and Enrollments to find students in the specific course
        String sql = "SELECT u.* FROM Users u " +
                "JOIN Enrollments e ON u.user_id = e.student_id " +
                "WHERE e.course_id = ? AND u.role = 'student' " +
                "ORDER BY u.last_name, u.first_name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                User student = new User();
                student.setUserId(rs.getInt("user_id"));
                student.setFirstName(rs.getString("first_name"));
                student.setLastName(rs.getString("last_name"));
                student.setEmail(rs.getString("email"));
                // We don't strictly need the password hash here, but we'll keep the object complete
                student.setPasswordHash(rs.getString("password_hash"));
                student.setRole(rs.getString("role"));
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
}