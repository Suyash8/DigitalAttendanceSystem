package com.college.attendance.dao;

import com.college.attendance.model.Course;
import com.college.attendance.model.User;
import com.college.attendance.model.AttendanceRecord;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    /**
     * Fetches all data required to build a detailed attendance grid for a specific course.
     * @param courseId The ID of the course.
     * @return A Map containing three keys: 'dates', 'students', and 'attendanceMap'.
     */
    public Map<String, Object> getDetailedAttendanceForCourse(int courseId) {
        Map<String, Object> result = new HashMap<>();
        // Use LinkedHashSet to maintain the insertion order of dates (chronological)
        Set<Date> dates = new LinkedHashSet<>();
        List<User> students = new ArrayList<>();
        // The map key will be "studentId-yyyy-MM-dd" for easy lookup in the JSP
        Map<String, String> attendanceMap = new HashMap<>();

        // We use a separate DAO for fetching students to keep logic separate
        CourseDao courseDao = new CourseDao();
        students = courseDao.getStudentsByCourseId(courseId);

        String sql = "SELECT student_id, lecture_date, status FROM AttendanceRecords WHERE course_id = ? ORDER BY lecture_date ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Date lectureDate = rs.getDate("lecture_date");
                int studentId = rs.getInt("student_id");
                String status = rs.getString("status");

                // Add the date to our set of unique dates
                dates.add(lectureDate);

                // Create the unique key and add the record to our map
                String key = studentId + "-" + lectureDate.toString();
                attendanceMap.put(key, status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        result.put("dates", new ArrayList<>(dates)); // Convert Set to List for easier iteration in JSP
        result.put("students", students);
        result.put("attendanceMap", attendanceMap);

        return result;
    }
}