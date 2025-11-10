package com.college.attendance.dao;

import com.college.attendance.model.Course; // We'll add a new field to this model
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportDao {

    /**
     * Fetches all courses for a given instructor and calculates the overall attendance percentage for each.
     * @param instructorId The ID of the instructor.
     * @return A list of Course objects, each populated with an overall attendance percentage.
     */
    public List<Course> getCourseAttendanceSummaryForInstructor(int instructorId) {
        List<Course> courses = new ArrayList<>();

        // This complex SQL query does all the heavy lifting in the database for maximum efficiency.
        String sql = "WITH CourseAttendance AS (" +
                "    SELECT " +
                "        c.course_id, c.course_code, c.course_name, " +
                "        COUNT(ar.record_id) AS total_records, " +
                "        COUNT(CASE WHEN ar.status = 'Present' THEN 1 END) AS present_records " +
                "    FROM Courses c " +
                "    LEFT JOIN AttendanceRecords ar ON c.course_id = ar.course_id " +
                "    WHERE c.instructor_id = ? " +
                "    GROUP BY c.course_id, c.course_code, c.course_name" +
                ")" +
                "SELECT " +
                "    *, " +
                "    (CAST(present_records AS DOUBLE) / total_records) * 100 AS attendance_percentage " +
                "FROM CourseAttendance";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, instructorId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Course course = new Course();
                course.setCourseId(rs.getInt("course_id"));
                course.setCourseCode(rs.getString("course_code"));
                course.setCourseName(rs.getString("course_name"));

                // We are retrieving a calculated value, so handle potential division by zero (NaN)
                double percentage = rs.getDouble("attendance_percentage");
                course.setOverallAttendance(Double.isNaN(percentage) ? 0.0 : percentage);

                courses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }
}