package com.college.attendance.dao;

import com.college.attendance.model.Course;
import com.college.attendance.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    /**
     * Checks if a specific student is enrolled in a specific course.
     * @param studentId The ID of the student.
     * @param courseId The ID of the course.
     * @return true if an enrollment record exists, false otherwise.
     */
    public boolean isStudentEnrolled(int studentId, int courseId) {
        // This query is optimized to just check for the existence of a row,
        // which is faster than retrieving data.
        String sql = "SELECT 1 FROM Enrollments WHERE student_id = ? AND course_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            pstmt.setInt(2, courseId);

            // If the query finds a row, rs.next() will be true.
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // In case of a database error, we should fail safely and assume not enrolled.
            return false;
        }
    }

    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        // Join with Users table to get the instructor's name directly
        String sql = "SELECT c.*, u.first_name, u.last_name FROM Courses c " +
                     "JOIN Users u ON c.instructor_id = u.user_id ORDER BY c.course_name";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Course course = new Course();
                course.setCourseId(rs.getInt("course_id"));
                course.setCourseCode(rs.getString("course_code"));
                course.setCourseName(rs.getString("course_name"));
                course.setInstructorId(rs.getInt("instructor_id"));
                course.setInstructorName(rs.getString("first_name") + " " + rs.getString("last_name"));
                courses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    public boolean createCourse(Course course) {
        String sql = "INSERT INTO Courses (course_code, course_name, instructor_id) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, course.getCourseCode());
            pstmt.setString(2, course.getCourseName());
            pstmt.setInt(3, course.getInstructorId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateCourse(Course course) {
        String sql = "UPDATE Courses SET course_code = ?, course_name = ?, instructor_id = ? WHERE course_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, course.getCourseCode());
            pstmt.setString(2, course.getCourseName());
            pstmt.setInt(3, course.getInstructorId());
            pstmt.setInt(4, course.getCourseId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // You can also add methods for managing enrollments here
    public boolean addEnrollment(int studentId, int courseId) {
        String sql = "INSERT INTO Enrollments (student_id, course_id, enrollment_date) VALUES (?, ?, CURRENT_DATE)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, courseId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // Ignore unique constraint violation errors, which happen if enrollment already exists
            if (!e.getSQLState().equals("23505")) {
                 e.printStackTrace();
            }
            return false;
        }
    }

    public boolean removeEnrollment(int studentId, int courseId) {
        String sql = "DELETE FROM Enrollments WHERE student_id = ? AND course_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, courseId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}