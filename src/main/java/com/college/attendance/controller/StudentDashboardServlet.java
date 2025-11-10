package com.college.attendance.controller;

import com.college.attendance.dao.ReportDao;
import com.college.attendance.model.Course;
import com.college.attendance.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/student-dashboard")
public class StudentDashboardServlet extends HttpServlet {
    private ReportDao reportDao;

    @Override
    public void init() {
        reportDao = new ReportDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User student = (User) req.getSession().getAttribute("user");
        List<Course> courses = reportDao.getCourseAttendanceSummaryForStudent(student.getUserId());
        req.setAttribute("courses", courses);
        req.getRequestDispatcher("/student_dashboard.jsp").forward(req, resp);
    }
}