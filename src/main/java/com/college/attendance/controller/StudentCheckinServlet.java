package com.college.attendance.controller;

import com.college.attendance.dao.AttendanceDao;
import com.college.attendance.dao.AttendanceSessionDao;
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
import java.util.Collections;

@WebServlet("/student-checkin")
public class StudentCheckinServlet extends HttpServlet {

    private AttendanceSessionDao sessionDao;
    private AttendanceDao attendanceDao;

    @Override
    public void init() {
        sessionDao = new AttendanceSessionDao();
        attendanceDao = new AttendanceDao();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession httpSession = req.getSession();
        User student = (User) httpSession.getAttribute("user");
        String code = req.getParameter("attendanceCode").toUpperCase();

        // 1. Check if the session code is valid and active
        AttendanceSession session = sessionDao.getValidSessionByCode(code);

        if (session == null) {
            // FAILED: Invalid or expired code
            req.setAttribute("errorMessage", "Invalid or expired attendance code. Please try again.");
            req.getRequestDispatcher("/student_dashboard.jsp").forward(req, resp);
            return;
        }

        // 2. If valid, create an attendance record
        AttendanceRecord record = new AttendanceRecord();
        record.setStudentId(student.getUserId());
        record.setCourseId(session.getCourseId());
        record.setLectureDate(new Date(System.currentTimeMillis()));
        record.setStatus("Present");

        // 3. Save the single record
        // The batch save method is overkill but will work fine for a single record.
        boolean success = attendanceDao.saveBatchAttendance(Collections.singletonList(record));

        if (success) {
            // SUCCESS
            resp.sendRedirect(req.getContextPath() + "/student_dashboard.jsp?message=Successfully marked present!");
        } else {
            // FAILED: Database error
            req.setAttribute("errorMessage", "A database error occurred. Please try again.");
            req.getRequestDispatcher("/student_dashboard.jsp").forward(req, resp);
        }
    }
}