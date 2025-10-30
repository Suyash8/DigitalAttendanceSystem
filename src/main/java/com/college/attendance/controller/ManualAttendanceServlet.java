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

@WebServlet("/manual-attendance")
public class ManualAttendanceServlet extends HttpServlet {

    private CourseDao courseDao;

    @Override
    public void init() {
        courseDao = new CourseDao();
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
        // This method will handle the form submission. We will implement this in the next step.
        // For now, it does nothing.
    }
}