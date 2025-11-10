package com.college.attendance.controller;

import com.college.attendance.dao.CourseDao;
import com.college.attendance.dao.ReportDao;
import com.college.attendance.model.AttendanceRecord;
import com.college.attendance.model.Course;
import com.college.attendance.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/student-log")
public class StudentLogServlet extends HttpServlet {
    private ReportDao reportDao;
    private CourseDao courseDao;

    @Override
    public void init() {
        reportDao = new ReportDao();
        courseDao = new CourseDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User student = (User) req.getSession().getAttribute("user");
        int courseId = Integer.parseInt(req.getParameter("courseId"));

        Course course = courseDao.getCourseById(courseId);
        List<AttendanceRecord> records = reportDao.getAttendanceLogForStudent(student.getUserId(), courseId);

        req.setAttribute("course", course);
        req.setAttribute("records", records);
        req.getRequestDispatcher("/student_log.jsp").forward(req, resp);
    }
}