package com.college.attendance.controller;

import com.college.attendance.dao.CourseDao;
import com.college.attendance.dao.ReportDao;
import com.college.attendance.model.Course;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/course-detail")
public class CourseDetailServlet extends HttpServlet {
    private ReportDao reportDao;
    private CourseDao courseDao;

    @Override
    public void init() {
        reportDao = new ReportDao();
        courseDao = new CourseDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int courseId = Integer.parseInt(req.getParameter("courseId"));

        // Fetch the main course details (name, code)
        Course course = courseDao.getCourseById(courseId);

        // Fetch the complex grid data
        Map<String, Object> attendanceData = reportDao.getDetailedAttendanceForCourse(courseId);

        req.setAttribute("course", course);
        req.setAttribute("dates", attendanceData.get("dates"));
        req.setAttribute("students", attendanceData.get("students"));
        req.setAttribute("attendanceMap", attendanceData.get("attendanceMap"));

        req.getRequestDispatcher("/course_detail.jsp").forward(req, resp);
    }
}