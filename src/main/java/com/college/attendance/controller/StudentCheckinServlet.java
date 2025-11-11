package com.college.attendance.controller;

import com.college.attendance.dao.AttendanceDao;
import com.college.attendance.dao.AttendanceSessionDao;
import com.college.attendance.dao.CourseDao;
import com.college.attendance.model.AttendanceRecord;
import com.college.attendance.model.AttendanceSession;
import com.college.attendance.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@WebServlet("/student-checkin")
public class StudentCheckinServlet extends HttpServlet {

    private static final int REQUIRED_SCANS = 3;

    private AttendanceSessionDao sessionDao;
    private AttendanceDao attendanceDao;
    private CourseDao courseDao;

    @Override
    public void init() {
        sessionDao = new AttendanceSessionDao();
        attendanceDao = new AttendanceDao();
        courseDao = new CourseDao();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession httpSession = req.getSession();
        User student = (User) httpSession.getAttribute("user");

        // The parameter name now matches our hidden form field
        String codesParam = req.getParameter("attendanceCodes");

        if (codesParam == null || codesParam.isEmpty()) {
            req.setAttribute("errorMessage", "No codes were submitted.");
            req.getRequestDispatcher("/student_dashboard.jsp").forward(req, resp);
            return;
        }

        // Split the comma-separated string into a List of codes
        List<String> submittedCodes = Arrays.asList(codesParam.split(","));

        // 1. Check the database for all valid codes from the submitted list
        List<AttendanceSession> validSessions = sessionDao.getValidSessionsByCodes(submittedCodes);

        // 2. Check if the number of valid codes meets our requirement
        if (validSessions.size() >= REQUIRED_SCANS) {
            // SUCCESS! Mark attendance.
            // We can just use the details from the first valid session to know the courseId.
            int courseId = validSessions.get(0).getCourseId();

            if (!courseDao.isStudentEnrolled(student.getUserId(), courseId)) {
                // FAILED: The student is not enrolled in the course this QR code belongs to.
                req.setAttribute("errorMessage", "Check-in failed. You are not enrolled in this course.");
                req.getRequestDispatcher("/student_dashboard.jsp").forward(req, resp);
                return; // Stop processing immediately
            }

            AttendanceRecord record = new AttendanceRecord();
            record.setStudentId(student.getUserId());
            record.setCourseId(courseId);
            record.setLectureDate(new Date(System.currentTimeMillis()));
            record.setStatus("Present");

            boolean success = attendanceDao.saveBatchAttendance(Collections.singletonList(record));

            if (success) {
                resp.sendRedirect(req.getContextPath() + "/student_dashboard.jsp?message=Success! You have been marked present.");
            } else {
                req.setAttribute("errorMessage", "A database error occurred. Please try again.");
                req.getRequestDispatcher("/student_dashboard.jsp").forward(req, resp);
            }
        } else {
            // FAILED: Not enough valid codes were submitted.
            req.setAttribute("errorMessage", "Scan was unsuccessful. Not enough valid codes were detected. Please try again.");
            req.getRequestDispatcher("/student_dashboard.jsp").forward(req, resp);
        }
    }
}