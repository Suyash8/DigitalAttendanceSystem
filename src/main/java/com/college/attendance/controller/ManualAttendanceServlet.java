package com.college.attendance.controller;

import com.college.attendance.dao.CourseDao;
import com.college.attendance.model.Course;
import com.college.attendance.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.college.attendance.dao.AttendanceDao; // New import
import com.college.attendance.model.AttendanceRecord; // New import
import java.sql.Date; // New import
import java.util.ArrayList; // New import
import java.util.Enumeration; // New import

@WebServlet("/manual-attendance")
public class ManualAttendanceServlet extends HttpServlet {

    private CourseDao courseDao;
    private AttendanceDao attendanceDao;

    @Override
    public void init() {
        courseDao = new CourseDao();
        attendanceDao = new AttendanceDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // For now, we hardcode the course ID. Later, this would come from the instructor's dashboard link.
        String courseIdParam = req.getParameter("courseId");
        if (courseIdParam == null || courseIdParam.isEmpty()) {
            // Handle error - no course specified
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Course ID is required.");
            return;
        }

        try {
            int courseId = Integer.parseInt(courseIdParam);

            // Fetch the list of students and the course details from the DAO
            List<User> studentRoster = courseDao.getStudentsByCourseId(courseId);
            Course course = courseDao.getCourseById(courseId);

            // Set the data as attributes in the request scope to be accessed by the JSP
            req.setAttribute("studentRoster", studentRoster);
            req.setAttribute("course", course);

            // Forward the request to the JSP page for rendering
            req.getRequestDispatcher("/manual_attendance_form.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Course ID format.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int courseId = Integer.parseInt(req.getParameter("courseId"));
        List<AttendanceRecord> records = new ArrayList<>();
        long millis = System.currentTimeMillis();
        Date today = new Date(millis);

        // The form sends parameters like "status_1", "status_2", etc.
        // We iterate through all parameter names to find the ones for student statuses.
        Enumeration<String> parameterNames = req.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            if (paramName.startsWith("status_")) {
                int studentId = Integer.parseInt(paramName.substring(7)); // "status_".length() == 7
                String status = req.getParameter(paramName);

                AttendanceRecord record = new AttendanceRecord();
                record.setStudentId(studentId);
                record.setCourseId(courseId);
                record.setLectureDate(today);
                record.setStatus(status);
                records.add(record);
            }
        }

        boolean success = attendanceDao.saveBatchAttendance(records);

        if (success) {
            // On success, redirect to the dashboard with a success message
            resp.sendRedirect(req.getContextPath() + "/instructor_dashboard.jsp?message=Attendance+Saved+Successfully");
        } else {
            // On failure, send them back to the form with an error message
            // (A more robust solution might re-populate the form with their selections)
            req.setAttribute("errorMessage", "Failed to save attendance. Please try again.");
            // We need to re-fetch the data for the form before forwarding
            doGet(req, resp);
        }
    }
}